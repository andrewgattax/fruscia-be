package club.fruscia.backend.modelGame.eventi;

import club.fruscia.backend.modelGame.Chiamata;
import club.fruscia.backend.modelGame.Partecipante;

public class PuntoEvento extends AppEvento {

    private Partecipante partecipante;

    private Chiamata chiamataVincente;

    public PuntoEvento(String idPartita, Partecipante partecipante, Chiamata chiamataVincente) {
        super("PUNTO", idPartita);
        this.partecipante = partecipante;
        this.chiamataVincente = chiamataVincente;
    }

}
