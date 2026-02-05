package club.fruscia.backend.controller;

import club.fruscia.backend.dto.request.RegistraRequest;
import club.fruscia.backend.modelDB.Utente;
import club.fruscia.backend.service.UtenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Utente", description = "API per la gestione degli utenti")
@RestController
@RequestMapping("/api/utente")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    @Operation(
            summary = "Registra un nuovo utente",
            description = "Crea un nuovo account utente nel sistema con username, password e dati personali"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Utente registrato con successo",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dati di validazione falliti",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username già esistente",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(schema = @Schema(implementation = club.fruscia.backend.exceptions.ExceptionResponseDTO.class))
            )
    })
    @PostMapping("/registra")
    public ResponseEntity<Void> registraUtente(
            @RequestBody @Valid RegistraRequest request) {
        utenteService.registraUtente(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
