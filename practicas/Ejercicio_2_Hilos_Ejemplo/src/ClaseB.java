public class ClaseB extends Thread {

    public ClaseB() {
    }

    @Override
    public void run() {
        ClaseA.variable--;
        System.out.println("Soy la clase B : " + ClaseA.variable);
    }

}
