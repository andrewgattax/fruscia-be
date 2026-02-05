package club.fruscia.backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private String status;
    private String message;
    private Map<String, String> details;
    private String category;
}
