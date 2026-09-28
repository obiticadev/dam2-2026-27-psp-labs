package Clases;

import Enum.Sabor;

public class Helado {
    private Sabor sabor;
    private double precio;

    public Helado(Sabor sabor) {
        this.sabor = sabor;
        this.precio = sabor.getPrecio();
    }

    public Sabor getSabor() {
        return sabor;
    }

    public double getPrecio() {
        return precio;
    }

}
