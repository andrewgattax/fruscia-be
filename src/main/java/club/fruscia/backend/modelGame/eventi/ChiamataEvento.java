package club.fruscia.backend.modelGame.eventi;

import club.fruscia.backend.modelGame.Chiamata;

public class ChiamataEvento extends AppEvento {

    Chiamata chiamata;

    public ChiamataEvento(String idPartita, Chiamata chiamata) {
        super("CHIAMATA", idPartita);
        this.chiamata = chiamata;
    }

}
