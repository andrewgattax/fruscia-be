package club.fruscia.backend.exceptions;

import it.trinex.blackout.exception.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class FrusciaExceptionHandler {

    @ExceptionHandler(FrusciaException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFrusciaException(FrusciaException ex) {
        HttpStatus status = ex.getStatus();

        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(status.name())
                .message(ex.getMessage())
                .category("FRUSCIA_ERROR")
                .build();

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });

        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .message("Validazione fallita")
                .details(details)
                .category("VALIDATION_ERROR")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .message(ex.getMessage())
                .category("INVALID_ARGUMENT")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataIntegrityViolation(DuplicateKeyException ex) {
        String message = "Esista già un account con questo username.";

        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(HttpStatus.CONFLICT.name())
                .message(message)
                .category("DUPLICATE_KEY")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleGenericException(Exception ex) {
        ex.printStackTrace();

        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message("Si è verificato un errore interno")
                .category("INTERNAL_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
