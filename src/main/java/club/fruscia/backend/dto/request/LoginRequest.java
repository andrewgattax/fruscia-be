package club.fruscia.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO per il login di un utente")
public class LoginRequest {

    @Schema(
            description = "Username dell'utente",
            example = "mario_rossi",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Lo username non può essere vuoto")
    private String username;

    @Schema(
            description = "Password dell'utente",
            example = "Password123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La password non può essere vuota")
    private String password;
}
