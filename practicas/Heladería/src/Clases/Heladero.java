package Clases;

import java.util.Random;
import Enum.Sabor;

public class Heladero implements Runnable {
    private static int instancias = 0;
    private int heladero;
    private int numHelados;
    private int conteoHelados = 0;

    public Heladero() {
        this.heladero = ++instancias;
        Random random = new Random();
        numHelados = random.nextInt(50) + 1;
    }

    public int getNumHelados() {
        return numHelados;
    }

    public void crearHelado() {
        if (conteoHelados < numHelados) {
            Random random = new Random();
            int saborPosition = random.nextInt(Sabor.values().length);
            Helado helado = new Helado(Sabor.values()[saborPosition]);
            Nevera.agregarHelado(helado);
            conteoHelados++;
        } else {

        }
    }

    public String marcharse() {
        return String.format("El heladero %d ha finalizado con %d helados", this.heladero, this.numHelados);
    }

    @Override
    public void run() {
        Nevera.lock.lock();
        crearHelado();
        Nevera.lock.unlock();
    }

}
