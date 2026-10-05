package Clases;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Heladero implements Runnable {
    private static ReentrantLock lockHeladeros = new ReentrantLock();
    public static int numHeladeros = 5;
    private int numHelados; // TOTAL DE HELADOS QUE HARÁ
    private int conteoHelados = 0;
    private Helado helado;
    private Random random = new Random();

    public Heladero() {
        this.numHelados = random.nextInt(50) + 1;
    }

    private boolean crearHelado() {
        if (conteoHelados < numHelados) {
            conteoHelados++;
            this.helado = new Helado();
            return true;
        }
        return false;
    }

    private boolean intentarMeterHelado(Helado helado) {
        boolean continuar = false;
        while (!continuar) {
            if (!Nevera.agregarHelado(helado)) {
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                continuar = true;
            }
        }
        return continuar;
    }

    private String marcharse() {
        lockHeladeros.lock();
        try {
            this.numHeladeros--;
        } finally {
            lockHeladeros.unlock();
        }
        return String.format("El heladero ha finalizado con %d helados", this.numHelados);
    }

    private void bucleHelados() {
        while (crearHelado()) {
            intentarMeterHelado(helado);
        }
        System.out.println(marcharse());
    }

    @Override
    public void run() {
        bucleHelados();
    }

    public int getNumHelados() {
        return numHelados;
    }

    public static int getNumHeladeros() {
        lockHeladeros.lock();
        try {
            return numHeladeros;
        } finally {
            lockHeladeros.unlock();
        }
    }

}
