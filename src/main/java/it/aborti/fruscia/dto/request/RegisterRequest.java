package it.aborti.fruscia.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private LocalDate dataNascita;
}
