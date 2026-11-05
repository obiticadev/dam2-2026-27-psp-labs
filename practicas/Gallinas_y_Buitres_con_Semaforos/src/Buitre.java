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

        try {
            granjero.s.acquire(1);
        } catch (InterruptedException e) {
            Logger.getLogger(Granjero.class.getName()).log(Level.SEVERE, null, e);
        }
        while (granjero.esDormido && cantidad < 200) {

            granjero.s.release(1);

            Huevo h = huevera.sacarHuevo();
            if (h != null) {
                try {
                    cantidad += h.getPeso();
                    cadena += "-" + h.getNombreGallina();
                    System.out.println(
                            "Soy buitre y me largo me comi los huevos de: " + cadena + "con un peso total comido "
                                    + cantidad);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                granjero.s.acquire(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Logger.getLogger(Granjero.class.getName()).log(Level.SEVERE, null, e);
            }

        }
        granjero.s.release(1);

    }

}
