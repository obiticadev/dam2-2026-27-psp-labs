package Clases;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Repartidor {
    private final int MAX_CAPACIDAD = 500;
    private int ingredientesEnCamion;
    private Ingrediente[] variedadDeIngrediente;
    private Deque<Ingrediente> listaDelCamion = new ArrayDeque<>(500);

    public Repartidor() {
        this.ingredientesEnCamion = 0;
        variedadDeIngrediente = Ingrediente.values();
    }

    public void cargarRefrigerador() {
        Random random = new Random();
        Ingrediente ingrediente;
        for (int i = 0; i < MAX_CAPACIDAD; i++) {
            ingrediente = random.nextInt(variedadDeIngrediente.length);
        }
    }

}
