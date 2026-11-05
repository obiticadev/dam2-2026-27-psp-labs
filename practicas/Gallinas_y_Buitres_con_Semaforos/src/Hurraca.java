/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Hurraca extends Thread {

    Granjero granjero;
    Huevera huevera;

    public Hurraca(Granjero granjero, Huevera huevera) {
        this.granjero = granjero;
        this.huevera = huevera;
    }

    @Override
    public void run() {
        Huevo h = null;

        try {
            granjero.s.acquire(1);
        } catch (InterruptedException e) {
            Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, e);
        }

        while (granjero.esDormido && h == null) {
            granjero.s.release(1);
            h = huevera.sacarHuevo();
            if (h != null) {
                try {
                    System.out.println("Soy Hurraca y me he comido este huevo " + h.toString());
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                granjero.s.acquire(1);
            } catch (InterruptedException e) {
                Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, e);
            }

        }
        granjero.s.release(1);

    }

}
