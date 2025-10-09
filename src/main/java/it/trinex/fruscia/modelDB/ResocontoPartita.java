package it.trinex.fruscia.modelDB;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
public class ResocontoPartita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "resocontiPartite")
    private List<Utente> partecipanti;

    @ElementCollection
    @CollectionTable(name = "risultati",
            joinColumns = @JoinColumn(name = "partita_id"))
    @MapKeyJoinColumn(name = "utente_id") // chiave della mappa
    @Column(name = "punteggio")
    private Map<Utente, Integer> risultati;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime endOfGame;

    @PrePersist
    public void prePersist()
    {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

}
