package it.trinex.fruscia.exceptions;

public class PartecipanteNotFoundException extends RuntimeException {
    public PartecipanteNotFoundException(String message) {
        super(message);
    }
}
