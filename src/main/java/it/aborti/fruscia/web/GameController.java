package it.aborti.fruscia.web;

import it.aborti.fruscia.dto.response.CreaPartitaResponse;
import it.aborti.fruscia.modelGame.Partita;
import it.aborti.fruscia.service.PartitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final PartitaService partitaService;

    @PostMapping("/join/{partitaId}")
    public ResponseEntity<String> joinGame(@RequestParam String partitaId) {
        partitaService.joinaPartita(partitaId);
        return ResponseEntity.ok().body(partitaId);
    }

    @PostMapping
    public ResponseEntity<CreaPartitaResponse> creaPartita() {
        return ResponseEntity.ok().body(partitaService.creaPartita());
    }

    @MessageMapping("/game/start")
    public void iniziaPartita(@Payload String partitaId) {
        partitaService.iniziaPartita(partitaId);
    }

}
