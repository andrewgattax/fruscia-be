package club.fruscia.backend.controller;

import club.fruscia.backend.dto.request.ChiamataRequest;
import club.fruscia.backend.dto.response.CreaPartitaResponse;
import club.fruscia.backend.service.PartitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Game", description = "API per la gestione delle partite di gioco")
@Controller
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final PartitaService partitaService;

    @PostMapping("/chiamata")
    private ResponseEntity<String> faiChiamata(@RequestBody @Valid ChiamataRequest request) {
        return ResponseEntity.ok(request.getChiamata().name());
    }

    @Operation(
            summary = "Unisciti a una partita",
            description = "Permette a un utente di unirsi a una partita esistente tramite il suo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utente aggiunto alla partita con successo",
                    content = @Content(schema = @Schema(implementation = String.class, example = "abc123def456"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Partita non trovata",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Richiesta non valida",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            )
    })
    @PostMapping("/join/{partitaId}")
    public ResponseEntity<String> joinGame(
            @Parameter(
                    description = "ID della partita a cui unirsi",
                    required = true,
                    example = "abc123def456"
            )
            @RequestParam String partitaId) {
        partitaService.joinaPartita(partitaId);
        return ResponseEntity.ok().body(partitaId);
    }

    @Operation(
            summary = "Crea una nuova partita",
            description = "Crea una nuova partita di gioco e restituisce l'ID univoco della partita creata"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Partita creata con successo",
                    content = @Content(schema = @Schema(implementation = CreaPartitaResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<CreaPartitaResponse> creaPartita() {
        return ResponseEntity.ok().body(partitaService.creaPartita());
    }

    @MessageMapping("/game/start")
    public void iniziaPartita(
            @Payload String partitaId) {
        partitaService.iniziaPartita(partitaId);
    }

}
