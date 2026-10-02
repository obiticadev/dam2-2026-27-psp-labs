package Clases;

import java.time.Duration;

public class Niño implements Runnable {

    Helado helado;

    public Niño() {
    }

    private boolean intentarCogerHelado() {
        boolean continuar = false;
        while (!continuar) {
            this.helado = Nevera.sacarHelado();
            if (helado == null) {
                try {
                    Thread.sleep(Duration.ofSeconds(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                continuar = true;
            }
        }
        return continuar;
    }

    private String eco() {
        return String.format("He comprado el helado nº %d de sabor %s a %.2f€, y está de muerte!",
                helado.getNumHelado(), helado.getSabor(),
                helado.getPrecio());
    }

    private void bucleHelados() {
        boolean continuar = true;
        while (continuar) {
            if (intentarCogerHelado()) {
                System.out.println(eco());
            }
        }
    }

    @Override
    public void run() {
        bucleHelados();
    }

}
