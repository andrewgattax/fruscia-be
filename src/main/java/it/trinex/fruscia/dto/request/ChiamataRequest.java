package it.trinex.fruscia.dto.request;

import it.trinex.fruscia.modelGame.Chiamata;
import lombok.Data;

import java.util.List;

@Data
public class ChiamataRequest {
    Chiamata chiamata;
    List<String> carteIds;
}
