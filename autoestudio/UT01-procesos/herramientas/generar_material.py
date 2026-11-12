"""Constructor inicial del material. Nunca sobreescribe archivos existentes.

No es necesario ejecutarlo para estudiar. Mantiene las referencias y plantillas
reproducibles; si existe cualquier destino, se detiene antes de escribir nada.
"""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LABS = [
    ("01-ping", "Diagnóstico de conexión", "localhost", '''
        String destino = args.length > 0 ? args[0] : "localhost";
        // ping es un ejecutable: no necesita cmd /c. -n es la opción de Windows.
        ejecutar(new ProcessBuilder("ping", "-n", "2", destino));''',
     "El padre recibe un destino y se lo pasa al hijo. El hijo ejecuta dos solicitudes ICMP, lee la salida y propaga el código de retorno. No se evalúa el texto localizado de ping. Un host puede bloquear ICMP sin estar caído.",
     "Cambia el número de solicitudes a cuatro. Distingue un destino que no responde de un comando que no puede arrancar."),
    ("02-directorios", "Explorar directorios", ".", '''
        String carpeta = args.length > 0 ? args[0] : ".";
        ProcessBuilder pb = new ProcessBuilder("cmd", "/d", "/c", "dir", "/b");
        // Así no hay que concatenar la ruta al texto que interpreta CMD.
        pb.directory(new File(carpeta));
        ejecutar(pb);''',
     "El padre recibe una carpeta. El hijo la establece como directorio de trabajo y ejecuta dir /b. La salida atraviesa dos lectores: CMD → hijo y después hijo → padre. No cambies el directorio global del equipo.",
     "Muestra subdirectorios con /s. Explica la diferencia entre una ruta pasada como argumento y directory()."),
    ("03-buscar", "Buscar archivos", ".", '''
        String carpeta = args.length > 0 ? args[0] : ".";
        // El patrón es fijo: CMD expande el comodín, no ProcessBuilder.
        ProcessBuilder pb = new ProcessBuilder("cmd", "/d", "/c", "dir", "/s", "/b", "*.txt");
        pb.directory(new File(carpeta));
        ejecutar(pb);''',
     "Recibe una carpeta y lista sus archivos .txt, también en subcarpetas. No uses Files.walk: se está practicando CMD. El orden de salida no es un contrato. Si no hay coincidencias, dir puede devolver error.",
     "Busca .java en lugar de .txt. Predice qué cambia al quitar /s o /b."),
    ("04-copiar", "Preparar una entrega", ".", '''
        File base = new File(args.length > 0 ? args[0] : ".");
        if (!base.isDirectory()) throw new IOException("No existe la carpeta de pruebas");
        // Los nombres son propios del ejercicio, no órdenes recibidas del usuario.
        if (!new File(base, "entrega").exists()) {
            ejecutarEn(base, "mkdir", "entrega");
        }
        ejecutarEn(base, "copy", "/Y", "entrada con espacios.txt", "entrega\\\\copia.txt");
        ejecutarEn(base, "dir", "/b", "entrega");''',
     "En una carpeta de pruebas con entrada con espacios.txt, crea entrega, copia el archivo a entrega\\copia.txt y muestra la carpeta. El origen debe conservarse. /Y permite reemplazar solo la copia generada en el taller.",
     "Quita el archivo de origen y comprueba que no aparece un mensaje falso de éxito. Explica por qué mkdir se condiciona si la carpeta ya existe."),
    ("05-mover", "Mover y renombrar", ".", '''
        File base = new File(args.length > 0 ? args[0] : ".");
        if (!base.isDirectory()) throw new IOException("No existe la carpeta de pruebas");
        if (!new File(base, "archivo").exists()) ejecutarEn(base, "mkdir", "archivo");
        ejecutarEn(base, "move", "/Y", "entrada con espacios.txt", "archivo\\\\temporal.txt");
        ejecutarEn(base, "ren", "archivo\\\\temporal.txt", "final.txt");
        ejecutarEn(base, "dir", "/b", "archivo");''',
     "Mueve entrada con espacios.txt a archivo\\temporal.txt y renómbralo a final.txt. El archivo deja de estar en el origen, pero conserva los bytes. REN recibe un nuevo nombre, no otra ruta de destino.",
     "Explica qué ocurriría si se repite la operación. Prepara datos nuevos antes de cada intento: mover consume el archivo original."),
    ("06-leer", "Recuperar el contenido", ".", '''
        File base = new File(args.length > 0 ? args[0] : ".");
        ejecutarEn(base, "type", "entrada con espacios.txt");''',
     "Ejecuta TYPE para mostrar entrada con espacios.txt. El padre debe recibir el texto, no únicamente el código 0. No resuelvas la lectura con FileReader: el objetivo es capturar la salida de un comando.",
     "Prueba un archivo vacío y uno inexistente. No confundas una línea vacía con el final del flujo."),
    ("07-otro-java", "Java que lanza Java", "Ada", '''
        String nombre = args.length > 0 ? args[0] : "Ada";
        String java = new File(System.getProperty("java.home"), "bin/java").getPath();
        String cp = new File(System.getProperty("java.class.path")).getAbsolutePath();
        // Este nieto es otra JVM: no equivale a llamar a Eco.main(args).
        ejecutar(new ProcessBuilder(java, "-cp", cp, "estudio.t07.Eco", nombre));''',
     "El padre lanza Hijo y este lanza Eco como una tercera JVM. Eco recibe un nombre, imprime ECO:nombre y termina. Conserva correctamente un argumento como Ada Lovelace. No llames directamente a Eco.main.",
     "Crea un Eco.java sin paquete y ejecútalo como fuente con java Eco.java. Después compáralo con java -cp bin estudio.t07.Eco. El modo fuente requiere Java 11 o posterior."),
    ("08-crear-jar", "Compilar, empaquetar y ejecutar", ".", '''
        File base = new File(args.length > 0 ? args[0] : ".");
        String herramientas = new File(System.getProperty("java.home"), "bin").getPath();
        ejecutar(new ProcessBuilder(new File(herramientas, "javac").getPath(),
                "-encoding", "UTF-8", "-d", "clases", "Saludo.java").directory(base));
        ejecutar(new ProcessBuilder(new File(herramientas, "jar").getPath(),
                "cfe", "Aplicacion.jar", "Saludo", "-C", "clases", ".").directory(base));
        ejecutar(new ProcessBuilder(new File(herramientas, "java").getPath(),
                "-jar", "Aplicacion.jar", "Ada").directory(base));''',
     "En la carpeta recibida hay un Saludo.java sin paquete. El hijo lo compila a clases, crea Aplicacion.jar con Saludo como clase principal y lo ejecuta con Ada como argumento. Si una etapa falla, no debe empezar la siguiente. No uses Maven para ocultar los comandos.",
     "Introduce un error de sintaxis y comprueba que se detiene antes de jar. Después mueve Saludo a un paquete y adapta clase principal, fuente y ejecución."),
    ("09-integrador", "Centro de operaciones", ".", '''
        File base = new File(args.length > 0 ? args[0] : ".");
        ejecutarEn(base, "dir", "/b");
        if (!new File(base, "informe").exists()) ejecutarEn(base, "mkdir", "informe");
        ejecutarEn(base, "copy", "/Y", "entrada con espacios.txt", "informe\\\\resultado.txt");
        ejecutarEn(base, "type", "informe\\\\resultado.txt");''',
     "Sin plantilla guiada: el padre recibe la carpeta de trabajo, lanza al hijo y recupera sus mensajes. El hijo lista la carpeta, crea informe, copia el archivo de entrada a resultado.txt y muestra su contenido. Debe abortar ante el primer fallo. No cambies los archivos originales.",
     "Sustituye la copia por la compilación y ejecución de un JAR. Haz que la selección de operación se decida con argumentos validados, nunca ejecutando una cadena arbitraria de CMD."),
]

