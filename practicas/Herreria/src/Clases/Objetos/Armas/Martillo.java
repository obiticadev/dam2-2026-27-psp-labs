package Clases.Objetos.Armas;

import ClasesAbstractas.Arma;

public class Martillo extends Arma {

    public Martillo(String nombre, String valor) {
        super(nombre, valor);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Martillo [valor=" + valor + "]";
    }

}
