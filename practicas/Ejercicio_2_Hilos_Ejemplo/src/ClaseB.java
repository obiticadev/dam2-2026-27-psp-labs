public class ClaseB extends Thread {

    public ClaseB() {
    }

    @Override
    public void run() {
        ClaseA.lock.lock();
        ClaseA.variable--;
        System.out.println("Soy la clase B : " + ClaseA.variable);
        ClaseA.lock.unlock();
    }

}
