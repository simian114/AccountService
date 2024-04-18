package account.web.validator;

import account.web.validator.annotation.AcmeEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class AcmeEmailValidator implements ConstraintValidator<AcmeEmail, String> {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:acme\\.com)$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void initialize(AcmeEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("in validator!!!!");
        return value != null && PATTERN.matcher(value).matches();
    }

}
