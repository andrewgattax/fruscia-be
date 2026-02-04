package club.fruscia.backend.dto.request;

import club.fruscia.backend.modelGame.Chiamata;
import lombok.Data;

import java.util.List;

@Data
public class ChiamataRequest {
    String partitaId;
    Chiamata chiamata;
    List<String> carteIds;
}
