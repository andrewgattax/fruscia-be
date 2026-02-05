package club.fruscia.backend.dto.response;

import club.fruscia.backend.modelGame.Carta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "DTO che rappresenta lo stato di un partecipante alla partita")
public class StatoPartecipante {

    @Schema(
            description = "Lista delle carte in mano al partecipante",
            example = "[{\"seme\": \"CUORI\", \"valore\": \"ASSO\"}]"
    )
    private List<Carta> mano;

    @Schema(
            description = "Punteggio attuale del partecipante",
            example = "10"
    )
    private Integer punteggio;
}
