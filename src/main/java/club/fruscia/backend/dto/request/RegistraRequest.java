package club.fruscia.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "DTO per la registrazione di un nuovo utente")
public class RegistraRequest {

    @Schema(
            description = "Nome dell'utente",
            example = "Mario",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    @Schema(
            description = "Cognome dell'utente",
            example = "Rossi",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Il cognome non può essere vuoto")
    private String cognome;

    @Schema(
            description = "Username univoco per l'accesso",
            example = "mario_rossi",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Lo username non può essere vuoto")
    private String username;

    @Schema(
            description = "Password dell'utente (min 8, max 20 caratteri)",
            example = "Password123!",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8,
            maxLength = 20
    )
    @NotBlank(message = "La password non può essere vuota")
    private String password;

    @Schema(
            description = "Conferma della password",
            example = "Password123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La password deve essere ripetuta")
    private String ripetiPassword;

    @Schema(
            description = "Data di nascita dell'utente",
            example = "1990-05-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La data di nascita non può essere vuota")
    @Past(message = "La data di nascita non può essere nel passato")
    private LocalDate dataNascita;

    @JsonIgnore
    @AssertTrue(message = "Le password non corrispondono")
    public boolean isRepeatPasswordValid() {
        return password.equals(ripetiPassword);
    }

    @JsonIgnore
    @AssertTrue(message = "La password deve essere tra gli 8 e i 20 caratteri")
    public boolean isPasswordValid() {
        return password.length() >= 8 && password.length() <= 20;
    }
}
