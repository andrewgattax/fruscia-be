package club.fruscia.backend.dto.response;

import club.fruscia.backend.modelGame.Carta;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatoPartecipante {
    private List<Carta> mano;
    private Integer punteggio;
}
