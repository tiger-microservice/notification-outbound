package vn.tiger.notification.dtos.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class NotifyDto {
    String id;
    String title;
    String content;
    String image;
    LocalDateTime createdDate;
    Map<String, String> data;
    Integer status;
}
