package vn.tiger.notification.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import vn.tiger.notification.dtos.request.NotificationInput;

@Data
@SuperBuilder
public class EmailSenderDto extends NotificationInput {
    String subject;
    String content;
}
