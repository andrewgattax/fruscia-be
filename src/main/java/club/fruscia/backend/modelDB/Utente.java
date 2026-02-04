package club.fruscia.backend.modelDB;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authAccountId;

    @Column(nullable = false)
    private LocalDate dataNascita;

    @OneToOne(mappedBy = "utente")
    private StatisticaUtente statisticaUtente;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Achievement> achievements;

    private String urlFotoProfilo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "utente_resoconto_partita",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "resoconto_partita_id")
    )
    private List<ResocontoPartita> resocontiPartite;


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
