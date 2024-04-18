package account.web.mapper;

import account.constants.SecurityEventEnum;
import account.domain.SecurityEvent;
import account.web.dto.SecurityEventDto;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventMapper {
    public SecurityEventDto toDto(SecurityEvent securityEvent) {
        return SecurityEventDto.builder()
                .date(securityEvent.getDate())
                .action(securityEvent.getAction().name())
                .object(securityEvent.getObject())
                .path(securityEvent.getPath())
                .subject(securityEvent.getSubject())
                .build();
    }

    public SecurityEvent toEntity(SecurityEventDto securityEventDto) {
        return SecurityEvent.builder()
                .date(securityEventDto.getDate())
                .action(SecurityEventEnum.valueOf(securityEventDto.getAction()))
                .object(securityEventDto.getObject())
                .path(securityEventDto.getPath())
                .subject(securityEventDto.getSubject())
                .build();
    }
}
