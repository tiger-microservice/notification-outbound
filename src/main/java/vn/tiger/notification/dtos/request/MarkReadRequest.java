package vn.tiger.notification.dtos.request;

import lombok.Builder;
import lombok.Data;
import vn.tiger.notification.dtos.response.NotifyDto;

import java.util.List;

@Data
@Builder
public class MarkReadRequest {
    List<NotifyDto> notifies;
    Boolean isReadAll;
}
