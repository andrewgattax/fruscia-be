package it.trinex.fruscia.modelGame;

import it.trinex.fruscia.modelDB.Utente;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
public class Partecipante {

    private Utente utente;

    private List<Carta> mano;

    private Integer punteggio;

    private boolean fruscia;

    private boolean colore;

    private boolean poker;

    private boolean cinquantacinque;

    private boolean quarantanove;

    public void incrementaPunteggio() {
        punteggio++;
    }

    public void resetPerNuovoTurno() {
        fruscia = false;
        colore = false;
        poker = false;
        mano.clear();
    }

    public void checkFruscia() {
        Set<Palo> pali = new HashSet<>();
        for (Carta c : mano) {
            pali.add(c.getPalo());
        }
        this.fruscia = pali.size() == mano.size();
    }

    public void checkColore() {
        Palo primoPalo = mano.getFirst().getPalo();
        for (Carta c : mano) {
            if (c.getPalo() != primoPalo) return;
        }
        this.colore = true;
    }

    public void checkPoker() {
        Integer primoNumero = mano.getFirst().getNumero();
        for (Carta c : mano) {
            if (!Objects.equals(c.getNumero(), primoNumero)) return;
        }
        this.poker = true;
    }

    public void checkQuarantanove(boolean isPrimaMano) {
        if (!isPrimaMano) {
            return;
        }
        if(getPunteggioSTO() >= 49) {
            quarantanove = true;
        }
    }

    public void checkCinquantacinque(boolean isPrimaMano) {
        if (!isPrimaMano) {
            return;
        }
        if(getPunteggioSTO() >= 55) {
            cinquantacinque = true;
        }
    }

    public Integer getPunteggioMano() {
        Integer punteggio = 0;
        for (Carta c : mano) {
            punteggio += c.getValore();
        }
        return punteggio;
    }

    public Integer getPunteggioSTO() {
        if (mano == null || mano.isEmpty()) {
            return 0;
        }

        // Mappa per accumulare i punteggi per ogni palo
        java.util.Map<Palo, Integer> punteggiPerPalo = new java.util.HashMap<>();

        // Scorre tutte le carte e accumula i valori per palo
        for (Carta c : mano) {
            Palo palo = c.getPalo();
            Integer valore = c.getValore();
            punteggiPerPalo.put(palo, punteggiPerPalo.getOrDefault(palo, 0) + valore);
        }

        // Trova il punteggio massimo tra tutti i pali
        Integer punteggioMassimo = 0;
        for (Integer punteggio : punteggiPerPalo.values()) {
            if (punteggio > punteggioMassimo) {
                punteggioMassimo = punteggio;
            }
        }

        return punteggioMassimo;
    }

}
