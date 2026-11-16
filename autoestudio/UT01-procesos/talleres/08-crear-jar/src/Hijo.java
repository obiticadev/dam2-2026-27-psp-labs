import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** Compila Saludo.java, crea un JAR ejecutable y lo ejecuta. */
public class Hijo {
    public static void main(String[] args) throws Exception {
        String bin = System.getProperty("java.home") + "/bin/";
        File carpeta = new File(args.length > 0 ? args[0] : "pruebas");

        // 1. Compilar: Saludo.java -> clases/Saludo.class.
        System.out.println("1. Compilando Saludo.java");
        int codigo = ejecutar(carpeta,
                bin + "javac.exe", "-encoding", "UTF-8",
                "-d", "clases", "Saludo.java");
        if (codigo != 0) {
            System.exit(codigo);
        }

        // 2. Empaquetar: el JAR incluye las clases y declara el main de Saludo.
        System.out.println("2. Creando Aplicacion.jar");
        codigo = ejecutar(carpeta,
                bin + "jar.exe", "cfe", "Aplicacion.jar", "Saludo",
                "-C", "clases", ".");
        if (codigo != 0) {
            System.exit(codigo);
        }

        // 3. Ejecutar: Ada llega al args[0] de Saludo.
        System.out.println("3. Ejecutando Aplicacion.jar");
        codigo = ejecutar(carpeta,
                bin + "java.exe", "-jar", "Aplicacion.jar", "Ada");
        System.exit(codigo);
    }

    // Reutilizamos el mismo patron en las tres etapas.
    private static int ejecutar(File carpeta, String... comando) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.directory(carpeta);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        }
        int codigo = process.waitFor();
        System.out.println("Codigo de la etapa: " + codigo);
        return codigo;
    }
}
