"""Genera los dos documentos de repaso. Dependencias: reportlab y pygments."""
from pathlib import Path
import sys
import html
import re

TEMP_LIBS = Path.home() / "AppData/Local/Temp/opencode/pdf-libs"
if TEMP_LIBS.exists():
    sys.path.insert(0, str(TEMP_LIBS))

from reportlab.pdfgen import canvas
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle,
    XPreformatted, KeepTogether,
)
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_LEFT
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from pygments import lex
from pygments.lexers import JavaLexer, BatchLexer
from pygments.token import Token

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "repaso"
OUT.mkdir(exist_ok=True)
FONTDIR = Path("C:/Windows/Fonts")
for name, filename in [
    ("Code", "consola.ttf"), ("CodeBold", "consolab.ttf"),
    ("Body", "segoeui.ttf"), ("BodyBold", "segoeuib.ttf"),
]:
    pdfmetrics.registerFont(TTFont(name, str(FONTDIR / filename)))
pdfmetrics.registerFontFamily("Body", normal="Body", bold="BodyBold")
pdfmetrics.registerFontFamily("Code", normal="Code", bold="CodeBold")

NAVY = colors.HexColor("#14283f")
TEAL = colors.HexColor("#007f88")
BLUE = colors.HexColor("#2862a6")
MUTED = colors.HexColor("#526176")
PALE = colors.HexColor("#eef4f8")
AMBER = colors.HexColor("#9b6100")
styles = getSampleStyleSheet()
styles.add(ParagraphStyle("TitleX", fontName="BodyBold", fontSize=31,
    leading=36, textColor=NAVY, spaceAfter=20))
styles.add(ParagraphStyle("SectionX", fontName="BodyBold", fontSize=21,
    leading=26, textColor=NAVY, spaceAfter=14))
styles.add(ParagraphStyle("SubX", fontName="BodyBold", fontSize=12,
    leading=16, textColor=TEAL, spaceBefore=12, spaceAfter=8,
    keepWithNext=True))
styles.add(ParagraphStyle("TextX", fontName="Body", fontSize=9.5,
    leading=14, textColor=NAVY, spaceAfter=7))
styles.add(ParagraphStyle("SmallX", parent=styles["TextX"], fontSize=8,
    leading=11, spaceAfter=5))
styles.add(ParagraphStyle("CodeX", fontName="Code", fontSize=8,
    leading=10.8, textColor=NAVY, spaceBefore=0, spaceAfter=0))
styles.add(ParagraphStyle("CallX", parent=styles["TextX"],
    spaceBefore=0, spaceAfter=0))
styles.add(ParagraphStyle("QuestionX", fontName="BodyBold", fontSize=11,
    leading=15, textColor=TEAL, spaceBefore=10, spaceAfter=6,
    keepWithNext=True))

def p(text, small=False):
    return Paragraph(text, styles["SmallX" if small else "TextX"])

def sub(text):
    return Paragraph(text, styles["SubX"])

def note(text):
    return panel(Paragraph(text, styles["CallX"]), "#e8f5f2", "#007f88")

def panel(content, background, accent):
    """El padding forma parte de la altura real: nunca invade otro bloque."""
    box = Table([[content]], colWidths=[497.276], hAlign="LEFT")
    box.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), colors.HexColor(background)),
        ("LINEBEFORE", (0, 0), (0, -1), 2, colors.HexColor(accent)),
        ("LEFTPADDING", (0, 0), (-1, -1), 11),
        ("RIGHTPADDING", (0, 0), (-1, -1), 11),
        ("TOPPADDING", (0, 0), (-1, -1), 10),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 10),
    ]))
    box.spaceBefore = 3
    box.spaceAfter = 11
    return box

def code(text, lang="java"):
    text = text.strip("\n")
    lexer = JavaLexer() if lang == "java" else BatchLexer()
    parts = []
    tokens = list(lex(text, lexer))
    for i, (token, value) in enumerate(tokens):
        color = "#243447"
        bold = False
        if token in Token.Comment:
            color = "#64748b"
        elif token in Token.Literal.String:
            color = "#14723f"
        elif token in Token.Literal.Number:
            color = "#b45c08"
        elif token in Token.Keyword.Type:
            color, bold = "#006b93", True
        elif token in Token.Keyword:
            color, bold = "#9631ae", True
        elif token in Token.Name:
            following = next((v for t, v in tokens[i+1:]
                              if v.strip()), "")
            if value[:1].isupper() or token in Token.Name.Class:
                color, bold = "#006f8b", True
            elif token in Token.Name.Function or following.startswith("("):
                color = "#1755a4"
            else:
                color = "#70492e"
        elif token in Token.Operator:
            color = "#a43b53"
        elif token in Token.Punctuation:
            color = "#40546a"
        escaped = html.escape(value)
        if bold:
            escaped = f"<b>{escaped}</b>"
        parts.append(f'<font color="{color}">{escaped}</font>')
    content = XPreformatted("".join(parts).rstrip(), styles["CodeX"])
    return panel(content, "#eef4f8", "#2862a6")

def table(headers, rows, widths=None):
    data = [[p(f"<b>{html.escape(x)}</b>", True) for x in headers]]
    data += [[p(str(x), True) for x in row] for row in rows]
    t = Table(data, colWidths=widths, hAlign="LEFT", repeatRows=1)
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#dae9f0")),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, PALE]),
        ("VALIGN", (0, 0), (-1, -1), "TOP"),
        ("LEFTPADDING", (0, 0), (-1, -1), 8),
        ("RIGHTPADDING", (0, 0), (-1, -1), 8),
        ("TOPPADDING", (0, 0), (-1, -1), 6),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 6),
        ("LINEBELOW", (0, 0), (-1, 0), .7, TEAL),
    ]))
    return t

class Anchor(Spacer):
    def __init__(self, key, title, level=0):
        super().__init__(0, 0)
        self.key, self.title = key, title
        self.level = level
    def draw(self):
        self.canv.bookmarkPage(self.key)
        self.canv.addOutlineEntry(self.title, self.key, self.level)

def header(c, doc):
    w, h = doc.pagesize
    c.setFillColor(TEAL)
    c.rect(0, h - 9, w, 9, fill=1, stroke=0)
    c.setFont("Body", 8)
    c.setFillColor(MUTED)
    c.drawString(43, h - 30, "PSP  /  UT01 · PROCESOS  /  WINDOWS + JAVA")
    c.setStrokeColor(colors.HexColor("#d8e2e9"))
    c.line(43, 37, w - 43, 37)
    c.drawString(43, 23, doc.short_title)
    c.drawRightString(w - 43, 23, str(doc.page))

