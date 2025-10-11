package it.aborti.fruscia.dto.mapper;

import it.aborti.fruscia.dto.response.StatoPartecipante;
import it.aborti.fruscia.modelGame.Partecipante;

public class PartecipanteMapper {

    public StatoPartecipante toStatoPartecipante(Partecipante partecipante) {
        return StatoPartecipante.builder()
                .mano(partecipante.getMano())
                .punteggio(partecipante.getPunteggio())
                .build();
    }

}
