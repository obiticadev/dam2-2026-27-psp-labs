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
public class Buitre extends Thread {
    int cantidad = 0;
    Granjero granjero;
    Huevera huevera;
    String cadena = "";

    public Buitre(Granjero granjero, Huevera huevera) {
        this.granjero = granjero;
        this.huevera = huevera;
    }

    @Override
    public void run() {

        granjero.lock.lock();
        while (granjero.esDormido && cantidad < 200) {

            granjero.lock.unlock();

            Huevo h = huevera.sacarHuevo();
            try {
                cantidad += h.getPeso();
                cadena += "-" + h.getNombreGallina();
                System.out
                        .println("Soy Buitre y me he comido este huevo " + h.toString() + "Llevo comidos " + cantidad);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, ex);
            }
            granjero.lock.lock();

        }
        granjero.lock.unlock();

        System.out.println(
                "Soy buitre y me largo me comi los huevos de: " + cadena + "con un peso total comido " + cantidad);

    }

}
