package it.aborti.fruscia.security;


import it.aborti.fruscia.exceptions.FrusciaException;
import it.aborti.fruscia.modelDB.Utente;
import it.aborti.fruscia.repository.UtenteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    public CustomUserDetailsService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws FrusciaException {
        Utente utente = utenteRepository.findByEmail(username)
                .orElseThrow(() -> new FrusciaException("Utente non trovato", HttpStatus.NOT_FOUND));
        return new CustomUserDetails(utente);
    }
}
