package it.aborti.fruscia.modelGame.eventi;

import it.aborti.fruscia.modelGame.Chiamata;

public class ChiamataEvento extends AppEvento {

    Chiamata chiamata;

    public ChiamataEvento(String idPartita, Chiamata chiamata) {
        super("CHIAMATA", idPartita);
        this.chiamata = chiamata;
    }

}
