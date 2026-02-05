package club.fruscia.backend.security;

import club.fruscia.backend.modelDB.Utente;
import club.fruscia.backend.repository.UtenteRepository;
import it.trinex.blackout.model.AuthAccount;
import it.trinex.blackout.repository.AuthAccountRepo;
import it.trinex.blackout.security.BlackoutUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FrusciaUserDetailService implements UserDetailsService {
    private final AuthAccountRepo authAccountRepo;
    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthAccount authAccount = authAccountRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Utente utente = utenteRepository.findByAuthAccountId(authAccount.getId())
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return BlackoutUserPrincipal.builder()
                // Default Blackout fields
                .id(authAccount.getId())
                .firstName(authAccount.getFirstName())
                .lastName(authAccount.getLastName())
                .userId(utente.getId())
                .authorities(authorities)
                .username(username)
                .password(authAccount.getPasswordHash())
                .build();
    }
}
