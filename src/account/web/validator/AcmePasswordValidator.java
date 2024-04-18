package account.web.validator;

import account.web.validator.annotation.AcmePassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AcmePasswordValidator implements ConstraintValidator<AcmePassword, String> {
    static private final String[] BREACHED_PASSWORDS = new String[]{
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    };
    private String lengthErrorMessage;
    private String breachedErrorMessage;

    @Override
    public void initialize(AcmePassword constraintAnnotation) {
        // TODO: is it a good pattern? or bad?
        lengthErrorMessage = constraintAnnotation.lengthErrorMessage();
        breachedErrorMessage = constraintAnnotation.breachedPasswordErrorMessage();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        log.error("password:" + password);
        /*
         * TODO: find out which one is better way.
         *  1. throw exception
         *  2. return false
         */
        if (password == null || password.length() < 12) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(lengthErrorMessage)
                    .addConstraintViolation();
            return false;
        }
        for (String breachedPassword : BREACHED_PASSWORDS) {
            if (password.equals(breachedPassword)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(breachedErrorMessage)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
