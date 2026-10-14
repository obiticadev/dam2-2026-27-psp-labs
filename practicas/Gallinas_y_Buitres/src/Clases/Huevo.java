package Clases;

import java.util.concurrent.ThreadLocalRandom;

public class Huevo {
    private int peso;
    private String nombrePadre;

    public Huevo(String nombrePadre, int pesoMin, int pesoMax) {
        this.nombrePadre = nombrePadre;
        this.peso = ThreadLocalRandom.current().nextInt(pesoMin, pesoMax + 1);
    }

    public int getPeso() {
        return peso;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

}
