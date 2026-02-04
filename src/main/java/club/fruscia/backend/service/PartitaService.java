package club.fruscia.backend.service;

import club.fruscia.backend.dto.request.ChiamataRequest;
import club.fruscia.backend.dto.response.CreaPartitaResponse;
import club.fruscia.backend.exceptions.FrusciaException;
import club.fruscia.backend.modelDB.Utente;
import club.fruscia.backend.modelGame.Partecipante;
import club.fruscia.backend.modelGame.Partita;
import it.trinex.blackout.security.BlackoutUserPrincipal;
import it.trinex.blackout.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PartitaService {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final CurrentUserService<BlackoutUserPrincipal> currentUserService;
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationEventPublisher applicationEventPublisher;

    private Map<String, Partita> partite = new ConcurrentHashMap<>();

    private String generaId() {
        StringBuilder result = new StringBuilder();
        do {
            result.setLength(0);
            for (int i = 0; i < 6; i++) {
                int randomIndex = (int) Math.floor(Math.random() * LETTERS.length());
                result.append(LETTERS.charAt(randomIndex));
            }
            return result.toString();
        } while (partite.containsKey(result.toString()));
    }

    public CreaPartitaResponse creaPartita() {
        String username = currentUserService.getCurrentPrincipal().getUsername();

        String id = generaId();
        Partecipante p = new Partecipante(username);
        Partita partita = new Partita(List.of(p), id, applicationEventPublisher);
        partite.put(id, partita);

        return CreaPartitaResponse.builder()
                .idPartita(id)
                .build();
    }

    public void joinaPartita(String partitaId) {
        String username = currentUserService.getCurrentPrincipal().getUsername();
        Partita partita = getPartita(partitaId);
        Partecipante p = new Partecipante(username);
        partita.aggiungiPartecipante(p);
    }

    public void iniziaPartita(String partitaId) {
        partite.get(partitaId).iniziaPartita();
    }

    public void faiChiamata(ChiamataRequest request) {
        String username = currentUserService.getCurrentPrincipal().getUsername();
        Partita partita = getPartita(request.getPartitaId());
        partita.faiChiamata(username, request);
    }

    public Partita getPartita(String id) {
        if(!partite.containsKey(id)) {
            throw new FrusciaException("Partita non trovata", HttpStatus.NOT_FOUND);
        }
        return partite.get(id);
    }

}
