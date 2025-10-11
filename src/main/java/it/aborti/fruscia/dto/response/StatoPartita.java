package it.aborti.fruscia.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class StatoPartita {

    private Integer turno;

    private Integer mano;

    private List<StatoPartecipante> partecipanti;

}
