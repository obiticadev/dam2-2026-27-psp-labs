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

        granjero.lock.lock();

        while (granjero.esDormido && h == null) {
            granjero.lock.unlock();
            h = huevera.sacarHuevo();
            try {
                System.out.println("Soy Hurraca y me he comido este huevo " + h.toString());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, ex);
            }
            granjero.lock.lock();

        }
        granjero.lock.unlock();

    }

}
