package ClasesAbstractas;

public abstract class Arma {
    protected String nombre;
    protected final int VALOR;

    public Arma(String nombre, int VALOR) {
        this.nombre = nombre;
        this.VALOR = VALOR;
    }

}
