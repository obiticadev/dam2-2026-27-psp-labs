package Clases;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import Enum.Sabor;

public class Helado {
    private static ReentrantLock lockHelados = new ReentrantLock();
    private static int totalHelados = 0;
    private int numHelado;
    private int saborPosition;
    private Sabor sabor;
    private double precio;
    private Random random = new Random();

    public Helado() {
        this.numHelado = sumarHelado();
        this.saborPosition = random.nextInt(Sabor.values().length);
        this.sabor = Sabor.values()[saborPosition];
        this.precio = sabor.getPrecio();
    }

    public Sabor getSabor() {
        return sabor;
    }

    public double getPrecio() {
        return precio;
    }

    public int getNumHelado() {
        return numHelado;
    }

    private int sumarHelado() {
        lockHelados.lock();
        try {
            return ++totalHelados;
        } finally {
            lockHelados.unlock();
        }
    }

}
