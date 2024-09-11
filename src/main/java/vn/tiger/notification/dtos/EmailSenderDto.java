package vn.tiger.notification.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import vn.tiger.notification.dtos.request.NotificationInput;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EmailSenderDto extends NotificationInput {
    String subject;
    String content;
}
