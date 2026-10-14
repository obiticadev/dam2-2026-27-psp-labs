package Clases.Animales;

import java.time.Duration;

import Clases.Granjero;
import Clases.Huevera;
import Clases.Huevo;
import Interfaces.Ponedor;

public class Pularda implements Ponedor, Runnable {
    private final int PESO_MIN = 80;
    private final int PESO_MAX = 120;
    private final Duration ESPERA_ENTRE_HUEVOS = Duration.ofSeconds(2);
    private final Granjero granjero;

    public Pularda(Granjero granjero) {
        this.granjero = granjero;
    }

    @Override
    public Huevo crearHuevo() {
        return new Huevo(this.getClass().getSimpleName(), PESO_MIN, PESO_MAX);
    }

    @Override
    public void poner(Huevo huevo) throws InterruptedException {
        Huevera.agregarHuevo(huevo);
        Thread.sleep(getEspera());
    }

    @Override
    public int getPesoMinimo() {
        return this.PESO_MIN;
    }

    @Override
    public int getPesoMaximo() {
        return this.PESO_MAX;
    }

    @Override
    public Duration getEspera() {
        return this.ESPERA_ENTRE_HUEVOS;
    }

    @Override
    public boolean condicionDeFinalizacion() {
        return granjero.isDespierto();
    }

    @Override
    public void run() {
        boolean continuar = true;
        while (continuar) {
            try {
                poner(crearHuevo());
                if (condicionDeFinalizacion()) {
                    continuar = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
