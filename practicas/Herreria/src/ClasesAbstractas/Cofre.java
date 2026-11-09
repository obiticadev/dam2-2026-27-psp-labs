package ClasesAbstractas;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Cofre {
    protected final Deque<Arma> armas;

    public Cofre() {
        this.armas = new ArrayDeque<>();
    }

}
