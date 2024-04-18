package account.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class SecurityEventDto {
    private LocalDateTime date;
    private String action;
    private String subject;
    private String object;
    private String path;
}
