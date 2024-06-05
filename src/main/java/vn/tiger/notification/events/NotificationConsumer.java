package vn.tiger.notification.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.dtos.messages.EmailMessage;
import vn.tiger.notification.dtos.messages.NotificationMessage;
import vn.tiger.notification.dtos.messages.SmsMessage;
import vn.tiger.notification.dtos.request.NotificationInput;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    @Qualifier("taskExecutor")
    final TaskExecutor taskExecutor;
    final NotificationAdapterProducer notificationAdapterProducer;
    final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {"${spring.kafka.consumer.send-notify-priority}"},
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void consumeNotifyPriority(@Payload List<String> messages,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.info("[consumeNotifyPriority] messages {}", messages.size());
        log.info("[consumeNotifyPriority] groupId {}", groupId);
        log.info("[consumeNotifyPriority] topic {}", topic);
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            try {
                String type = objectMapper.readTree(message).get("type").asText();
                NotificationInput input = getNotificationInput(type, message);
                this.taskExecutor.execute(() -> {
                    notificationAdapterProducer.sendNotify(input);
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private NotificationInput getNotificationInput(String type, String message) {
        NotificationType notificationType = NotificationType.valueOf(type);
        switch (notificationType) {
            case NOTIFY:
                return ObjectMapperUtil.castToObject(message, NotificationMessage.class);
            case SMS:
                return ObjectMapperUtil.castToObject(message, SmsMessage.class);
            case EMAIL:
                return ObjectMapperUtil.castToObject(message, EmailMessage.class);
            default:
                throw new IllegalArgumentException("Unknown message type: " + type);
        }
    }
}
