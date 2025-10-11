package it.aborti.fruscia.exceptions;

import org.springframework.http.HttpStatus;

public class FrusciaException extends RuntimeException {

    HttpStatus status;

    public FrusciaException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FrusciaException(String message) {
        super(message);
    }
}
