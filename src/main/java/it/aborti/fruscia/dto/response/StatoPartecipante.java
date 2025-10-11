package it.aborti.fruscia.dto.response;

import it.aborti.fruscia.modelGame.Carta;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatoPartecipante {
    private List<Carta> mano;
    private Integer punteggio;
}
