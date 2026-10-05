import Clases.Heladero;
import Clases.Niño;

public class App {

    public static void main(String[] args) throws Exception {
        final int NUM_NIÑOS = 10;

        for (int i = 0; i < Heladero.numHeladeros; i++) {
            Heladero heladero = new Heladero();
            Thread hilo = new Thread(heladero);
            hilo.start();
        }
        for (int i = 0; i < NUM_NIÑOS; i++) {
            Niño niño = new Niño();
            Thread hilo = new Thread(niño);
            hilo.start();
        }
    }
}
