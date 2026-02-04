package club.fruscia.backend.modelGame.eventi;

import club.fruscia.backend.dto.response.StatoPartita;

public class InizioPartitaEvento extends AppEvento {

    StatoPartita statoPartita;

    public InizioPartitaEvento(String idPartita,  StatoPartita statoPartita) {
        super("INIZIO_PARTITA", idPartita);
        this.statoPartita = statoPartita;
    }
}
