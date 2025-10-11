package it.aborti.fruscia.service;

import it.aborti.fruscia.dto.request.ChiamataRequest;
import it.aborti.fruscia.dto.response.CreaPartitaResponse;
import it.aborti.fruscia.exceptions.FrusciaException;
import it.aborti.fruscia.modelDB.Utente;
import it.aborti.fruscia.modelGame.Partecipante;
import it.aborti.fruscia.modelGame.Partita;
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
    private final CurrentUserService currentUserService;
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
        Utente utente = currentUserService.getCurrentUser();

        String id = generaId();
        Partecipante p = new Partecipante(utente);
        Partita partita = new Partita(List.of(p), id, applicationEventPublisher);
        partite.put(id, partita);

        return CreaPartitaResponse.builder()
                .idPartita(id)
                .build();
    }

    public void joinaPartita(String partitaId) {
        Utente u = currentUserService.getCurrentUser();
        Partita partita = getPartita(partitaId);
        Partecipante p = new Partecipante(u);
        partita.aggiungiPartecipante(p);
    }

    public void iniziaPartita(String partitaId) {
        partite.get(partitaId).iniziaPartita();
    }

    public void faiChiamata(ChiamataRequest request) {
        Utente utente = currentUserService.getCurrentUser();
        Partita partita = getPartita(request.getPartitaId());
        partita.faiChiamata(utente, request);
    }

    public Partita getPartita(String id) {
        if(!partite.containsKey(id)) {
            throw new FrusciaException("Partita non trovata", HttpStatus.NOT_FOUND);
        }
        return partite.get(id);
    }

}
