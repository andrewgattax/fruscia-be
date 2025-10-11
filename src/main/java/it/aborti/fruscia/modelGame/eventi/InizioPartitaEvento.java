package it.aborti.fruscia.modelGame.eventi;

import it.aborti.fruscia.dto.response.StatoPartita;

public class InizioPartitaEvento extends AppEvento {

    StatoPartita statoPartita;

    public InizioPartitaEvento(String idPartita,  StatoPartita statoPartita) {
        super("INIZIO_PARTITA", idPartita);
        this.statoPartita = statoPartita;
    }
}
