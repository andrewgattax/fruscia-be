package club.fruscia.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO di risposta per la creazione di una nuova partita")
public class CreaPartitaResponse {

    @Schema(
            description = "ID univoco della partita creata",
            example = "abc123def456"
    )
    private String idPartita;
}
