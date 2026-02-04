package club.fruscia.backend.modelGame.eventi;

import club.fruscia.backend.dto.response.StatoPartita;

public class UpdateEvento extends AppEvento {

    private StatoPartita statoPartita;

    public UpdateEvento(String idPartita, StatoPartita statoPartita) {
        super("UPDATE", idPartita);
        this.statoPartita = statoPartita;
    }
}
