package Clases.Objetos;

import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;
import ClasesAbstractas.Arma;
import ClasesAbstractas.Cofre;

public class CofreHerrero extends Cofre {
    private final int CAPACIDAD_ARMAS = 20;
    private final int MAX_PERMISOS = 4;

    private Semaphore cofre = new Semaphore(MAX_PERMISOS);

    public CofreHerrero() {
    }

    public Boolean agregarArma(Arma arma) {
        Boolean completado = false;
        try {
            cofre.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (armas.size() < CAPACIDAD_ARMAS) {
            try {
                cofre.acquire(MAX_PERMISOS - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            armas.push(arma);
            cofre.release(MAX_PERMISOS);
            completado = true;
        }
        return completado;
    }

    public Arma retirarArma() {
        Arma armaRetirada = null;
        try {
            cofre.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (armas.size() > 0) {
            cofre.release();
            try {
                cofre.acquire(MAX_PERMISOS - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            armaRetirada = armas.pop();
            cofre.release(MAX_PERMISOS - 1);
        }
        return armaRetirada;
    }

    public ArrayDeque<Arma> retirarTodasLasArmas() {
        ArrayDeque<Arma> listArmas = new ArrayDeque<>();
        try {
            cofre.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (armas.size() > 0) {
            try {
                cofre.acquire(MAX_PERMISOS - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listArmas.push(armas.pop());
            cofre.release(MAX_PERMISOS - 1);
        }
        return listArmas;

    }
}
