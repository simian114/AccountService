package account.web.exception.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidUserException extends RuntimeException{
    static public String USER_ALREADY_EXISTS = "User exist!";
    public InvalidUserException(String reason) {
        super(reason);
    }
}
