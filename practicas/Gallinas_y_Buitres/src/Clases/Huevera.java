package Clases;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Huevera {
    private final static int MAX_CAPACIDAD = 12;
    private static Deque<Huevo> pila = new ArrayDeque<>(MAX_CAPACIDAD);
    private static ReentrantLock lockHuevera = new ReentrantLock();
    private static Condition hayHueco = lockHuevera.newCondition();
    private static Condition hayHuevo = lockHuevera.newCondition();
    private static boolean cerrada = false;

    public Huevera() {
    }

    public static boolean agregarHuevo(Huevo huevo) throws InterruptedException {
        lockHuevera.lock();
        try {
            while (pila.size() == MAX_CAPACIDAD && !cerrada) {
                hayHueco.await();
            }
            if (cerrada) {
                return false;
            }
            pila.push(huevo);
            hayHuevo.signal();
            return true;
        } finally {
            lockHuevera.unlock();
        }
    }

    public static Huevo retirarHuevo() throws InterruptedException {
        Huevo huevo = null;
        lockHuevera.lock();
        try {
            while (pila.size() == 0 && !cerrada) {
                hayHuevo.await();
            }
            huevo = pila.pop();
            hayHueco.signal();
        } finally {
            lockHuevera.unlock();
        }
        return huevo;
    }

    public static void cerrar() {
        lockHuevera.lock();
        try {
            cerrada = true;
            hayHueco.signalAll();
            hayHuevo.signalAll();
        } finally {
            lockHuevera.unlock();
        }
    }

}
