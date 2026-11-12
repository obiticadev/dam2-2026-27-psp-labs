package estudio.t02;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Explorar directorios: Hijo. Referencia, no copiar sobre tu intento. */
public class Hijo {
    /** El examen es en Windows; la rama Linux permite ensayar el mismo patrón en casa. */
    static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");
    public static void main(String[] args) throws Exception {
        try {
        String carpeta = args.length > 0 ? args[0] : ".";
        ProcessBuilder pb = WINDOWS
                ? new ProcessBuilder("cmd", "/d", "/c", "dir", "/b")
                : new ProcessBuilder("ls", "-1"); // ls sí es un ejecutable
        // Así no hay que concatenar la ruta al texto que interpreta CMD.
        pb.directory(new File(carpeta));
        ejecutar(pb);
        } catch (EtapaFallida ex) {
            System.err.println(ex.getMessage()); System.exit(ex.codigo);
        } catch (IOException ex) {
            System.err.println("No se pudo iniciar o leer el proceso: " + ex.getMessage()); System.exit(1);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); System.err.println("Espera interrumpida"); System.exit(130);
        }
    }

    /** Lee antes de esperar: así el hijo no llena su tubería sin lector. */
    private static void ejecutar(ProcessBuilder pb) throws IOException, InterruptedException {
        pb.redirectErrorStream(true);
        Process proceso = pb.start();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
            String linea;
            while ((linea = lector.readLine()) != null) System.out.println(linea);
        }
        int codigo = proceso.waitFor();
        if (codigo != 0) throw new EtapaFallida(codigo);
    }

    private static void ejecutarEn(File carpeta, String... orden) throws IOException, InterruptedException {
        java.util.List<String> comando = new java.util.ArrayList<>();
        if (WINDOWS) comando.addAll(java.util.List.of("cmd", "/d", "/c")); // Linux: el ejecutable va directo
        comando.addAll(java.util.List.of(orden));
        ejecutar(new ProcessBuilder(comando).directory(carpeta));
    }

    private static class EtapaFallida extends IOException {
        final int codigo;
        EtapaFallida(int codigo) { super("El comando termino con codigo " + codigo); this.codigo = codigo; }
    }
}
