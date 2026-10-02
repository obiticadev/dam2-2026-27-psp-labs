package Clases;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Nevera {
    private final static int MAX_CAPACIDAD = 25;
    public static ReentrantLock lock = new ReentrantLock();

    public static ArrayList<Helado> listaHelados = new ArrayList<>();

    public static boolean agregarHelado(Helado helado) {
        boolean esMetido = false;
        lock.lock();
        if (listaHelados.size() < MAX_CAPACIDAD) {
            listaHelados.add(helado);
            esMetido = true;
        }
        lock.unlock();
        return esMetido;

    }

    public static Helado sacarHelado() {
        Helado helado = null;
        lock.lock();
        if (listaHelados.size() > 0) {
            helado = listaHelados.removeFirst();
        }
        lock.unlock();
        return helado;
    }
}
