package club.fruscia.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FrusciaException extends RuntimeException {

    private final HttpStatus status;

    public FrusciaException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FrusciaException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
