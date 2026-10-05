package Clases;

import java.time.Duration;

public class Niño implements Runnable {

    Helado helado;

    public Niño() {
    }

    private String eco() {
        return String.format("He comprado el helado nº %d de sabor %s a %.2f€, y está de muerte!",
                helado.getNumHelado(), helado.getSabor(),
                helado.getPrecio());
    }

    private void bucleHelados() {
        boolean continuar = true;
        while (continuar) {
            this.helado = Nevera.sacarHelado();

            if (this.helado != null) {
                // CASO 1: Ha conseguido helado -> se lo come
                System.out.println(eco());
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (Heladero.getNumHeladeros() == 0) {
                // CASO 2: La nevera está vacía Y ya no hay heladeros trabajando -> se marcha
                System.out.println("Me voy, ya no habrá más helados...");
                continuar = false;
            } else {
                // CASO 3: La nevera está vacía pero AÚN hay heladeros fabricando -> espera a
                // que repongan
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        bucleHelados();
    }

}
