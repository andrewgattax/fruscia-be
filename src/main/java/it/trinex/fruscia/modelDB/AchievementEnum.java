package it.trinex.fruscia.modelDB;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AchievementEnum {
    PARTITE_GIOCATE("Partite giocate"),
    FRUSCE("Frusce"),
    COLORI("Colori"),
    POKER("Poker"),
    PARTITE_VINTE("Partite vinte"),
    PARTITE_PERSE("Partite perse"),
    PUNTEGGI("Punteggi"),
    INSULTI_INVIATI("Insulti inviati"),
    INSULTI_RICEVUTI("Insulti ricevuti");


    private final String nome;
}
