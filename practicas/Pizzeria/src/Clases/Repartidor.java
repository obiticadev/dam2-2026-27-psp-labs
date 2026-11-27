package Clases;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import Clases.Enum.Ingrediente;

public class Repartidor {
    private final int MAX_CAPACIDAD = 500;
    private Ingrediente[] variedadDeIngrediente;
    private Deque<Ingrediente> listaDelCamion = new ArrayDeque<>(500);

    public Repartidor() {
        variedadDeIngrediente = Ingrediente.values();
    }

    public void cargarCamion() {
        Random random = new Random();
        Ingrediente ingrediente;
        for (int i = 0; i < MAX_CAPACIDAD; i++) {
            ingrediente = variedadDeIngrediente[random.nextInt(variedadDeIngrediente.length)];
            listaDelCamion.push(ingrediente);
        }
    }

    public void repartirEn(Refrigerador refrigerador) {
        Ingrediente ingrediente;
        while ((ingrediente = listaDelCamion.peekFirst()) != null) {
            refrigerador.guardarIngrediente(ingrediente);
            listaDelCamion.removeFirst();
        }
    }

    public Deque<Ingrediente> getListaDelCamion() {
        return listaDelCamion;
    }

}
