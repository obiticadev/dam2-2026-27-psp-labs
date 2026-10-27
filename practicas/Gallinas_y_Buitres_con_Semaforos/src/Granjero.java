/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Granjero extends Thread {

    boolean esDormido = true;
    Semaphore s;
    final int MAX_PETICIONES_GRANJERO = 6;

    public Granjero() {
        this.s = new Semaphore(MAX_PETICIONES_GRANJERO);
    }

    @Override
    public void run() {

        try {
            Thread.sleep(7000);
            s.acquire(6);
            System.err.println("SOY EL GRANJERO TODO EL MUNDO FUERA");
            esDormido = false;
            s.release(6);
        } catch (InterruptedException ex) {
            Logger.getLogger(Granjero.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getMAX_PETICIONES_GRANJERO() {
        return MAX_PETICIONES_GRANJERO;
    }

}