def build(filename, story, title):
    doc = SimpleDocTemplate(str(OUT / filename), pagesize=(595.276, 841.89),
        leftMargin=43, rightMargin=43, topMargin=49, bottomMargin=49,
        title=title, author="Material de repaso · PSP UT01",
        subject="Procesos Java, CMD, argumentos, flujos y talleres 01-09")
    doc.short_title = title
    doc.build(story, onFirstPage=header, onLaterPages=header)

sections = []
def page(key, title, *content):
    sections.append((key, title, list(content)))

page("teoria", "01 · Teoría esencial",
    note("<b>Programa ≠ proceso ≠ hilo.</b> El archivo existe en disco; el proceso existe mientras se ejecuta; sus hilos ejecutan las instrucciones."),
    table(["Concepto", "Respuesta breve para el examen"], [
        ("Programa / ejecutable", "Programa: instrucciones. Ejecutable: archivo que permite ponerlas en marcha. Un .jar es un archivo, no un proceso."),
        ("Proceso", "Instancia de un programa en ejecución, con memoria, recursos y estado. El SO lo identifica mediante un PID."),
        ("Hilo", "Flujo de ejecución dentro de un proceso. Tiene pila y registros propios; comparte código y memoria con otros hilos del mismo proceso."),
        ("Procesos vs. hilos", "Los procesos tienen entornos normalmente aislados; los hilos comparten recursos. Los procesos también pueden comunicarse mediante mecanismos explícitos."),
        ("Concurrencia", "Varias tareas progresan durante intervalos solapados. Un solo núcleo puede alternarlas."),
        ("Paralelismo", "Ejecución realmente simultánea en varias unidades de procesamiento. Concurrencia no implica paralelismo."),
        ("Ventajas de los hilos", "Menor coste habitual de creación/cambio, intercambio de datos y capacidad de respuesta. No garantizan acelerar cualquier programa."),
        ("Tipos de procesos", "Por lotes: importa el resultado. Interactivos: intercambio continuo con el usuario. Tiempo real: deben cumplirse plazos, no solo ir rápido."),
    ], [130, 379]),
    sub("Aplicado a nuestros talleres"),
    p("Padre Java, Hijo Java y ping son <b>tres procesos</b>. Cada JVM tiene su propia memoria. Llamar a <font face='Code'>Hijo.main(args)</font> sería una llamada a un método dentro de la misma JVM, no crear un proceso."),
    p("Estos ejercicios se resuelven de forma secuencial: no necesitas Thread, Executor ni sincronización."),
    sub("Comunicación y aislamiento"),
    p("Cada proceso tiene su entorno y espacio de direcciones: cambiar una variable del hijo no cambia la del padre. Para comunicarse usan mecanismos explícitos: tuberías, sockets, archivos o memoria compartida. Aquí usamos argumentos al iniciar y tuberías para la salida."),
    p("Los hilos comparten memoria, lo que facilita comunicarse, pero puede provocar condiciones de carrera si acceden a un mismo dato sin coordinación. El aislamiento de procesos reduce ese acoplamiento, a cambio de una comunicación normalmente más costosa.")
)
page("estados", "02 · Estados y planificación",
    sub("Modelo de cinco estados"),
    table(["Estado", "Qué significa"], [
        ("Nuevo", "Se acaba de crear y está pendiente de admisión."),
        ("Listo", "Puede ejecutarse, pero espera que le asignen CPU."),
        ("En ejecución", "Está utilizando la CPU."),
        ("Bloqueado", "Espera un suceso o una operación de E/S; no le basta con que quede CPU libre."),
        ("Terminado", "Ha finalizado o ha sido retirado."),
    ], [115, 394]),
    note("<b>Transiciones:</b> nuevo → listo → ejecución → terminado. Ejecución → bloqueado por E/S; bloqueado → listo cuando llega el suceso; ejecución → listo por expulsión/fin del quantum."),
    sub("BCP, contexto y swapping"),
    p("<b>BCP / PCB:</b> registro del SO con PID, estado, memoria, recursos, prioridad e información de planificación y contexto de CPU: registros, contador de programa y puntero de pila."),
    p("<b>Cambio de contexto:</b> guardar el estado de la tarea que sale y restaurar el de la que entra. Permite reanudarla donde estaba, pero consume tiempo. No significa copiar el programa entero a disco."),
    p("<b>Swapping:</b> intercambio entre RAM y almacenamiento para liberar memoria. En el modelo ampliado aparecen listo/suspendido y bloqueado/suspendido. Suspendido no es sinónimo de bloqueado."),
    sub("Planificador: decide quién usa la CPU"),
    table(["Algoritmo", "Idea y riesgo"], [
        ("FCFS", "Orden de llegada; en su forma no expulsiva deja terminar la ráfaga. Una tarea larga puede retrasar a las demás."),
        ("SJF", "Primero la ráfaga más corta entre las disponibles. Puede perjudicar a trabajos largos. SRTF es su variante expulsiva."),
        ("Round Robin", "Cola circular y quantum. Si no termina, vuelve a la cola. Un quantum pequeño aumenta los cambios de contexto."),
    ], [115, 394]),
    p("Objetivos: equidad, uso de CPU, rendimiento y tiempos de respuesta/retorno razonables. <b>Retorno</b> = finalización − llegada; <b>respuesta inicial</b> = primera ejecución − llegada.", True),
    p("<b>Ejemplo:</b> si el padre llama a waitFor(), espera a que el hijo termine; no le está quitando la CPU al hijo. Cuando llega el suceso, el llamador vuelve a estar listo. Una planificación expulsiva permite retirar la CPU a una tarea; la no expulsiva no lo hace arbitrariamente.", True)
)
page("api", "03 · El contrato de la API Java",
    table(["Elemento", "Para qué sirve"], [
        ("ProcessBuilder", "Configura ejecutable, argumentos, carpeta, entorno y redirecciones. Construirlo NO arranca nada."),
        ("pb.start()", "Solicita crear el proceso y devuelve un Process. Se llama una sola vez por ejecución deseada."),
        ("Process", "Representa el proceso creado: flujos, espera, estado y código de salida."),
        ("pb.directory(File)", "Fija la carpeta de trabajo del nuevo proceso. No crea carpetas ni cambia la carpeta del padre."),
        ("pb.environment()", "Mapa de variables de entorno del futuro proceso; modificarlo afecta a ese proceso, no al Windows global."),
        ("redirectErrorStream(true)", "Fusiona stderr con stdout. Configúralo antes de start(). Sin true, el método sin argumentos solo consulta."),
        ("process.waitFor()", "Bloquea al llamador hasta que termina el proceso; devuelve su código. No detiene al hijo."),
        ("process.exitValue()", "Consulta el código sin esperar. Si sigue vivo lanza IllegalThreadStateException."),
        ("System.exit(codigo)", "Termina esta JVM con ese código; permite propagar el resultado hacia el padre."),
    ], [161, 348]),
    sub("Los flujos se nombran desde el padre"),
    table(["Método del Process", "Dirección"], [
        ("getInputStream()", "Leer lo que el hijo escribe en stdout."),
        ("getErrorStream()", "Leer lo que el hijo escribe en stderr (si no se fusionó)."),
        ("getOutputStream()", "Escribir en stdin del hijo. No es para leer su salida."),
    ], [161, 348]),
    note("<b>Regla:</b> configurar → arrancar → consumir salida → esperar → comprobar → propagar."),
    p("Un IOException de start() significa que no se pudo arrancar (ruta, permisos, carpeta...). Un código distinto de cero significa que el proceso sí arrancó, pero declaró un problema. Cero suele significar éxito según su contrato.")
)
page("patron", "04 · Patrón maestro reutilizable",
    p("Este helper sirve tanto para el padre que lanza al hijo como para el hijo que ejecuta CMD, ping o las herramientas del JDK. Necesita los imports indicados."),
    code(r'''import java.io.*;

static int ejecutar(ProcessBuilder pb) throws Exception {
    pb.redirectErrorStream(true);
    Process proceso = pb.start();

    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(proceso.getInputStream()))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
    }
    return proceso.waitFor();
}'''),
    sub("Una operación: devolver su resultado"),
    code(r'''public static void main(String[] args) throws Exception {
    ProcessBuilder pb =
        new ProcessBuilder("ping", "-n", "2", "127.0.0.1");
    int codigo = ejecutar(pb);
    System.exit(codigo);
}'''),
    sub("Varias operaciones: abortar ante el primer fallo"),
    code(r'''int codigo = ejecutar(etapa1);
if (codigo != 0) {
    System.exit(codigo);
}
codigo = ejecutar(etapa2);
System.exit(codigo);'''),
    note("<b>Cada nivel hace su trabajo:</b> el hijo consume y propaga el resultado del comando; el padre consume y propaga el del hijo. Fusionar errores en el padre NO fusiona automáticamente los del nieto."),
    p("Consume la salida antes de waitFor(): una tubería llena puede bloquear al hijo si nadie la lee. readLine() devuelve <b>null</b> al terminar; una línea vacía devuelve <b>\"\"</b> y no debe cortar el bucle."),
    p("No dejes un catch vacío. Para depurar: e.printStackTrace(). Si capturas un fallo, no lo conviertas luego en éxito ni continúes la cadena. Este helper deja propagar las excepciones.", True)
)
page("args", "05 · Argumentos: el viaje de los datos",
    sub("El padre pasa texto; el hijo lo recibe en args"),
    code(r'''// En el padre:
String nombre = "Ada Lovelace";
ProcessBuilder pb = new ProcessBuilder(
    rutaJava, "src/Hijo.java", nombre
);

// En el hijo:
public static void main(String[] args) {
    String nombre = args.length > 0 ? args[0] : "Ada";
    System.out.println("ECO:" + nombre);
}'''),
    table(["Expresión", "Significado"], [
        ("args.length", "Cuántos argumentos ha recibido este main."),
        ("args[0]", "Primer argumento. Hay que comprobar que exista."),
        ("args[1]", "Segundo argumento. Requiere args.length > 1."),
        ('condicion ? valor1 : valor2', "Operador ternario: si se cumple, valor1; si no, valor2."),
        ('args.length > 0 ? args[0] : "."', "Usa la carpeta recibida o la carpeta actual como valor por defecto."),
    ], [210, 299]),
    sub("Equivalencia con la terminal"),
    code(r'''java src\Hijo.java "Ada Lovelace"
rem En Hijo: args.length == 1
rem args[0] contiene Ada Lovelace, sin las comillas.''', "bat"),
    p("En ProcessBuilder, un nombre con espacios se pasa como <b>un elemento de la lista</b>; al llamar directamente a un .exe no añadas comillas dentro del valor. Los argumentos no incluyen el ejecutable ni el nombre del fuente."),
    sub("Comas y + tienen trabajos diferentes"),
    code(r'''// + forma la ruta completa del ejecutable.
// Las comas separan ejecutable y argumentos.
new ProcessBuilder(bin + "java.exe", "src/Hijo.java", nombre);

// INCORRECTO: busca un programa llamado "ping -n 2".
new ProcessBuilder("ping -n 2", "localhost");'''),
    note("Padre e hijo no comparten sus variables normales. Pueden llamarse igual o distinto: los datos viajan como argumentos y por los flujos, no por el nombre de la variable.")
)
page("rutas", "06 · Rutas, Java y trabajo sin PATH",
    sub("1. Obtener el Java que está ejecutando este programa"),
    code(r'''String bin = System.getProperty("java.home") + "/bin/";
String rutaJava = bin + "java.exe";
String rutaJavac = bin + "javac.exe";
String rutaJar = bin + "jar.exe";'''),
    p("<b>java.home</b> es la clave exacta de una propiedad de la JVM, no una ruta escrita a mano. getProperty devuelve su carpeta de instalación. No escribas java/home. El nombre de la variable bin lo eliges tú."),
    sub("2. Comprobar qué herramientas existen"),
    code(r'''for (String nombre : new String[]{"java", "javac", "jar"}) {
    File exe = new File(bin + nombre + ".exe");
    System.out.println(nombre + ": " + exe.isFile());
}'''),
    note("<b>Ruta completa ≠ instalación.</b> El Extension Pack puede ejecutar con su runtime privado, pero no garantiza los tres .exe. Si falta alguno, busca otro JDK completo o usa uno portátil si está permitido. No hay garantía universal solo con la extensión."),
    p("En Windows, un lanzamiento directo puede encontrar java junto al ejecutable del padre aunque CMD no lo encuentre. <b>No dependas de esa casualidad</b>: para el examen, prueba y usa rutas completas."),
    sub("3. Rutas relativas y carpeta de trabajo"),
    code(r'''System.out.println(System.getProperty("user.dir"));
File base = new File(args.length > 0 ? args[0] : ".")
    .getAbsoluteFile();
pb.directory(base);  // Configurar ANTES de start().'''),
    p("<b>.</b> = carpeta actual; <b>..</b> = carpeta superior. src/Hijo.java solo sirve si partes de la raíz del taller. Si cambias directory(), cambia también desde dónde se resuelven las rutas relativas del comando."),
    p("Java y muchas API Windows aceptan C:/carpeta/archivo. En strings Java, una barra inversa se escribe doble: <font face='Code'>\"src\\\\Hijo.java\"</font>. Para órdenes CMD, usa preferentemente barras inversas."),
    sub("Alternativa sin PATH desde CMD"),
    code(r'''"C:\ruta real\jdk\bin\javac.exe" Saludo.java
where java
where javac
where jar''', "bat"),
    p("where consulta la búsqueda del entorno de CMD. Si no encuentra nada, eso no demuestra que no haya una instalación en otra carpeta.", True)
)
page("modos", "07 · Fuente, clases y JAR: no mezclarlos",
    table(["Modo", "Comando y requisito"], [
        ("Fuente (.java)", "java src\\Hijo.java dato<br/>Java 11 o superior. Compila para ejecutar; no prepara una distribución .jar."),
        ("Clase compilada", "java -cp bin estudio.t07.Hijo dato<br/>Primero compilar. La clase se escribe sin .class."),
        ("JAR ejecutable", "java -jar Aplicacion.jar dato<br/>El manifiesto debe declarar la clase principal."),
    ], [120, 389]),
    sub("Modo fuente sencillo para tus prácticas"),
    code(r'''// Desde la raíz del taller, Hijo.java SIN package:
new ProcessBuilder(rutaJava, "src/Hijo.java", destino);'''),
    p("Para evitar choques entre las clases Padre/Hijo sin paquete, abre <b>un taller por ventana</b> como proyecto independiente. Para estos ejemplos fuente usamos archivos sin package y rutas coherentes."),
    sub("Modo compilado con paquetes: estructura recomendada"),
    code(r'''// src/estudio/t07/Hijo.java:
package estudio.t07;

// Comandos desde la raíz del taller:
// javac -d bin src/estudio/t07/*.java
// java -cp bin estudio.t07.Hijo "Ada Lovelace"'''),
    p("Después de compilar, debe existir <b>bin/estudio/t07/Hijo.class</b>. El classpath apunta a <b>bin</b>, raíz del paquete, no a bin/estudio/t07. Los fuentes originales del repositorio están directamente en src; javac puede compilarlos indicando sus archivos, pero el IDE o el lanzador fuente pueden exigir coherencia entre paquete y ruta."),
    sub("Cuando cambias el directorio del nuevo proceso"),
    code(r'''String cp = new File("bin").getAbsolutePath();
ProcessBuilder pb = new ProcessBuilder(
    rutaJava, "-cp", cp, "estudio.t07.Hijo", destino
);'''),
    note("No uses -cp src/Hijo.java: -cp indica dónde buscar clases y dependencias, no qué fuente ejecutar. Al ejecutar el fuente, no necesitas ese -cp."),
    p("No mezcles clases antiguas de bin con fuentes que ya has cambiado. Si falla la compilación, no continúes ejecutando una versión vieja. En VS Code, limpiar Java Language Server Workspace puede eliminar diagnósticos/cachés obsoletos.", True)
)
page("cmd", "08 · Diccionario de CMD y opciones",
    p("<b>Programa externo:</b> ping.exe, java.exe, javac.exe, jar.exe se pueden ejecutar directamente. <b>Orden interna:</b> dir, copy, move, ren, type, mkdir necesitan CMD incluso si lanzas Java desde una terminal CMD."),
    table(["Comando / opción", "Qué hace"], [
        ("cmd /c orden", "Ejecuta la orden y sale. /k deja CMD abierto: evítalo aquí. /d desactiva AutoRun y hace la ejecución más predecible."),
        ("ping -n 2 host", "Envía 2 solicitudes ICMP. -w 1000 espera hasta 1000 ms por respuesta; no es un timeout global. -t es continuo."),
        ("dir /b", "Lista nombres en formato simple, sin cabeceras ni resúmenes."),
        ("dir /s /b /a-d *.txt", "Recorre subcarpetas, formato simple y solo archivos. /a-d excluye directorios; *.txt es un patrón, .txt no lo es."),
        ("mkdir entrega", "Crea una carpeta. Comprueba antes si ya existe cuando debas permitir repetir la práctica."),
        ("copy /y origen destino", "Copia y conserva el origen. /y permite sobrescribir sin preguntar; no genera nombres numerados."),
        ("move /y origen destino", "Traslada; el origen deja de estar en su ubicación. /y evita la pregunta de reemplazo."),
        ("ren ruta\\viejo.txt nuevo.txt", "Cambia el nombre en esa carpeta. El segundo argumento es un nombre, no otra ruta."),
        ("type archivo.txt", "Muestra el contenido de un archivo de texto en stdout; no lo modifica."),
        ("where ping · where java", "Busca ejecutables en la carpeta actual y PATH de CMD. No busca órdenes internas como dir."),
        ("which / type (Bash)", "En Bash/Linux, which busca programas; type también identifica órdenes internas. No son el equivalente nativo de CMD."),
        ("dir /? · help dir", "Ayuda offline para las opciones. help muestra órdenes internas. Java usa --help."),
    ], [192, 317]),
    sub("Dos formas: directo o a través del intérprete"),
    code(r'''new ProcessBuilder("ping", "-n", "2", destino);
new ProcessBuilder("cmd", "/d", "/c", "dir", "/b");'''),
    note("<b>CMD interpreta texto:</b> &, |, &gt;, &lt;, %, ! y las comillas pueden tener significado especial. Separar argumentos NO convierte texto arbitrario en seguro. En estos talleres usa comandos fijos, datos controlados y directory() para la carpeta."),
    p("<b>Cómo decidir:</b> where ping muestra ping.exe; where dir normalmente no encuentra nada, pero help dir sí explica la orden interna. Si where java no encuentra nada, puede faltar solo el PATH. En PowerShell usa where.exe, porque where puede ser un alias distinto.", True),
    p("Para rutas con espacios en órdenes internas, verifica las comillas de CMD. Los ejemplos siguientes usan órdenes fijas con comillas explícitas. En .exe directos no añadas comillas manuales.", True)
)
page("t123", "09 · Talleres 01–03: observar y buscar",
    sub("01 / Ping: pasar destino y recuperar respuestas"),
    p("Padre recibe host, lanza Hijo y reenvía su salida. Hijo recibe args[0] y ejecuta ping. Prueba primero 127.0.0.1 o localhost: no necesitas Internet."),
    code(r'''String destino = args.length > 0 ? args[0] : "localhost";
int codigo = ejecutar(
    new ProcessBuilder("ping", "-n", "2", destino));
System.exit(codigo);'''),
    p("<b>Comprueba:</b> dos solicitudes; la salida llega al padre; el código se propaga. ICMP puede estar bloqueado: que ping falle no demuestra que una web esté caída."),
    sub("02 / Directorio: fijar dónde se ejecuta DIR"),
    code(r'''File base = new File(args.length > 0 ? args[0] : ".")
    .getAbsoluteFile();
ProcessBuilder pb =
    new ProcessBuilder("cmd", "/d", "/c", "dir", "/b");
pb.directory(base);
System.exit(ejecutar(pb));'''),
    p("<b>Idea clave:</b> args lleva la ruta como texto; directory() convierte esa decisión en carpeta de trabajo. Una carpeta inexistente puede impedir start(). No hay que hacer cd global."),
    sub("03 / Buscar: cambiar argumentos, no el patrón de lectura"),
    code(r'''ProcessBuilder pb = new ProcessBuilder(
    "cmd", "/d", "/c", "dir", "/s", "/b", "/a-d", "*.txt"
);
pb.directory(base);
System.exit(ejecutar(pb));'''),
    p("<b>/s</b> recorre; <b>/b</b> simplifica; <b>/a-d</b> solo archivos; <b>*.txt</b> filtra. El comodín lo interpreta DIR, no ProcessBuilder. Para Java, sustituye por *.java."),
    note("<b>Ensayo mínimo:</b> crea un .txt en la carpeta base y otro en una subcarpeta, además de un .csv. Deben salir los dos .txt, no el .csv. Si no hay coincidencias, DIR puede devolver un código de error."),
    p("En todos los casos, el padre aplica el helper ejecutar al ProcessBuilder que lanza Hijo y termina con su mismo código.", True)
)
page("t456", "10 · Talleres 04–06: operar con archivos",
    p("Trabaja en una <b>carpeta desechable</b> con entrada con espacios.txt. Los comandos siguientes son ejemplos para escribir en CMD, con nombres fijos. Cada línea debe ser una etapa separada en Java."),
    sub("04 / Crear entrega, copiar y listar"),
    code(r'''mkdir entrega
copy /y "entrada con espacios.txt" "entrega\copia.txt"
dir /b entrega''', "bat"),
    p("<b>Qué observar:</b> el original sigue existiendo y la copia conserva su contenido. Si entrega ya existe, omite su creación. No sigas hacia el listado si falla la copia."),
    sub("05 / Mover y después renombrar"),
    code(r'''mkdir archivo
move /y "entrada con espacios.txt" "archivo\temporal.txt"
ren "archivo\temporal.txt" final.txt
dir /b archivo''', "bat"),
    p("<b>Qué observar:</b> desaparece el origen; aparece archivo\\final.txt y ya no temporal.txt. REN recibe solo final.txt como segundo argumento. Antes de repetir, restaura los datos de prueba."),
    sub("06 / Mostrar contenido con TYPE"),
    code(r'''type "entrada con espacios.txt"''', "bat"),
    p("TYPE lee el archivo; tu Java lee la tubería y reenvía el texto. No sustituyas el comando por FileReader si se pide practicar procesos. Un archivo vacío puede terminar bien sin imprimir líneas."),
    sub("Ejemplo de una etapa CMD con nombre fijo y espacios"),
    code(r'''ProcessBuilder pb = new ProcessBuilder(
    "cmd", "/d", "/c",
    "copy /y \"entrada con espacios.txt\" \"entrega\\copia.txt\""
);
pb.directory(base);
int codigo = ejecutar(pb);
if (codigo != 0) {
    System.exit(codigo);
}'''),
    note("Aquí la cadena completa se entrega deliberadamente a CMD, que la interpreta. Esto no contradice separar argumentos para un .exe directo. No construyas esa cadena concatenando órdenes libres del usuario.")
)
page("t7", "11 · Taller 07: Java que lanza Java",
    note("<b>Cadena:</b> Padre JVM → Hijo JVM → Eco JVM. El texto viaja en sentido contrario: Eco → lector del hijo → stdout del hijo → lector del padre."),
    sub("Eco: responsabilidad mínima"),
    code(r'''// Eco.java sin package, para ejecutar en modo fuente.
public class Eco {
    public static void main(String[] args) {
        String nombre = args.length > 0 ? args[0] : "Ada";
        System.out.println("ECO:" + nombre);
    }
}'''),
    sub("Hijo: lanzar Eco y conservar el argumento"),
    code(r'''String bin = System.getProperty("java.home") + "/bin/";
String nombre = args.length > 0 ? args[0] : "Ada Lovelace";
ProcessBuilder pb = new ProcessBuilder(
    bin + "java.exe", "src/Eco.java", nombre
);
System.exit(ejecutar(pb));'''),
    p("El helper ejecutar es el del apartado 04 y debe estar dentro de la clase Hijo. Desde la raíz del taller, src/Eco.java debe existir. El padre repite la idea con src/Hijo.java."),
    sub("Variante compilada con paquetes"),
    code(r'''String cp = new File("bin").getAbsolutePath();
ProcessBuilder pb = new ProcessBuilder(
    bin + "java.exe", "-cp", cp,
    "estudio.t07.Eco", nombre
);'''),
    p("Compila antes las clases del taller. Si ejecutas desde CMD, un argumento con espacios necesita comillas; si lo pasas como nombre en ProcessBuilder, ya es un único elemento."),
    table(["Prueba", "Resultado esperado"], [
        ("Nombre Ada Lovelace", "ECO:Ada Lovelace completo, sin perder el apellido."),
        ("Sin argumento", "Se aplica el valor por defecto elegido."),
        ("Ruta errónea de Eco", "Aparece el error y el padre recibe un código no cero."),
    ], [175, 334]),
    p("No resuelve el taller llamar a Eco.main(): imprime, pero no crea la tercera JVM.", True)
)
page("t8", "12 · Taller 08: compilar, JAR y ejecutar",
    p("La carpeta recibida contiene <b>Saludo.java sin paquete</b>. Comprueba antes la existencia de java.exe, javac.exe y jar.exe. Ninguna de estas herramientas necesita cmd /c."),
    code(r'''// Archivo de prueba Saludo.java:
public class Saludo {
    public static void main(String[] args) {
        System.out.println("SALUDO:" + args[0]);
    }
}'''),
    sub("Las tres etapas y sus opciones"),
    table(["Orden", "Significado"], [
        ("javac -encoding UTF-8 -d clases Saludo.java", "Lee el fuente como UTF-8; -d clases coloca el bytecode en clases. --release 17 es opcional si se exige compatibilidad con Java 17."),
        ("jar cfe Aplicacion.jar Saludo -C clases .", "c crea; f indica archivo; e declara punto de entrada. Después: nombre del JAR y clase principal. -C clases . incluye el contenido de clases desde su raíz."),
        ("java -jar Aplicacion.jar Ada", "-jar ejecuta el JAR usando su manifiesto; Ada llega a args[0]."),
    ], [236, 273]),
    sub("ProcessBuilder de cada etapa, sin depender del PATH"),
    code(r'''ProcessBuilder compilar = new ProcessBuilder(
    bin + "javac.exe", "-encoding", "UTF-8",
    "-d", "clases", "Saludo.java");
ProcessBuilder empaquetar = new ProcessBuilder(
    bin + "jar.exe", "cfe", "Aplicacion.jar", "Saludo",
    "-C", "clases", ".");
ProcessBuilder lanzar = new ProcessBuilder(
    bin + "java.exe", "-jar", "Aplicacion.jar", "Ada");'''),
    p("Configura directory(base) en <b>cada builder</b>. Ejecuta y valida compilar antes de arrancar empaquetar; valida empaquetar antes de lanzar. Termina con el resultado de lanzar. Usa la secuencia del apartado 04."),
    note("<b>Prueba decisiva:</b> introduce un error de sintaxis. La compilación debe fallar y NO deben comenzar jar ni java. Usa una carpeta limpia para no confundir un JAR viejo con uno recién creado."),
    p("Si Saludo tiene package estudio, su clase principal será estudio.Saludo. Mantén -C clases . para conservar la estructura del paquete. Para inspeccionar: jar tf Aplicacion.jar (t lista, f archivo).", True)
)
page("t9", "13 · Taller 09: resolver cualquier cadena",
    sub("Primero, dibuja el contrato"),
    table(["Quién", "Recibe / hace / devuelve"], [
        ("Padre", "Recibe carpeta; lanza Hijo con esa ruta; lee sus mensajes; propaga el código."),
        ("Hijo", "Valida carpeta; lista; crea informe si hace falta; copia entrada a informe\\resultado.txt; muestra el contenido. Aborta al primer fallo."),
        ("CMD", "Realiza cada operación sobre la carpeta indicada. No modifica los archivos originales al copiar."),
    ], [95, 414]),
    sub("Plan antes del código"),
    code(r'''// Cada flecha exige: leer salida + waitFor + comprobar.
// 1. dir /b
// 2. mkdir informe        (solo si no existe)
// 3. copy /y "entrada con espacios.txt" "informe\resultado.txt"
// 4. type "informe\resultado.txt"'''),
    p("Para cada etapa, crea su ProcessBuilder, fija directory(base) y llama a ejecutar. Si devuelve distinto de cero, termina con ese código. No lances todos los procesos para esperarlos después: eso permite que se solapen."),
    sub("Existencia de carpetas sin ocultar errores"),
    code(r'''File informe = new File(base, "informe");
if (!informe.exists()) {
    ProcessBuilder crear =
        new ProcessBuilder("cmd", "/d", "/c", "mkdir", "informe");
    crear.directory(base);
    int codigo = ejecutar(crear);
    if (codigo != 0) System.exit(codigo);
}
if (!informe.isDirectory()) {
    throw new IOException("informe existe, pero no es carpeta");
}'''),
    sub("Qué constituye una evidencia"),
    p("<b>Nombres:</b> un listado permite comprobar ubicaciones. <b>Contenido:</b> compara texto o bytes según lo que se pida. <b>Resultado:</b> comprueba el código de salida. Un println(\"Correcto\") sin condición no demuestra ninguna operación."),
    note("<b>No añadas complejidad:</b> basta con procesos en serie y un helper. Evita &, tuberías y ejecución de cadenas arbitrarias. La entrada del usuario debe ser un dato o una opción validada, no una orden libre.")
)
page("fallos", "14 · Diagnóstico y repaso de examen",
    table(["Síntoma", "Qué comprobar primero"], [
        ("No aparece nada", "Fusiona stderr con true EN CADA NIVEL. Comprueba carpeta actual y que el fuente esté guardado."),
        ("Cannot run program dir", "Falta cmd /c. Ejecutar Java desde CMD no lo añade automáticamente."),
        ("java no se reconoce / where no encuentra", "Usa la ruta completa. No confundas PATH ausente con herramienta inexistente."),
        ("Paquete no coincide con ruta", "Alinea package y carpetas. Para modo fuente simple usa archivos sin package y un taller aislado."),
        ("No encuentra Hijo / clase principal", "Distingue fuente de clase. No uses -cp con un .java; en compilado usa raíz del classpath y nombre cualificado."),
        ("La copia falla pero padre devuelve 0", "El hijo debe esperar al comando y hacer System.exit(codigo). Repite la propagación en el padre."),
        ("No hay coincidencias", "Usa *.txt, no .txt. Mira errores de DIR y el código; puede ser un resultado sin coincidencias."),
        ("Se queda esperando", "Evita /k y ping -t; consume la salida; evita preguntas de sobrescritura; no esperes antes de leer una salida grande."),
        ("MOVE falla la segunda vez", "El primer intento consumió el origen. Prepara datos nuevos."),
        ("JAR falla o ejecuta versión antigua", "Verifica javac y jar, manifiesto/Main-Class, -C y orden de etapas. No sigas tras compilación fallida."),
    ], [185, 324]),
    sub("Checklist antes de entregar"),
    p("1. ¿Sé qué recibe cada main? 2. ¿Las rutas parten de la carpeta correcta? 3. ¿Arranco una vez por etapa? 4. ¿Leo hasta null y cierro el lector? 5. ¿Recojo errores en cada nivel? 6. ¿Espero, compruebo y propago el código? 7. ¿He probado un fallo y nombres con espacios?"),
    sub("Preguntas para responder sin mirar"),
    p("¿Proceso e hilo? ¿Listo y bloqueado? ¿Concurrencia y paralelismo? ¿Qué guarda el BCP? ¿Qué hace el planificador? ¿Por qué getInputStream lee stdout? ¿Qué hace realmente directory()? ¿Por qué el padre puede recibir 0 tras un copy fallido?"),
    p("<b>Base del material:</b> enunciados de talleres 01–09 y resúmenes teóricos de panel/data.js de este repositorio. Los ejemplos explicativos no sustituyen probarlos en el JDK y entorno del examen. Los PDF se pueden consultar sin Internet.", True)
)

