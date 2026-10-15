package Clases;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

public class Granjero implements Runnable {
    private final static ReentrantLock granjeroLock = new ReentrantLock();
    private boolean despierto;
    private final Duration duracion = Duration.ofSeconds(7);

    public Granjero() {
        this.despierto = false;
    }

    private void despertar() {
        try {
            Thread.sleep(duracion);
            granjeroLock.lock();
            try {
                this.despierto = true;
            } finally {
                granjeroLock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isDespierto() {
        granjeroLock.lock();
        try {
            return this.despierto;
        } finally {
            granjeroLock.unlock();
        }
    }

    @Override
    public void run() {
        despertar();
        System.out.println("¡SALID DE MI GRANJA ANIMALES!");
        Huevera.cerrar();
    }

}
