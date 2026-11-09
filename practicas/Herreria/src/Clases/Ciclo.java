package Clases;

import java.time.Duration;
import java.util.concurrent.Semaphore;

public class Ciclo implements Runnable {
    private final static int MAX_PETICIONES_CICLO = 5;
    private final Duration NOCHE_EN_SEG = Duration.ofSeconds(7);
    private final Duration DIA_EN_SEG = Duration.ofSeconds(5);
    private final int MAX_DIAS = 3;
    public static Semaphore ciclo = new Semaphore(MAX_PETICIONES_CICLO);
    private static boolean isDia;
    private int dia;

    public Ciclo() {
        this.isDia = true;
        this.dia = 1;
    }

    public static boolean isDia() {
        Boolean tmpBool = null;
        try {
            Ciclo.ciclo.acquire();
            tmpBool = isDia;
            Ciclo.ciclo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tmpBool;
    }

    @Override
    public void run() {
        try {
            for (dia = 1; dia <= MAX_DIAS; dia++) {
                System.out.printf("Comienza el día: %d", dia);

                Thread.sleep(DIA_EN_SEG);

                ciclo.acquire(MAX_PETICIONES_CICLO);
                isDia = false;
                ciclo.release(MAX_PETICIONES_CICLO);

                Thread.sleep(NOCHE_EN_SEG);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
