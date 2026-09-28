package Enum;

public enum Sabor {
    NARANJA(0.45),
    LIMÓN(0.50),
    ALMENDRA(1.00),
    NATA(1.10),
    CHOCOLATE(1.30);

    private final double precio;

    Sabor(double precio) {
        this.precio = precio;
    }

    public double getPrecio() {
        return this.precio;
    }
}
