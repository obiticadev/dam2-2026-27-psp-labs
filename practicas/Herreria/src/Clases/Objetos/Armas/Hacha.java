package Clases.Objetos.Armas;

import ClasesAbstractas.Arma;

public class Hacha extends Arma {

    public Hacha() {
        super(Hacha.class.getName(), 5);
    }

    @Override
    public String toString() {
        return "Hacha [valor=" + VALOR + "]";
    }

}
