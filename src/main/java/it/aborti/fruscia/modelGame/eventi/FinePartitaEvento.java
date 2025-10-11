package it.aborti.fruscia.modelGame.eventi;

import it.aborti.fruscia.dto.response.StatoPartita;

public class FinePartitaEvento extends AppEvento {

    StatoPartita statoPartita;

    public FinePartitaEvento (String idPartita, StatoPartita statoPartita) {
        super("FINE_PARTITA", idPartita);
        this.statoPartita = statoPartita;
    }
}
