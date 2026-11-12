import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

/** Pruebas de aceptación sin dependencias. Ejecutar desde UT01-procesos.
 * Uso: java herramientas/Verificar.java soluciones [02-directorios]
 * En Windows prueba la rama CMD; en Linux, la rama ls/cp/mv/cat. Cada SO certifica solo su rama.
 * Solo ejecuta procesos creados por esta prueba y escribe bajo target/.
 */
public class Verificar {
    private static final String[] SLUGS = {"01-ping", "02-directorios", "03-buscar", "04-copiar",
            "05-mover", "06-leer", "07-otro-java", "08-crear-jar", "09-integrador"};
    private static final Path ROOT = Path.of("").toAbsolutePath();
    private static final String JAVA = Path.of(System.getProperty("java.home"), "bin", "java").toString();
    private static int passed, failed;
    private record Result(int code, String out) {}

    public static void main(String[] args) throws Exception {
        String kind = args.length > 0 ? args[0] : "soluciones";
        if (!List.of("soluciones", "talleres").contains(kind)) {
            throw new IllegalArgumentException("Usa soluciones o talleres");
        }
        String filter = args.length > 1 ? args[1] : "";
        if (!filter.isEmpty() && !List.of(SLUGS).contains(filter)) {
            throw new IllegalArgumentException("Taller desconocido: " + filter);
        }
        Path results = ROOT.resolve("target/verificacion");
        Files.createDirectories(results);
        Path run = Files.createTempDirectory(results, "psp zona de pruebas ");
        StringBuilder report = new StringBuilder("PSP / " + kind + " / " + System.getProperty("os.name")
                + " / " + java.time.Instant.now() + "\n");
        for (int i = 0; i < SLUGS.length; i++) {
            String slug = SLUGS[i];
            if (!filter.isEmpty() && !filter.equals(slug)) continue;
            Path source = ROOT.resolve(kind).resolve(slug).resolve("src");
            Path test = run.resolve(slug); Files.createDirectories(test);
            Path bin = test.resolve("bin"); Files.createDirectories(bin);
            List<String> compile = new ArrayList<>(List.of(
                    Path.of(System.getProperty("java.home"), "bin", "javac").toString(),
                    "-encoding", "UTF-8", "--release", "17", "-d", bin.toString()));
            try (var files = Files.list(source)) {
                files.filter(p -> p.toString().endsWith(".java")).sorted().forEach(p -> compile.add(p.toString()));
            }
            try {
                require(run(compile, test).code == 0, "No compila: mira los archivos proceso-*.txt");
                Path data = test.resolve("datos con espacios"); Files.createDirectories(data);
                Files.writeString(data.resolve("entrada con espacios.txt"), "CONTENIDO_PSP\nsegunda linea\n");
                Files.createDirectories(data.resolve("subcarpeta"));
                Files.writeString(data.resolve("subcarpeta/otro.txt"), "OTRO");
                Files.writeString(data.resolve("ignorar.csv"), "NO_LISTAR");
                Files.writeString(data.resolve("Saludo.java"),
                        "public class Saludo { public static void main(String[] a) { System.out.println(\"SALUDO:\" + a[0]); } }");
                String value = i == 0 ? "127.0.0.1" : i == 6 ? "Ada Lovelace" : data.toString();
                String cls = "estudio.t%02d.Padre".formatted(i + 1);
                Result r = run(List.of(JAVA, "-cp", bin.toString(), cls, value), test);
                require(r.code == 0 && r.out.contains("PADRE:codigo=0"), "Falla la cadena padre/hijo: " + r.out);
                switch (i) {
                    case 1 -> require(r.out.contains("entrada con espacios.txt"), "No se recupera DIR");
                    case 2 -> require(r.out.contains("otro.txt") && !r.out.contains("ignorar.csv"), "El filtro/recursion es incorrecto");
                    case 3 -> {
                        same(data.resolve("entrada con espacios.txt"), data.resolve("entrega/copia.txt"));
                        require(r.out.contains("copia.txt"), "No se lista la entrega");
                    }
                    case 4 -> {
                        require(!Files.exists(data.resolve("entrada con espacios.txt")), "MOVE no ha movido el origen");
                        require(Files.readString(data.resolve("archivo/final.txt")).startsWith("CONTENIDO_PSP"), "REN pierde el contenido");
                        require(!Files.exists(data.resolve("archivo/temporal.txt")), "REN no cambia el nombre");
                    }
                    case 5 -> require(r.out.contains("CONTENIDO_PSP") && r.out.contains("segunda linea"), "No se recupera TYPE completo");
                    case 6 -> {
                        require(r.out.contains("ECO:Ada Lovelace"), "Se ha dividido el argumento con espacios");
                        // Contraste real con la variante java Archivo.java (modo fuente).
                        Path fuente = data.resolve("EcoFuente.java");
                        Files.writeString(fuente, "public class EcoFuente { public static void main(String[] a) { System.out.println(\"FUENTE:\" + a[0]); } }");
                        Result sourceMode = run(List.of(JAVA, fuente.toString(), "Ada Lovelace"), test);
                        require(sourceMode.code == 0 && sourceMode.out.contains("FUENTE:Ada Lovelace"), "Falla la variante de ejecución de fuente .java");
                    }
                    case 7 -> {
                        require(r.out.contains("SALUDO:Ada"), "No se ejecuta el JAR");
                        try (JarFile jar = new JarFile(data.resolve("Aplicacion.jar").toFile())) {
                            require(jar.getEntry("Saludo.class") != null, "Falta Saludo.class");
                            require("Saludo".equals(jar.getManifest().getMainAttributes().getValue("Main-Class")), "Main-Class incorrecta");
                        }
                    }
                    case 8 -> { same(data.resolve("entrada con espacios.txt"), data.resolve("informe/resultado.txt"));
                        require(r.out.contains("CONTENIDO_PSP"), "No se muestra el informe"); }
                    default -> { }
                }
                // Prueba negativa: no fingir éxito ante datos inválidos.
                if (i > 0 && i != 6) {
                    Path bad = test.resolve("fallo"); Files.createDirectories(bad);
                    if (i == 1 || i == 2) bad = bad.resolve("no-existe");
                    if (i == 7) Files.writeString(bad.resolve("Saludo.java"), "public class Saludo { ERROR SINTAXIS }");
                    Result negative = run(List.of(JAVA, "-cp", bin.toString(), cls, bad.toString()), test);
                    require(negative.code != 0, "El padre debe propagar el fallo del hijo");
                    if (i == 7) require(!Files.exists(bad.resolve("Aplicacion.jar")), "No debe empaquetar si falla javac");
                }
                passed++; line(report, "PASS " + slug + ": cadena real y resultado comprobados");
            } catch (Exception | AssertionError e) {
                failed++; line(report, "FAIL " + slug + ": " + e.getMessage());
            }
        }
        line(report, "Resultado en " + System.getProperty("os.name") + ": " + passed + " correctos, " + failed + " fallidos.");
        Files.writeString(run.resolve("resultado.txt"), report);
        System.out.println("Evidencias conservadas en " + run);
        if (failed > 0) System.exit(1);
    }

    private static int processNumber;
    private static Result run(List<String> command, Path cwd) throws Exception {
        Path output = cwd.resolve("proceso-" + (++processNumber) + ".txt");
        Process p = new ProcessBuilder(command).directory(cwd.toFile()).redirectErrorStream(true)
                .redirectOutput(output.toFile()).start();
        if (!p.waitFor(Duration.ofSeconds(30).toMillis(), TimeUnit.MILLISECONDS)) {
            p.descendants().forEach(ProcessHandle::destroyForcibly); p.destroyForcibly();
            throw new IOException("Tiempo agotado; se cerraron los procesos de esta prueba");
        }
        // No exigir UTF-8 a CMD: su página de códigos depende de Windows.
        return new Result(p.exitValue(), new String(Files.readAllBytes(output), java.nio.charset.Charset.defaultCharset()));
    }
    private static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
    private static void same(Path a, Path b) throws IOException {
        require(Files.mismatch(a,b) == -1, "La copia no conserva los bytes");
    }
    private static void line(StringBuilder b, String message) { b.append(message).append('\n'); System.out.println(message); }
}