def panel_questions():
    """Lee las mismas preguntas y respuestas que index.html muestra desde data.js."""
    data = (ROOT / "panel/data.js").read_text(encoding="utf-8")
    block = data.split("  theory: [", 1)[1].split("  exams:", 1)[0]
    quoted = r"'((?:\\.|[^'\\])*)'"
    questions = []
    def unquote(value):
        return value.replace("\\'", "'").replace("\\n", "\n").replace("\\\\", "\\")
    for line in block.splitlines():
        if not re.search(r"\{id:", line):
            continue
        fields = {}
        for field in ("id", "q", "model", "trap"):
            match = re.search(r"\b" + field + ":" + quoted, line)
            if not match:
                raise ValueError(f"Falta {field} en la pregunta: {line}")
            fields[field] = unquote(match.group(1))
        keys = re.search(r"\bkey:\[(.*?)\]", line)
        fields["key"] = [unquote(x) for x in re.findall(quoted, keys.group(1))]
        questions.append(fields)
    if len(questions) != 16:
        raise ValueError("Ha cambiado el número de preguntas del panel; revisar paginación.")
    return questions

questions = panel_questions()
question_sections = []
for group in range(4):
    first, last = group * 4, group * 4 + 4
    key = f"preguntas-{group+1}"
    title = f"02{chr(65+group)} · Preguntas del panel ({first+1}–{last})"
    content = [p("Mismo enunciado y respuesta del panel. Las claves ayudan a recordar; la advertencia señala el error típico.", True)]
    for number, question in enumerate(questions[first:last], first + 1):
        heading = f"{number:02d}. {question['q']}"
        content += [
            Anchor("pregunta-" + question["id"], question["q"], 1),
            Paragraph(html.escape(heading), styles["QuestionX"]),
            p(html.escape(question["model"])),
            p("<b>Claves:</b> " + " · ".join(html.escape(x) for x in question["key"]), True),
            p("<font color='#9b6100'><b>Evita este error:</b></font> " + html.escape(question["trap"]), True),
        ]
    question_sections.append((key, title, content))
