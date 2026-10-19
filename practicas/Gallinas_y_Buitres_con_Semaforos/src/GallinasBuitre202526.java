/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.util.Stack;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Usuario
 */
public class GallinasBuitre202526 {
    static final int MAX_PETICIONES = 20;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Granjero granjero = new Granjero();
        Stack<Huevo> hueveraPila = new Stack<Huevo>();
        Semaphore s = new Semaphore(MAX_PETICIONES);
        Huevera huevera = new Huevera(hueveraPila, s, granjero);
        // Inicio Gallina
        for (int i = 0; i < 10; i++) {

            Gallina g = new Gallina(i + "", granjero, huevera);
            Thread t = new Thread(g);
            t.start();
        }
        // Inicio Pularda
        for (int i = 0; i < 5; i++) {

            Pulardas g = new Pulardas(i + "", granjero, huevera);
            Thread t = new Thread(g);
            t.start();
        }
        // Inicio Buitre
        for (int i = 0; i < 2; i++) {

            Buitre g = new Buitre(granjero, huevera);
            Thread t = new Thread(g);
            t.start();
        }
        // Inicio Hurraca
        for (int i = 0; i < 3; i++) {

            Hurraca g = new Hurraca(granjero, huevera);
            Thread t = new Thread(g);
            t.start();
        }

        // Inicio granjero
        Thread h = new Thread(granjero);
        h.start();

    }

}
