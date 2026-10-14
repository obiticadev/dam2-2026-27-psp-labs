package Clases;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Huevera {
    private final static int MAX_CAPACIDAD = 12;
    private static Deque<Huevo> pila = new ArrayDeque<>(MAX_CAPACIDAD);
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition hayHueco = lock.newCondition();
    private static Condition hayHuevo = lock.newCondition();

    public Huevera() {

    }

    public static void agregarHuevo(Huevo huevo) throws InterruptedException {
        lock.lock();
        try {
            while (pila.size() == MAX_CAPACIDAD) {
                hayHueco.await();
            }
            pila.push(huevo);
            hayHuevo.signal();
        } finally {
            lock.unlock();
        }
    }

    public static Huevo retirarHuevo() throws InterruptedException {
        Huevo huevo = null;
        lock.lock();
        try {
            while (pila.size() == 0) {
                hayHuevo.await();
            }
            huevo = pila.pop();
            hayHueco.signal();
        } finally {
            lock.unlock();
        }
        return huevo;
    }

    private static int getSizePila() {
        lock.lock();
        try {
            return pila.size();
        } finally {
            lock.unlock();
        }
    }

}
