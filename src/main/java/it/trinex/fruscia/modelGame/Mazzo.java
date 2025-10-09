package it.trinex.fruscia.modelGame;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Mazzo {

    private List<Carta> carte;

    public Mazzo() {
        inizializzaMazzo();
    }

    private void inizializzaMazzo() {
        carte = new ArrayList<>();
        for(Palo palo : Palo.values()) {
            for(int i = 1; i <= 10; i++) {
                Carta carta = new Carta(palo, i);
                carte.add(carta);
            }
        }
        Collections.shuffle(carte);
    }

    public Carta pescaCarta() throws IndexOutOfBoundsException {
        return carte.removeFirst();
    }


}
