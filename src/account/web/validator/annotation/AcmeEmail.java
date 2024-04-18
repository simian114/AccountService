package account.web.validator.annotation;

import account.web.validator.AcmeEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AcmeEmailValidator.class)
public @interface AcmeEmail {
    String message() default "Invalid email format or domain";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