HELPERS = '''
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
        comando.addAll(java.util.List.of("cmd", "/d", "/c"));
        comando.addAll(java.util.List.of(orden));
        ejecutar(new ProcessBuilder(comando).directory(carpeta));
    }

    private static class EtapaFallida extends IOException {
        final int codigo;
        EtapaFallida(int codigo) { super("El comando termino con codigo " + codigo); this.codigo = codigo; }
    }
'''

def main():
    files = {}
    for index, (slug, title, default, body, contract, variant) in enumerate(LABS, 1):
        package = f"estudio.t{index:02}"
        parent_body = f'''
        String java = new File(System.getProperty("java.home"), "bin/java").getPath();
        // Un único directorio de clases, sin dependencias externas.
        String cp = new File(System.getProperty("java.class.path")).getAbsolutePath();
        String dato = args.length > 0 ? args[0] : "{default}";
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", cp, "{package}.Hijo", dato);
        pb.redirectErrorStream(true);
        Process hijo = pb.start();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(hijo.getInputStream()))) {{
            String linea;
            while ((linea = lector.readLine()) != null) System.out.println(linea);
        }}
        int codigo = hijo.waitFor();
        System.out.println("PADRE:codigo=" + codigo);
        System.exit(codigo);'''
        for kind in ["talleres", "soluciones"]:
            folder = ROOT / kind / slug
            for cls in ["Padre", "Hijo"]:
                if kind == "talleres":
                    action = '''
        // TODO: configura ProcessBuilder con cada argumento separado.
        // TODO: arranca UNA vez; fusiona stderr, lee hasta null y espera.
        // TODO: propaga el codigo de salida; detente si falla una etapa.
        throw new UnsupportedOperationException("Ejercicio pendiente: consulta el panel y completa este main");'''
                    if cls == "Padre":
                        action = action.replace("configura ProcessBuilder", f"lanza {package}.Hijo con java -cp; configura ProcessBuilder")
                    extra = ""
                else:
                    action = parent_body if cls == "Padre" else body
                    extra = "" if cls == "Padre" else HELPERS
                if cls == "Hijo" and kind == "soluciones":
                    action = '''
        try {''' + action + '''
        } catch (EtapaFallida ex) {
            System.err.println(ex.getMessage()); System.exit(ex.codigo);
        } catch (IOException ex) {
            System.err.println("No se pudo iniciar o leer el proceso: " + ex.getMessage()); System.exit(1);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); System.err.println("Espera interrumpida"); System.exit(130);
        }'''
                files[folder / "src" / f"{cls}.java"] = f'''package {package};

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** {title}: {cls}. {"Referencia, no copiar sobre tu intento." if kind == "soluciones" else "Plantilla para resolver."} */
public class {cls} {{
    public static void main(String[] args) throws Exception {{{action}
    }}
{extra}}}
'''
            if index == 7:
                files[folder / "src" / "Eco.java"] = '''package estudio.t07;
/** Programa auxiliar ya resuelto: el objetivo es lanzarlo como proceso. */
public class Eco {
    public static void main(String[] args) {
        if (args.length != 1) { System.err.println("Falta el nombre"); System.exit(2); }
        System.out.println("ECO:" + args[0]);
    }
}
'''
            files[folder / "README.md"] = f'''# {index:02} · {title}

{"Referencia completa. Intenta resolver primero talleres/" + slug + "." if kind == "soluciones" else "Tu espacio de trabajo. Las soluciones están fuera de esta carpeta."}

## Enunciado

{contract}

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\\*.java
java -cp bin {package}.Padre "{default if index in [1,7] else 'C:\\PSP\\zona de pruebas'}"
```

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\\comprobar.cmd {kind} {slug}`.

## Variante sin ayudas

{variant}

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/{slug}).
'''
    existing = [str(p) for p in files if p.exists()]
    if existing:
        raise SystemExit("No se escribe nada: ya existe material. No sobreescribir intentos. " + existing[0])
    for path, text in files.items():
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text, encoding="utf-8")
    print(f"Creados {len(files)} archivos; futuras ejecuciones se detendrán sin sobrescribirlos.")

if __name__ == "__main__":
    main()
