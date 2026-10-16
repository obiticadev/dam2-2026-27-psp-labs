package Clases.Animales;

import java.time.Duration;

import Clases.Granjero;
import Clases.Huevera;
import Clases.Huevo;
import Interfaces.Extractor;

public class Urraca implements Extractor, Runnable {
    private final Duration ESPERA_ENTRE_HUEVOS = Duration.ofSeconds(1);
    private boolean huevoRetirado = false;

    public Urraca() {
    }

    @Override
    public Huevo extraer() throws InterruptedException {
        Huevo huevo = Huevera.retirarHuevo();
        this.huevoRetirado = true;
        return huevo;
    }

    private String eco(Huevo huevo) throws InterruptedException {
        return String.format(
                "Me he comido un huevo de %s con %d gramos, ME VOY",
                huevo.getNombrePadre(), huevo.getPeso());
    }

    @Override
    public Duration getEspera() {
        return this.ESPERA_ENTRE_HUEVOS;
    }

    @Override
    public boolean condicionDeFinalizacion() {
        boolean salida;
        if (Granjero.isDespierto() || huevoRetirado) {
            salida = true;
        } else {
            salida = false;
        }
        return salida;
    }

    @Override
    public void run() {
        try {
            while (!condicionDeFinalizacion()) {
                System.out.println(eco(extraer()));
                Thread.sleep(getEspera());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
