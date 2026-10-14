package Interfaces;

import Clases.Huevo;

public interface Ponedor extends Finalizador {
    Huevo crearHuevo();

    void poner(Huevo huevo) throws InterruptedException;

    int getPesoMinimo();

    int getPesoMaximo();

}
