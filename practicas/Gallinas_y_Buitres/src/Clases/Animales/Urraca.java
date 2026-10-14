package Clases.Animales;

import java.time.Duration;

import Clases.Huevo;
import Interfaces.Extractor;

public class Urraca implements Extractor {
    private final Duration ESPERA_ENTRE_HUEVOS = Duration.ofSeconds(1);

    @Override
    public Huevo extraer() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'extraer'");
    }

    @Override
    public Duration getEspera() {
        return this.ESPERA_ENTRE_HUEVOS;
    }

    @Override
    public boolean condicionDeFinalizacion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'condicionDeFinalizacion'");
    }

}
