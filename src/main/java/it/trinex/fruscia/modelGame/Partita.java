package it.trinex.fruscia.modelGame;

import it.trinex.fruscia.dto.request.ChiamataRequest;
import it.trinex.fruscia.exceptions.PartecipanteNotFoundException;
import it.trinex.fruscia.modelDB.Utente;
import org.springframework.security.core.parameters.P;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Partita {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private List<Partecipante> partecipanti;

    private Mazzo mazzo;

    private final Queue<Carta> mazzoAParte;

    private String idPartita;

    private Integer turno;

    private boolean isAParte;

    private boolean isSto;

    private boolean isPrimaMano;

    private Map<Partecipante, Chiamata> chiamate;

    public Partita(List<Partecipante> partecipanti) {
        this.partecipanti = partecipanti;
        this.mazzo = new Mazzo();
        this.idPartita = generateID();
        this.mazzoAParte = new ConcurrentLinkedQueue<>();
        this.turno = 0;
        this.isAParte = false;
        this.isSto = false;
        this.chiamate = new ConcurrentHashMap<>();
    }

    private String generateID() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = (int) Math.floor(Math.random() * LETTERS.length());
            result.append(LETTERS.charAt(randomIndex));
        }
        return result.toString();
    }

    public void checkInterazioneCompleta() {
        if(chiamate.size() < partecipanti.size()) {
            return;
        }
        Optional<Partecipante> p = checkVincitore();
        if(p.isPresent()) {
            Partecipante partecipante = p.get();
            // TODO: Logica di vittoria
            partecipante.incrementaPunteggio();
            nuovoTurno();
        }
        isPrimaMano = false;
        if(chiamate.containsValue(Chiamata.STO)) {
            isSto = true;
        }
    }

    public Optional<Partecipante> checkVincitore() {
        Partecipante vincitore = null;

        // Prendiamo solo i partecipanti che hanno fatto una chiamata
        List<Partecipante> partecipantiConChiamata = new ArrayList<>(chiamate.keySet());

        // Aggiorniamo i flag di ogni partecipante in base alla chiamata fatta
        for (Partecipante p : partecipantiConChiamata) {
            Chiamata c = chiamate.get(p);
            if (c == null) continue;

            switch (c) {
                case FRUSCIA -> p.checkFruscia();
                case COLORE -> p.checkColore();
                case POKER -> p.checkPoker();
                case QUARANTANOVE -> p.checkQuarantanove(isPrimaMano);
                case CINQUANTACINQUE -> p.checkCinquantacinque(isPrimaMano);
                // STO non si verifica qui
            }
        }

        // Ordiniamo la gerarchia delle chiamate
        List<Chiamata> gerarchia = List.of(
                Chiamata.POKER,
                Chiamata.CINQUANTACINQUE,
                Chiamata.COLORE,
                Chiamata.FRUSCIA,
                Chiamata.QUARANTANOVE
        );

        // Cerchiamo il vincitore tra chi ha fatto chiamata (escluso STO)
        for (Chiamata tipo : gerarchia) {
            List<Partecipante> candidati = partecipantiConChiamata.stream()
                    .filter(p -> {
                        return switch (tipo) {
                            case FRUSCIA -> p.isFruscia();
                            case COLORE -> p.isColore();
                            case POKER -> p.isPoker();
                            case QUARANTANOVE -> p.isQuarantanove();
                            case CINQUANTACINQUE -> p.isCinquantacinque();
                            default -> false;
                        };
                    }).toList();

            if (!candidati.isEmpty()) {
                // tra i candidati prendiamo chi ha il punteggio mano più alto
                vincitore = candidati.getFirst();
                for (Partecipante p : candidati) {
                    if (p.getPunteggioMano() > vincitore.getPunteggioMano()) {
                        vincitore = p;
                    } else if (p.getPunteggioMano().equals(vincitore.getPunteggioMano())) {
                        return Optional.empty(); // pareggio
                    }
                }
                return Optional.of(vincitore);
            }
        }

        // Se nessuno ha vinto, verifichiamo lo STO solo se almeno un partecipante lo ha chiamato
        boolean stoChiamato = partecipantiConChiamata.stream()
                .anyMatch(p -> chiamate.get(p) == Chiamata.STO);

        if (stoChiamato) {
            vincitore = partecipanti.getFirst();
            for (Partecipante p : partecipanti) {
                if (p.getPunteggioSTO() > vincitore.getPunteggioSTO()) {
                    vincitore = p;
                } else if (p.getPunteggioSTO().equals(vincitore.getPunteggioSTO())) {
                    return Optional.empty(); // pareggio
                }
            }
            return Optional.of(vincitore);
        }

        return Optional.empty();
    }

    private Optional<Partecipante> getPartecipanteByUtente(Utente u) {
        return partecipanti.stream().filter(p -> p.getUtente().equals(u)).findFirst();
    }

    private void nuovoMazzoConScarto() {
        mazzo = new Mazzo();
        for (Partecipante partecipante : partecipanti) {
            mazzoAParte.addAll(partecipante.getMano());
        }
        mazzo.getCarte().removeAll(mazzoAParte);
    }

    private void resetStatoPartecipanti() {
        for(Partecipante partecipante : partecipanti) {
            partecipante.resetPerNuovoTurno();
        }
    }

    public void nuovoTurno() {
        turno++;
        isPrimaMano = true;
        if (mazzo.getCarte().size() < (partecipanti.size() * 4 + partecipanti.size() * 3) || isAParte) {
            mazzo = new Mazzo();
            isAParte = false;
        }
        this.isSto = false;
        resetStatoPartecipanti();
        distribuisciCarte();
    }

    public void distribuisciCarte() {
        for (Partecipante partecipante : partecipanti) {
            int cartePartecipante = 4 - partecipante.getMano().size();
            for(int i = 0; i < cartePartecipante; i++) {
                try {
                    partecipante.getMano().add(mazzo.pescaCarta());
                } catch (IndexOutOfBoundsException e) {
                    nuovoMazzoConScarto();
                    partecipante.getMano().add(mazzo.pescaCarta());
                }
            }
        }
        Optional<Partecipante> vincitore = checkVincitore();
        if(vincitore.isPresent()) {
            vincitore.get().incrementaPunteggio();
            nuovoTurno();
        }
        if(mazzo.getCarte().size() < (partecipanti.size() * 3)) {
            isAParte = true;
        }
    }

    public void scartaCarte(Partecipante p, List<String> carteIds) {

        List<Carta> carteDaRimuovere = new ArrayList<>();

        for (String cartaId : carteIds) {
            p.getMano().stream()
                    .filter(c -> c.getId().equals(cartaId))
                    .forEach(carteDaRimuovere::add);
        }

        p.getMano().removeAll(carteDaRimuovere);

        if (isAParte) {
            mazzoAParte.addAll(carteDaRimuovere);
        }
    }

    public void faiChiamata(Utente utente, ChiamataRequest request) {
        Partecipante p = getPartecipanteByUtente(utente)
                .orElseThrow(() -> new PartecipanteNotFoundException("Partecipante non trovato"));
        if(!chiamate.containsKey(p)) {
            chiamate.put(p, request.getChiamata());
            if(request.getChiamata() == Chiamata.SCARTO) {
                scartaCarte(p, request.getCarteIds());
            }
        }
        checkInterazioneCompleta();
    }







}
