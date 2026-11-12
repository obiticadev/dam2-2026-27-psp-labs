package estudio.t07;
/** Programa auxiliar ya resuelto: el objetivo es lanzarlo como proceso. */
public class Eco {
    public static void main(String[] args) {
        if (args.length != 1) { System.err.println("Falta el nombre"); System.exit(2); }
        System.out.println("ECO:" + args[0]);
    }
}
