package account.web.validator.annotation;

import account.web.validator.AcmePasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcmePasswordValidator.class)
public @interface AcmePassword {
    String message() default "The password is in the hacker's database!";

    String lengthErrorMessage() default "Password length must be 12 chars minimum!";

    String breachedPasswordErrorMessage() default "The password is in the hacker's database!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
