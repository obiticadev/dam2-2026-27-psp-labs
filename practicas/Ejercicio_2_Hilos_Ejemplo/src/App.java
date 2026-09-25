// import java.util.concurrent.locks.ReentrantLock;

public class App {
    public static void main(String[] args) throws Exception {

        // ReentrantLock rl = new ReentrantLock();
        System.out.println("Hola mundo");
        for (int i = 0; i < 2; i++) {
            ClaseA claseA = new ClaseA();
            Thread t = new Thread(claseA);
            t.start();

            ClaseB claseB = new ClaseB();
            Thread t2 = new Thread(claseB);
            t2.start();
        }
    }
}
