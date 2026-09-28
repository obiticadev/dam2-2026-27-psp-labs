package Clases;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Nevera {
    private final static int MAX_CAPACIDAD = 25;
    public static ReentrantLock lock = new ReentrantLock();

    public static ArrayList<Helado> listaHelados = new ArrayList<>();

    public static void agregarHelado(Helado helado) {
        if (listaHelados.size() < MAX_CAPACIDAD) {
            listaHelados.add(helado);
        }
    }
}
