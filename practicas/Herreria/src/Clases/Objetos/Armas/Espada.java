package Clases.Objetos.Armas;

import ClasesAbstractas.Arma;

public class Espada extends Arma {

    public Espada() {
        super(Espada.class.getName(), 2);
    }

    @Override
    public String toString() {
        return "Espada [valor=" + VALOR + "]";
    }

}
