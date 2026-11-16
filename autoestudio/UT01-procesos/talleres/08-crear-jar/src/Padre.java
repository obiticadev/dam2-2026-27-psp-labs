import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/** Lanza al Hijo y le pasa la carpeta que contiene Saludo.java. */
public class Padre {
    public static void main(String[] args) throws Exception {
        String bin = System.getProperty("java.home") + "/bin/";
        String carpeta = args.length > 0 ? args[0] : "pruebas";

        // Ejecutamos el fuente del Hijo, como en los talleres anteriores.
        // La carpeta viaja como un unico argumento, aunque tenga espacios.
        ProcessBuilder pb = new ProcessBuilder(
                bin + "java.exe", "src\\Hijo.java",
                new File(carpeta).getAbsolutePath());
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
        int codigo = process.waitFor();
        System.out.println("Codigo del Hijo recibido por el Padre: " + codigo);
        System.exit(codigo);
    }
}
