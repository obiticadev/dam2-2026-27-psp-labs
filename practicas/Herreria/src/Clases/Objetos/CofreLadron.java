package Clases.Objetos;

import ClasesAbstractas.Arma;
import ClasesAbstractas.Cofre;

public class CofreLadron extends Cofre {

    public CofreLadron() {
    }

    public void agregarArma(Arma arma) {
        armas.push(arma);
    }
}
