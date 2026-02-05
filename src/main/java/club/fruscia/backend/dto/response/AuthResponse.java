package club.fruscia.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO di risposta per il login con token JWT")
public class AuthResponse {

    @Schema(
            description = "Token JWT per l'autenticazione",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    String token;
}
