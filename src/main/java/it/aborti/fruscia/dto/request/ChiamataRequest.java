package it.aborti.fruscia.dto.request;

import it.aborti.fruscia.modelGame.Chiamata;
import lombok.Data;

import java.util.List;

@Data
public class ChiamataRequest {
    String partitaId;
    Chiamata chiamata;
    List<String> carteIds;
}
