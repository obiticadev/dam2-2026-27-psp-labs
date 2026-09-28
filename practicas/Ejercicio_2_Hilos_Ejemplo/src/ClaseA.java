import java.util.concurrent.locks.ReentrantLock;

public class ClaseA implements Runnable {

    // ReentrantLock debe ser static si la variable ligada también lo es. Se crea
    // junto a la variable ligada por las leyes de POO
    static int variable = 0;
    static ReentrantLock lock = new ReentrantLock(); // Se llama en todos los sitios donde se use la variable ligada

    public ClaseA() {
    }

    @Override
    public void run() {
        lock.lock();
        variable++;
        System.out.println("Soy la clase A : " + variable);
        lock.unlock();
    }

}
