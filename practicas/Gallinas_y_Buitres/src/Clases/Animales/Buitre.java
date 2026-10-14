package Clases.Animales;

import java.time.Duration;

import Clases.Granjero;
import Clases.Huevo;
import Interfaces.Extractor;

public class Buitre implements Extractor, Runnable {

    private final Granjero granjero;

    public Buitre(Granjero granjero) {
        this.granjero = granjero;
    }

    @Override
    public Huevo extraer() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'extraer'");
    }

    @Override
    public Duration getEspera() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEspera'");
    }

    @Override
    public boolean condicionDeFinalizacion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'condicionDeFinalizacion'");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
