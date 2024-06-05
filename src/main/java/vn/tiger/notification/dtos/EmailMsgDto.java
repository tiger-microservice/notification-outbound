package vn.tiger.notification.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class EmailMsgDto {
    UUID id;
    String templateCode;
    String notificationType;
    Map<String, String> data;
}
