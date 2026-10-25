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

    public Pularda() {
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
        return Granjero.isDespierto();
    }

    @Override
    public void run() {
        try {
            Granjero.lockGranjero.lock();
            while (!condicionDeFinalizacion() && Huevera.agregarHuevo(crearHuevo())) {
                Granjero.lockGranjero.unlock();
                Thread.sleep(getEspera());
                Granjero.lockGranjero.lock();
            }
            Granjero.lockGranjero.unlock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