sections[2:2] = question_sections

story = [
    Spacer(1, 55),
    p("<font color='#007f88'><b>DOCUMENTACIÓN CORTA DE REPASO</b></font>"),
    Paragraph("La guía maestra<br/>de procesos", styles["TitleX"]),
    Paragraph("Java + CMD · UT01", styles["SectionX"]),
    p("Del primer ProcessBuilder a una cadena completa de compilación, empaquetado y ejecución."),
    Spacer(1, 20),
    note("<b>Un patrón, nueve talleres.</b><br/>Configurar → arrancar → leer → esperar → comprobar → propagar."),
    Spacer(1, 20),
    sub("Qué encontrarás"),
    p("Teoría para el examen, comandos con sus opciones, argumentos, rutas sin PATH, fuente frente a clases y JAR, recetas por taller y diagnóstico de fallos."),
    sub("Cómo utilizarla mañana"),
    p("<b>Repaso rápido:</b> apartados 04, 06 y 08.<br/><b>Resolver un ejercicio:</b> apartados 09–13.<br/><b>Repasar teoría:</b> apartados 01–03 y preguntas 02A–02D.<br/><b>Si algo falla:</b> apartado 14."),
    Spacer(1, 28),
    p("Convenciones: Windows · Java 11+ para modo fuente · JDK 17+ recomendado para los talleres. Ejemplos fuente sin paquete salvo que se indique lo contrario."),
    p("Documento complementario: Chuleta_maestra_PSP.pdf, pensado como consulta inmediata.", True),
    PageBreak(),
    Paragraph("Índice navegable", styles["SectionX"]),
    p("Pulsa un título para saltar al apartado. El PDF también incluye marcadores laterales."),
]
for i, (key, title, content) in enumerate(sections, start=3):
    story.append(Paragraph(
        f'<link href="#{key}" color="#2862a6">{title}</link>'
        f' <font color="#526176"> · {i}</font>', styles["TextX"]))
    story.append(Spacer(1, 5))
