package account.web.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithStatusDto {
    private String user;
    private String status;
}
