package Clases.Objetos.Armas;

import ClasesAbstractas.Arma;

public class Martillo extends Arma {

    public Martillo() {
        super(Martillo.class.getName(), 7);
    }

    @Override
    public String toString() {
        return "Martillo [valor=" + VALOR + "]";
    }

}