story += [Spacer(1, 22), note("<b>Mapa mental:</b> el sistema operativo crea procesos. Java los configura y coordina. CMD interpreta sus órdenes internas. Las herramientas del JDK compilan, empaquetan y ejecutan.")]
for key, title, content in sections:
    story += [PageBreak(), Anchor(key, title),
              Paragraph(title, styles["SectionX"])] + content
build("Guia_maestra_PSP.pdf", story, "Guía maestra · Procesos Java y CMD")

quick = [
    Paragraph("Chuleta maestra", styles["TitleX"]),
    p("<b>1 / 4 · PATRÓN JAVA Y DATOS</b>"),
    code(r'''import java.io.*;

static int ejecutar(ProcessBuilder pb) throws Exception {
    pb.redirectErrorStream(true);
    Process p = pb.start();
    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(p.getInputStream()))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
    }
    return p.waitFor();
}'''),
    sub("Java sin PATH + argumento + carpeta"),
    code(r'''String bin = System.getProperty("java.home") + "/bin/";
String dato = args.length > 0 ? args[0] : ".";
ProcessBuilder pb = new ProcessBuilder(
    bin + "java.exe", "src/Hijo.java", dato);
System.exit(ejecutar(pb)); // Dentro de main.'''),
    p("<b>Carpeta de trabajo de un comando:</b> configura <font face='Code'>pb.directory(new File(dato).getAbsoluteFile());</font> antes de ejecutar(pb). Si cambias la carpeta, revisa las rutas relativas del comando."),
    p("<b>Args:</b> primer dato = args[0]; segundo = args[1]. Comprueba args.length. \"Ada Lovelace\" se pasa como un único elemento, no se divide por espacios."),
    table(["Recuerda", "Evita"], [
        ('bin + "java.exe"', 'new ProcessBuilder(bin, "java.exe", ...)'),
        ('"ping", "-n", "2", dato', '"ping -n 2" como ejecutable'),
        ('redirectErrorStream(true)', 'redirectErrorStream() solo consulta'),
        ('readLine() != null', 'Terminar al encontrar una línea vacía'),
        ('System.exit(codigo)', 'Acabar normalmente aunque el comando falle'),
    ], [240, 269]),
    note("Comprueba que existan java.exe, javac.exe y jar.exe en bin. El Extension Pack NO garantiza los tres. Usa rutas completas; si faltan, necesitas otro JDK. Este helper debe estar dentro de la clase."),
    PageBreak(),
    Paragraph("CMD + herramientas JDK", styles["SectionX"]),
    p("<b>2 / 4 · OPCIONES QUE DEBES RECORDAR</b>"),
    table(["Comando", "Qué hace"], [
        ('cmd /d /c orden', '/d sin AutoRun; /c ejecuta y sale. /k queda abierto.'),
        ('ping -n 2 localhost', '2 solicitudes; -w 1000: espera por respuesta; evita -t.'),
        ('dir /b', 'Listado simple.'),
        ('dir /s /b /a-d *.txt', 'Recursivo; simple; solo archivos; patrón TXT.'),
        ('mkdir entrega', 'Crea carpeta. Si existe, comprobar y omitir esa etapa.'),
        ('copy /y "origen.txt" "destino.txt"', 'Copia; conserva origen; sobrescribe sin preguntar.'),
        ('move /y "origen.txt" "destino.txt"', 'Traslada; consume el origen.'),
        ('ren "archivo\\temporal.txt" final.txt', 'Nuevo nombre sin ruta de destino.'),
        ('type "archivo.txt"', 'Muestra contenido de texto.'),
        ('dir /? · help dir', 'Ayuda offline CMD. Herramientas Java: --help.'),
        ('where ping · where java', 'CMD: busca ejecutables; where dir no encuentra el interno.'),
        ('which programa · type programa', 'Bash/Linux: which busca programas; type identifica también internos.'),
    ], [244, 265]),
    sub("Lanzamiento directo vs. CMD"),
    code(r'''new ProcessBuilder("cmd", "/d", "/c", "dir", "/b");
new ProcessBuilder("ping", "-n", "2", "localhost");
new ProcessBuilder(bin + "java.exe", "src/Hijo.java", dato);'''),
    p("<b>Internos:</b> dir, copy, move, ren, mkdir, type necesitan CMD. <b>Externos:</b> ping y Java no. Si where no encuentra nada, prueba help nombre: puede ser interno o faltar el PATH. En PowerShell usa where.exe."),
    sub("Compilar → empaquetar → ejecutar"),
    code(r'''javac -encoding UTF-8 -d clases Saludo.java
jar cfe Aplicacion.jar Saludo -C clases .
java -jar Aplicacion.jar Ada
jar tf Aplicacion.jar''', "bat"),
    p("<b>-d</b>: salida de clases; <b>c</b>: crear; <b>f</b>: archivo JAR; <b>e</b>: principal; <b>-C clases .</b>: incluir desde la raíz de clases; <b>t</b>: listar."),
    p("<b>Compilado:</b> java -cp bin estudio.t07.Hijo dato. El -cp apunta a bin; clase sin .class. <b>Fuente:</b> java src/Hijo.java dato (11+), sin -cp para indicar el fuente."),
    note("En CMD, entrecomilla rutas con espacios. En llamadas directas a .exe, cada argumento es un elemento, sin comillas añadidas. CMD interpreta &, |, &gt;, %, etc.: no le pases órdenes libres del usuario."),
    PageBreak(),
    Paragraph("Talleres + examen", styles["SectionX"]),
    p("<b>3 / 4 · MAPA DE RESOLUCIÓN Y DIAGNÓSTICO</b>"),
    table(["Taller", "Cadena que debes reconstruir"], [
        ("01 · Ping", "Padre pasa host → hijo ping -n 2 → salida y código hacia padre."),
        ("02 · Listar", "Padre pasa carpeta → directory(base) → cmd /c dir /b."),
        ("03 · Buscar", "Misma base → dir /s /b /a-d *.txt. .txt NO es un comodín."),
        ("04 · Copiar", "Crear entrega si falta → copy /y entrada a entrega\\copia.txt → dir. Conservar origen."),
        ("05 · Mover", "Crear archivo si falta → move a temporal.txt → ren a final.txt. Restaurar origen antes de repetir."),
        ("06 · Leer", "type entrada → hijo lee tubería → padre imprime contenido."),
        ("07 · Otra JVM", "Padre → Hijo → Eco. Argumento con espacios intacto. No llamar a Eco.main()."),
        ("08 · JAR", "javac → esperar y validar → jar → esperar y validar → java -jar."),
        ("09 · Integrador", "dir → mkdir informe → copy → type. Detenerse ante primer fallo."),
    ], [105, 404]),
    sub("Si algo falla"),
    p("<b>No sale nada:</b> redirectErrorStream(true) en cada nivel. <b>No arranca:</b> ejecutable/carpeta/ruta. <b>Paquete:</b> alinea package con ruta o usa fuente sin paquete. <b>Padre devuelve 0 tras fallo:</b> falta propagar con System.exit. <b>Se bloquea:</b> leer antes de esperar; evita /k, -t y preguntas interactivas."),
    note("<b>Regla final:</b> un start por etapa; leer hasta null; esperar; comprobar; propagar. No empezar la siguiente etapa si falló la anterior. getInputStream lee stdout; getOutputStream escribe stdin."),
    PageBreak(),
    Paragraph("Teoría de bolsillo", styles["SectionX"]),
    p("<b>4 / 4 · DEFINICIONES + RELACIONES PARA EL EXAMEN</b>"),
    table(["Concepto", "Cómo explicarlo con precisión"], [
        ("Programa / proceso", "El programa son instrucciones; el ejecutable es un archivo. Un proceso es una instancia en ejecución con memoria, recursos, PID y estado. Un mismo programa puede originar varios procesos."),
        ("Hilo / proceso", "El hilo ejecuta instrucciones dentro de un proceso. Tiene pila y registros propios; comparte memoria y recursos con los hilos del mismo proceso. Los procesos se comunican mediante mecanismos explícitos."),
        ("Concurrencia / paralelismo", "Concurrencia: tareas que progresan en intervalos solapados, incluso alternándose en un núcleo. Paralelismo: ejecución simultánea en varias unidades de procesamiento."),
        ("Ventajas y límites de hilos", "Suelen costar menos al crearse/cambiar de contexto; facilitan compartir datos y mantener respuesta. Exigen coordinar datos compartidos y no garantizan mayor velocidad."),
        ("BCP / cambio de contexto", "BCP: registro de control del SO (PID, estado, memoria, planificación, contexto, recursos). Cambio: guardar registros/contador/pila de una tarea y restaurar los de otra."),
        ("Swapping / suspensión", "Mover un proceso o parte de él entre RAM y almacenamiento. Suspendido no significa bloqueado: puede estar listo/suspendido o bloqueado/suspendido."),
    ], [136, 373]),
    sub("Estados: la diferencia que suelen preguntar"),
    p("<b>Nuevo → listo → ejecución → terminado.</b> Listo espera CPU; bloqueado espera un suceso (por ejemplo E/S). Ejecución → bloqueado al esperar datos; bloqueado → listo cuando llegan. Al agotar quantum puede pasar de ejecución a listo."),
    sub("Planificación y tipos"),
    p("<b>FCFS:</b> orden de llegada. <b>SJF:</b> ráfaga más corta; puede retrasar las largas. <b>Round Robin:</b> cola circular con quantum. La planificación busca equidad, uso de CPU, rendimiento y tiempos de respuesta razonables."),
    p("<b>Lotes:</b> importa el resultado final. <b>Interactivo:</b> comunicación continua con el usuario. <b>Tiempo real:</b> responder dentro de plazos, no simplemente ser rápido."),
    note("<b>Respuesta aplicada:</b> al llamar a waitFor(), el hilo llamador espera; el hijo sigue trabajando. Padre, Hijo y ping son tres procesos. Hijo.main() es solo una llamada a un método y no crea otra JVM."),
]
build("Chuleta_maestra_PSP.pdf", quick, "Chuleta maestra · PSP UT01")
print(f"PDF generados en {OUT}")
