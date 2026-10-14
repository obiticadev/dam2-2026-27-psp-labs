import Clases.Granjero;
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

        Granjero granjero = new Granjero();
        Thread hiloGranjero = new Thread(granjero);
        hiloGranjero.start();

        for (int i = 0; i < PULARDAS; i++) {
            Pularda pularda = new Pularda(granjero);
            Thread hilo = new Thread(pularda);
            hilo.start();
        }

        for (int i = 0; i < GALLINAS; i++) {
            Gallina gallina = new Gallina(granjero);
            Thread hilo = new Thread(gallina);
            hilo.start();
        }

        for (int i = 0; i < BUITRES; i++) {
            Buitre buitre = new Buitre(granjero);
            Thread hilo = new Thread(buitre);
            hilo.start();
        }

        for (int i = 0; i < URRACAS; i++) {
            Urraca urraca = new Urraca(granjero);
            Thread hilo = new Thread(urraca);
            hilo.start();
        }
    }

}
