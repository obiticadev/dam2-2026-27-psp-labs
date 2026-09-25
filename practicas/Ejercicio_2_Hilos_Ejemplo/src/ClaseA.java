public class ClaseA implements Runnable {

    static int variable = 0;

    public ClaseA() {
    }

    @Override
    public void run() {
        variable++;
        System.out.println("Soy la clase A : " + variable);
    }

}
