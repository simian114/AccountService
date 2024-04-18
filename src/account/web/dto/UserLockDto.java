package account.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLockDto {
    @NotBlank
    private String user;

    private Operation operation;

    public static enum Operation {
        LOCK,
        UNLOCK
    }
}
