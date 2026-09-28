import java.util.ArrayList;

import Clases.Heladero;
import Clases.Nevera;
import Clases.Niño;

public class App {
    public static void main(String[] args) throws Exception {
        Nevera nevera = new Nevera();
        final int NUM_HELADEROS = 5;
        final int NUM_NIÑOS = 10;
        ArrayList<Heladero> listaHeladeros = new ArrayList<>(NUM_HELADEROS);
        ArrayList<Niño> listaNiños = new ArrayList<>(NUM_NIÑOS);

        for (Heladero heladero : listaHeladeros) {
            heladero = new Heladero();
            listaHeladeros.add(heladero);
        }
        for (Niño niño : listaNiños) {
            niño = new Niño();
            listaNiños.add(niño);
        }
    }
}
