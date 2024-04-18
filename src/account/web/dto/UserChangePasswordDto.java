package account.web.dto;

import account.web.validator.annotation.AcmePassword;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDto {

    @AcmePassword
    @JsonProperty(value = "new_password", access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    public String getEmail() {
        return email.toLowerCase();
    }
}
