package club.fruscia.backend.dto.mapper;

import club.fruscia.backend.dto.response.StatoPartecipante;
import club.fruscia.backend.modelGame.Partecipante;

public class PartecipanteMapper {

    public StatoPartecipante toStatoPartecipante(Partecipante partecipante) {
        return StatoPartecipante.builder()
                .mano(partecipante.getMano())
                .punteggio(partecipante.getPunteggio())
                .build();
    }

}
