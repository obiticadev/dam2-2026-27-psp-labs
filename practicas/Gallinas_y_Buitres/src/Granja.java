import java.util.ArrayList;
import java.util.List;

import Clases.Animales.Buitre;
import Clases.Animales.Gallina;
import Clases.Animales.Pularda;
import Clases.Animales.Urraca;

public class Granja {
    public static void main(String[] args) throws Exception {
        final int PULARDAS = 5;
        final int GALLINAS = 10;
        final int BUITRES = 2;
        final int URRACAS = 3;

        for (int i = 0; i < PULARDAS; i++) {
            Pularda pularda = new Pularda();
            Thread hilo = new Thread(pularda);
            hilo.start();
        }
    }

}
