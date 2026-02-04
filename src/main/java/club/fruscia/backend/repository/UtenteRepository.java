package club.fruscia.backend.repository;

import club.fruscia.backend.modelDB.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    public Optional<Utente> findByAuthAccountId(Long id);

}
