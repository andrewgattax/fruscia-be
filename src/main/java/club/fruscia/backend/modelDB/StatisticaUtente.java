package club.fruscia.backend.modelDB;

import jakarta.persistence.*;

@Entity
@Table(name = "statistica-utente")
public class StatisticaUtente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "utente_id", referencedColumnName = "id")
    private Utente utente;

    private Integer frusce;
    private Integer colori;
    private Integer pokers;
    private Integer punteggi;
    private Integer insultiInviati;
    private Integer insultiRicevuti;
    private Integer partiteVinte;
    private Integer partiteGiocate;


}
