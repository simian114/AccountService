package account.web.exception.ErrorResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    @Override
    public String toString() {
        return String.format("""
                        {
                            "timestamp": "%s",
                            "status": %d,
                            "error": "%s",
                            "message": "%s",
                            "path": "%s"
                        }
                        """,
                LocalDateTime.now(),
                403,
                this.error,
                this.message,
                this.path
        );
    }
}
