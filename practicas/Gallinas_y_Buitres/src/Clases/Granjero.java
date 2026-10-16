package Clases;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

public class Granjero implements Runnable {
    public final static ReentrantLock lockGranjero = new ReentrantLock();
    private static boolean despierto = false;
    private final Duration duracion = Duration.ofSeconds(7);

    public Granjero() {
    }

    private void despertar() {
        try {
            Thread.sleep(duracion);
            lockGranjero.lock();
            try {
                Granjero.despierto = true;
            } finally {
                lockGranjero.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isDespierto() {
        lockGranjero.lock();
        try {
            return despierto;
        } finally {
            lockGranjero.unlock();
        }
    }

    @Override
    public void run() {
        despertar();
        System.out.println("¡SALID DE MI GRANJA ANIMALES!");
        Huevera.cerrar();
    }

}
