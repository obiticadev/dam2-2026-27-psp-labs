/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Huevera {

    final int CANTIDAD_MAX = 12;
    Stack<Huevo> huevera;
    Semaphore s;
    Granjero granjero;

    public Huevera(Stack<Huevo> huevera, Semaphore s, Granjero granjero) {
        this.huevera = huevera;
        this.s = s;
        this.granjero = granjero;
    }

    public void meterHuevo(Huevo h) {
        try {
            s.acquire(1);
            granjero.s.acquire(1);

            while (huevera.size() >= CANTIDAD_MAX && granjero.esDormido) {
                s.release(1);
                granjero.s.release(1);

                // Esperar
                Thread.sleep(100);

                s.acquire(1);
                granjero.s.acquire(1);
            }

            s.acquire(GallinasBuitre202526.MAX_PETICIONES - 1);

            if (granjero.esDormido) {
                granjero.s.release(1);
                huevera.push(h);
                System.out.println("He metido el huevo " + h.toString());
            }
            s.release(GallinasBuitre202526.MAX_PETICIONES);
        } catch (InterruptedException ex) {
            Logger.getLogger(Huevera.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Huevo sacarHuevo() {
        Huevo huevo = null;
        try {
            s.acquire(1);
            granjero.s.acquire(1);
            while (huevera.empty() && granjero.esDormido) {
                s.release(1);
                granjero.s.release(1);
                // Esperar
                Thread.sleep(100);
                s.acquire(1);
                granjero.s.acquire(1);
            }

            s.acquire(GallinasBuitre202526.MAX_PETICIONES - 1);

            if (granjero.esDormido) {
                huevo = huevera.pop();
                System.out.println("He sacado el huevo " + huevo.toString());
            }
            s.release(GallinasBuitre202526.MAX_PETICIONES);
            granjero.s.release(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(Huevera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return huevo;

    }

}
