package it.aborti.fruscia.modelGame.eventi;

import it.aborti.fruscia.modelGame.Chiamata;
import it.aborti.fruscia.modelGame.Partecipante;

public class PuntoEvento extends AppEvento {

    private Partecipante partecipante;

    private Chiamata chiamataVincente;

    public PuntoEvento(String idPartita, Partecipante partecipante, Chiamata chiamataVincente) {
        super("PUNTO", idPartita);
        this.partecipante = partecipante;
        this.chiamataVincente = chiamataVincente;
    }

}
