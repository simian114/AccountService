package account.web.exception.exceptions.Forbidden;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Access Denied!");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
