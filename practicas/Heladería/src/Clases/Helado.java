package Clases;

import java.util.Random;

import Enum.Sabor;

public class Helado {
    private static int totalHelados = 0;
    private int numHelado;
    private int saborPosition;
    private Sabor sabor;
    private double precio;
    private Random random = new Random();

    public Helado() {
        this.numHelado = ++totalHelados;
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

}
