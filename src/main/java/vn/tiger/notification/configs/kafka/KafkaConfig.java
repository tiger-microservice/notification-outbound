package vn.tiger.notification.configs.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic sendSmsEventsTopic() {
        return TopicBuilder.name("send-sms")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sendEmailEventsTopic() {
        return TopicBuilder.name("send-email")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sendNotifyEventsTopic() {
        return TopicBuilder.name("send-notify")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
