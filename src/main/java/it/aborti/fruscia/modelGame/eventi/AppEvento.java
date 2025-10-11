package it.aborti.fruscia.modelGame.eventi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AppEvento {
    private String nome;
    private String idPartita;
}
