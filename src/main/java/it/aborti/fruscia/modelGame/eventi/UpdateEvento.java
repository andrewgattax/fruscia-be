package it.aborti.fruscia.modelGame.eventi;

import it.aborti.fruscia.dto.response.StatoPartita;

public class UpdateEvento extends AppEvento {

    private StatoPartita statoPartita;

    public UpdateEvento(String idPartita, StatoPartita statoPartita) {
        super("UPDATE", idPartita);
        this.statoPartita = statoPartita;
    }
}
