package account.security.service;

import account.constants.SecurityEventEnum;
import account.domain.SecurityEvent;
import account.domain.User;
import account.repository.SecurityEventRepository;
import account.web.dto.SecurityEventDto;
import account.web.mapper.SecurityEventMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class SecurityEventService {
    private final SecurityEventRepository securityEventRepository;
    private final SecurityEventMapper securityEventMapper;


    public void createSecurityEvent(SecurityEventEnum securityEventEnum, HttpServletRequest request, User currentUser, String object) {
        SecurityEventDto dto = SecurityEventDto.builder()
                .date(LocalDateTime.now())
                .action(securityEventEnum.name())
                .subject(currentUser == null ? "Anonymous" : currentUser.getEmail().toLowerCase())
                .object(object)
                .path(request.getRequestURI())
                .build();
        SecurityEvent securityEvent = this.securityEventMapper.toEntity(dto);
        this.securityEventRepository.save(securityEvent);
//        switch (securityEventEnum) {
//            case LOCK_USER -> {
//                System.out.println("LOCK_USER");
//            }
//            case GRANT_ROLE -> {
//                System.out.println("GRANT_ROLE");
//            }
//            case BRUTE_FORCE -> {
//                System.out.println("BRUTE_FORCE");
//            }
//            case CREATE_USER -> {
//                securityEvent = this.securityEventMapper.toEntity(builder.build());
//                System.out.println("CREATE_USER");
//            }
//            case DELETE_USER -> {
//                System.out.println("DELETE_USER");
//            }
//            case REMOVE_ROLE -> {
//                System.out.println("REMOVE_ROLE");
//            }
//            case UNLOCK_USER -> {
//                System.out.println("UNLOCK_USER");
//            }
//            case LOGIN_FAILED -> {
//                System.out.println("LOGIN_FAILED");
//            }
//            case ACCESS_DENIED -> {
//                System.out.println("ACCESS_DENIED");
//            }
//            case CHANGE_PASSWORD -> {
//                System.out.println("CHANGE_PASSWORD");
//            }
//            default -> throw new IllegalStateException("Unexpected value: " + securityEventEnum);
//        }
    }

}
