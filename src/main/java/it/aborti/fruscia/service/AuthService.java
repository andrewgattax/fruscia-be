package it.aborti.fruscia.service;

import it.aborti.fruscia.dto.request.LoginRequest;
import it.aborti.fruscia.dto.request.RegisterRequest;
import it.aborti.fruscia.dto.response.AuthResponse;
import it.aborti.fruscia.modelDB.Utente;
import it.aborti.fruscia.repository.UtenteRepository;
import it.aborti.fruscia.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import it.aborti.fruscia.exceptions.FrusciaException;


@Service
public class AuthService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new FrusciaException("Email già registrata", HttpStatus.BAD_REQUEST);
        }
        Utente utente = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .dataNascita(request.getDataNascita())
                .build();
        utenteRepository.save(utente);
        String token = jwtService.generateToken(utente.getEmail(), java.util.Collections.emptyMap());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new FrusciaException("Utente non trovato", HttpStatus.NOT_FOUND));
        String token = jwtService.generateToken(utente.getEmail(), java.util.Collections.emptyMap());
        return new AuthResponse(token);
    }
}
