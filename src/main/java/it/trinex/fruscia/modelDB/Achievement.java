package it.trinex.fruscia.modelDB;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AchievementEnum nome;

    @Enumerated(EnumType.STRING)
    private ValoreAchievement valore;

    @ManyToOne
    @JoinColumn(name = "achievements")
    private Utente utente;
}
