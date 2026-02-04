package club.fruscia.backend.modelGame;

import club.fruscia.backend.dto.mapper.PartecipanteMapper;
import club.fruscia.backend.dto.request.ChiamataRequest;
import club.fruscia.backend.dto.response.StatoPartita;
import club.fruscia.backend.exceptions.FrusciaException;
import club.fruscia.backend.modelGame.eventi.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Partita {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PartecipanteMapper partecipanteMapper;

    private List<Partecipante> partecipanti;

    private Mazzo mazzo;

    private final Queue<Carta> mazzoAParte;

    private final String idPartita;

    private Integer turno;

    private boolean isAParte;

    private boolean isSto;

    private Integer mano;

    private Map<Partecipante, Chiamata> chiamate;

    public Partita(List<Partecipante> partecipanti, String idPartita, ApplicationEventPublisher applicationEventPublisher) {
        this.partecipanti = partecipanti;
        this.mazzo = new Mazzo();
        this.idPartita = idPartita;
        this.mazzoAParte = new ConcurrentLinkedQueue<>();
        this.mano = 0;
        this.turno = 0;
        this.isAParte = false;
        this.isSto = false;
        this.chiamate = new ConcurrentHashMap<>();
        this.applicationEventPublisher = applicationEventPublisher;
        this.partecipanteMapper = new PartecipanteMapper();
    }

    public void iniziaPartita() {
        nuovoTurno();
    }

    public void aggiungiPartecipante(Partecipante partecipante) {
        this.partecipanti.add(partecipante);
        aggiornaPartita();
    }

    public StatoPartita getStatoPartita() {
        return StatoPartita.builder()
                .turno(turno)
                .mano(mano)
                .partecipanti(partecipanti.stream().map(partecipanteMapper::toStatoPartecipante).collect(Collectors.toList()))
                .build();
    }

    public void aggiornaPartita() {
        AppEvento appEvento = new UpdateEvento(idPartita, getStatoPartita());
        applicationEventPublisher.publishEvent(appEvento);
    }

    public void checkInterazioneCompleta() {
        if(chiamate.size() < partecipanti.size()) {
            return;
        }
        Optional<Pair<Partecipante, Chiamata>> vincitore = checkVincitore();
        if(vincitore.isPresent()) {
            Partecipante partecipante = vincitore.get().getFirst();
            AppEvento appEvento = new PuntoEvento(idPartita, partecipante, vincitore.get().getSecond());
            applicationEventPublisher.publishEvent(appEvento);
            partecipante.incrementaPunteggio();
            nuovoTurno();
        }
        mano++;
        if(chiamate.containsValue(Chiamata.STO)) {
            isSto = true;
        }
        distribuisciCarte();
    }

    public Optional<Pair<Partecipante, Chiamata>> checkVincitore() {
        Partecipante vincitore = null;
        Chiamata chiamataVincente = null;

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
                case QUARANTANOVE -> p.checkQuarantanove(mano == 1);
                case CINQUANTACINQUE -> p.checkCinquantacinque(mano == 1);
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
                vincitore = candidati.get(0);
                chiamataVincente = tipo;
                for (Partecipante p : candidati) {
                    if (p.getPunteggioMano() > vincitore.getPunteggioMano()) {
                        vincitore = p;
                    } else if (p.getPunteggioMano().equals(vincitore.getPunteggioMano())) {
                        return Optional.empty(); // pareggio
                    }
                }
                return Optional.of(Pair.of(vincitore, chiamataVincente));
            }
        }

        // Se nessuno ha vinto, verifichiamo lo STO solo se almeno un partecipante lo ha chiamato
        boolean stoChiamato = partecipantiConChiamata.stream()
                .anyMatch(p -> chiamate.get(p) == Chiamata.STO);

        if (stoChiamato) {
            vincitore = partecipanti.get(0);
            chiamataVincente = Chiamata.STO;
            for (Partecipante p : partecipanti) {
                if (p.getPunteggioSTO() > vincitore.getPunteggioSTO()) {
                    vincitore = p;
                } else if (p.getPunteggioSTO().equals(vincitore.getPunteggioSTO())) {
                    return Optional.empty(); // pareggio
                }
            }
            return Optional.of(Pair.of(vincitore, chiamataVincente));
        }

        return Optional.empty();
    }


    private Optional<Partecipante> getPartecipanteByUsername(String u) {
        return partecipanti.stream().filter(p -> p.getUsername().equals(u)).findFirst();
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
        mano = 0;
        if (mazzo.getCarte().size() < (partecipanti.size() * 4 + partecipanti.size() * 3) || isAParte) {
            mazzo = new Mazzo();
            isAParte = false;
        }
        this.isSto = false;
        AppEvento appEvento = new NuovoTurnoEvento(idPartita, getStatoPartita());
        applicationEventPublisher.publishEvent(appEvento);
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
        if(mazzo.getCarte().size() < (partecipanti.size() * 3)) {
            isAParte = true;
        }
        aggiornaPartita();
    }
    
    public void finisciPartita() {

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

    public void faiChiamata(String username, ChiamataRequest request) {
        Partecipante p = getPartecipanteByUsername(username)
                .orElseThrow(() -> new FrusciaException("Partecipante non trovato", HttpStatus.NOT_FOUND));
        if(!chiamate.containsKey(p)) {
            chiamate.put(p, request.getChiamata());
            if(request.getChiamata() == Chiamata.SCARTO) {
                scartaCarte(p, request.getCarteIds());
            }
        }

        ChiamataEvento evento = new ChiamataEvento(idPartita, request.getChiamata());

        applicationEventPublisher.publishEvent(evento);

        checkInterazioneCompleta();
    }







}
