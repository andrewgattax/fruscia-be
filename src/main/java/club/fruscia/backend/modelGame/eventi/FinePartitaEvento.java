package club.fruscia.backend.modelGame.eventi;

import club.fruscia.backend.dto.response.StatoPartita;

public class FinePartitaEvento extends AppEvento {

    StatoPartita statoPartita;

    public FinePartitaEvento (String idPartita, StatoPartita statoPartita) {
        super("FINE_PARTITA", idPartita);
        this.statoPartita = statoPartita;
    }
}
