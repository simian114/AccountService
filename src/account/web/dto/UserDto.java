package account.web.dto;

import account.web.validator.annotation.AcmeEmail;
import account.web.validator.annotation.AcmePassword;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    @AcmeEmail
    private String email;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @AcmePassword
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> roles;
}
