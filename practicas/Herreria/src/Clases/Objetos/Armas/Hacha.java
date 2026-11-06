package Clases.Objetos.Armas;

import ClasesAbstractas.Arma;

public class Hacha extends Arma {

    public Hacha(String nombre, String valor) {
        super(nombre, valor);
    }

    @Override
    public String toString() {
        return "Hacha [valor=" + valor + "]";
    }

}
