package account.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminUserRoleUpdateDto {
    @NotBlank
    private String user;

    @NotBlank
    private String role;

    private Operation operation;

    public static enum Operation {
        GRANT,
        REMOVE
    }
}
