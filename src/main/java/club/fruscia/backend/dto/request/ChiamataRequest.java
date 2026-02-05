package club.fruscia.backend.dto.request;

import club.fruscia.backend.modelGame.Chiamata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO per effettuare una chiamata durante il gioco")
public class ChiamataRequest {

    @Schema(
            description = "ID della partita",
            example = "abc123def456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "L'ID della partita non può essere vuoto")
    private String partitaId;

    @Schema(
            description = "Tipo di chiamata effettuata",
            example = "FRUSCIA",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La chiamata non può essere nulla")
    private Chiamata chiamata;

    @Schema(
            description = "Lista degli indici delle carte coinvolte",
            example = "[1,3,4]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(message = "Devi selezionare almeno una carta")
    private List<Integer> carteIds;

    @JsonIgnore
    @AssertTrue(message = "Le carte coinvolte possono essere massimo 4")
    private boolean isListSizeValid () {
        return carteIds.size() <= 4;
    }

    @JsonIgnore
    @AssertTrue(message = "Non sono ammessi duplicati")
    private boolean noDuplicates () {
        return carteIds.size() == carteIds.stream().distinct().toList().size();
    }

    @JsonIgnore
    @AssertTrue(message = "Gli unici indici ammessi sono 0,1,2,3")
    private boolean isCardsValid () {
        for (Integer carta : carteIds) {
            if (carta != 0 && carta != 1 && carta != 2 && carta != 3) {
                return false;
            }
        }
        return true;
    }
}
