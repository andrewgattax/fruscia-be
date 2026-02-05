package club.fruscia.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "DTO che rappresenta lo stato completo di una partita")
public class StatoPartita {

    @Schema(
            description = "Numero del turno corrente",
            example = "1"
    )
    private Integer turno;

    @Schema(
            description = "Numero della mano corrente",
            example = "3"
    )
    private Integer mano;

    @Schema(
            description = "Lista dei partecipanti con i rispettivi stati",
            example = "[{\"mano\": [...], \"punteggio\": 10}]"
    )
    private List<StatoPartecipante> partecipanti;
}
