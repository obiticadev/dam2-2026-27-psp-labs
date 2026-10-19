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
public class Gallina extends Thread {

    String nombre;
    Granjero granjero;
    Huevera huevera;

    public Gallina(String nombre, Granjero granjero, Huevera huevera) {
        this.nombre = nombre;
        this.granjero = granjero;
        this.huevera = huevera;
    }

    public Gallina(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {

        granjero.lock.lock();
        while (granjero.esDormido) {

            granjero.lock.unlock();
            int peso = (int) ((Math.random() * 50) + 25);
            Huevo h = new Huevo(peso, nombre);
            huevera.meterHuevo(h);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Gallina.class.getName()).log(Level.SEVERE, null, ex);
            }
            granjero.lock.lock();

        }
        granjero.lock.unlock();

    }

}
