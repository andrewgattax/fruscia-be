package club.fruscia.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatoPartita {

    private Integer turno;

    private Integer mano;

    private List<StatoPartecipante> partecipanti;

}
