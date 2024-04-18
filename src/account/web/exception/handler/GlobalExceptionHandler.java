package account.web.exception.handler;

import account.web.exception.ErrorResponse.CustomErrorResponse;
import account.web.exception.exceptions.Forbidden.ForbiddenException;
import account.web.exception.exceptions.InvalidUserException;
import account.web.exception.exceptions.UnAuthorizedUserException;
import account.web.exception.exceptions.UserNotFoundException;
import account.web.exception.exceptions.notFound.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // error handle for @Valid
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CustomErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getBindingResult().getFieldError().getDefaultMessage(),
                        request.getRequestURI()
                ));
    }

    // ConstraintViolationException caused by @validated -> for collection
    // error handle for not found or bad request and collection bean validation by @Validated
    @ExceptionHandler({InvalidUserException.class, ConstraintViolationException.class})
    public ResponseEntity<CustomErrorResponse> handleInvalidUserException(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler({UserNotFoundException.class, NotFoundException.class})
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CustomErrorResponse(LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler({UnAuthorizedUserException.class})
    public ResponseEntity<CustomErrorResponse> handleUnAuthorizedUserException(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }
}
