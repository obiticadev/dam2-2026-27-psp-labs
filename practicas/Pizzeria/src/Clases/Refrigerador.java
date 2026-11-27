package Clases;

import java.util.ArrayDeque;
import java.util.Deque;

import Clases.Enum.Ingrediente;

public class Refrigerador {
    private final int MAX_CAPACIDAD = 500;
    private Deque<Ingrediente> contenidoDelRefrigerador;

    public Refrigerador() {
        this.contenidoDelRefrigerador = new ArrayDeque<>();
    }

    public boolean guardarIngrediente(Ingrediente ingrediente) {
        if (contenidoDelRefrigerador.size() < MAX_CAPACIDAD) {
            contenidoDelRefrigerador.push(ingrediente);
            return true;
        }
        return false;
    }

    public Deque<Ingrediente> getContenidoDelRefrigerador() {
        return contenidoDelRefrigerador;
    }

}
