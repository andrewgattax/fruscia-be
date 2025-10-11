package it.aborti.fruscia.service;

import it.aborti.fruscia.modelDB.Utente;
import it.aborti.fruscia.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserService {

    Utente getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUtente();
    }

}
