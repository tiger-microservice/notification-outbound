package vn.tiger.notification.events;

import com.tiger.common.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import vn.tiger.notification.constants.enums.NotificationType;
import vn.tiger.notification.dtos.request.NotificationInput;
import vn.tiger.notification.exceptions.BusinessLogicException;
import vn.tiger.notification.exceptions.ErrorCode;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAdapterProducer {

    @Value("${spring.kafka.send-email-sending:send-email-sending}")
    public String topicSendEmailSending;

    @Value("${spring.kafka.send-sms-sending:send-sms-sending}")
    public String topicSendSmsSending;

    @Value("${spring.kafka.send-notify-sending:send-notify-sending}")
    public String topicSendNotifySending;

    final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> sendNotify(NotificationInput msgSender) {
        try {
            var key = msgSender.getId() != null ? msgSender.getId().toString() : UUID.randomUUID().toString();
            var completableFuture = kafkaTemplate.send(mappingTopicByNotifyType(msgSender.getType()),
                    key,
                    ObjectMapperUtil.castToString(msgSender));
            return completableFuture.whenComplete(((sendResult, throwable) -> {
                if (throwable != null) {
                    handleFailure(key, msgSender, throwable);
                } else {
                    handleSuccess(key, msgSender, sendResult);
                }
            }));
        } catch (Exception e) {
            log.error("[sendEmailMsg] error {}", e.getMessage(), e);
            throw new BusinessLogicException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private String mappingTopicByNotifyType(NotificationType type) {
        switch (type) {
            case SMS -> {
                return this.topicSendSmsSending;
            }
            case EMAIL -> {
                return this.topicSendEmailSending;
            }
            case NOTIFY -> {
                return this.topicSendNotifySending;
            }
            default -> {
                throw new BusinessLogicException(ErrorCode.BEAN_NOT_DEFINED);
            }
        }
    }

    private void handleSuccess(String key, Object value, SendResult<String, String> sendResult) {
        log.info("Message sent successfully for the key: {} and the value: {}, partition is: {}",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(String key, Object value, Throwable throwable) {
        log.error("Error sending message and exception is {}", throwable.getMessage(), throwable);
    }
}
