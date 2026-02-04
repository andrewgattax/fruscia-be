package club.fruscia.backend.modelGame;

import lombok.Data;

@Data
public class Carta {

    public Carta(Palo palo, Integer numero) {
        this.palo = palo;
        this.numero = numero;

    }

    private Palo palo;

    private Integer numero;

    private String id;

    private String generaID() {
        return numero.toString() + "_" + palo.toString().toUpperCase();
    }

    public Integer getValore() {
       if (numero == 1) {
           return 16;
       } else if (numero > 1 && numero < 6) {
           return numero + 10;
       } else if (numero > 5 && numero < 8) {
           return numero * 3;
       } else {
           return 10;
       }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return id.equals(carta.id);
    }

}
