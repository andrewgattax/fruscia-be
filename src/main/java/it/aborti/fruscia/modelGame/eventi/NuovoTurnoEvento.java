package it.aborti.fruscia.modelGame.eventi;

import it.aborti.fruscia.dto.response.StatoPartita;

public class NuovoTurnoEvento extends AppEvento {

    private StatoPartita statoPartita;

    public NuovoTurnoEvento(String idPartita, StatoPartita statoPartita) {
        super("NUOVO_TURNO", idPartita);
        this.statoPartita = statoPartita;
    }

}
