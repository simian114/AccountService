package account.domain;

import account.constants.SecurityEventEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SecurityEvent {
    @Id
    @GeneratedValue
    private int id;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private SecurityEventEnum action;

    private String subject; // 요청을 날린 사람 (auth에 있는 사람)
    private String object; // 그 대상. 어드민이 A의 비밀번호를 바꾸면 A가 object
    private String path;
}
