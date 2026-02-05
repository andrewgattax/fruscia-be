package club.fruscia.backend.service;

import club.fruscia.backend.dto.request.RegistraRequest;
import club.fruscia.backend.modelDB.Utente;
import club.fruscia.backend.repository.UtenteRepository;
import it.trinex.blackout.model.AuthAccount;
import it.trinex.blackout.repository.AuthAccountRepo;
import it.trinex.blackout.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UtenteRepository utenteRepository;
    private final AuthAccountRepo authAccountRepo;

    public void registraUtente(RegistraRequest request) {
        AuthAccount authAccount = null;

        try {
            // 1. Create AuthAccount in auth database
            authAccount = authService.registerAuthAccount(
                    AuthAccount.builder()
                            .firstName(request.getNome())
                            .lastName(request.getCognome())
                            .username(request.getUsername())
                            .passwordHash(passwordEncoder.encode(request.getPassword()))
                            .isActive(true)
                            .build()
            );

            // 2. Create business entity in primary database
            Utente user = Utente.builder()
                    .dataNascita(request.getDataNascita())
                    .authAccountId(authAccount.getId()) // ← CRITICAL: Link to AuthAccount
                    .build();

            utenteRepository.save(user);

        } catch (Exception e) {
            // 3. Manual rollback: delete AuthAccount if User creation fails
            if (authAccount != null && authAccount.getId() != null) {
                authAccountRepo.deleteById(authAccount.getId());
            }
            throw e; // Re-throw to let caller handle the error
        }
    }

}
