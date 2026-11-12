# 07 · Java que lanza Java

Tu espacio de trabajo. Las soluciones están fuera de esta carpeta.

## Enunciado

El padre lanza Hijo y este lanza Eco como una tercera JVM. Eco recibe un nombre, imprime ECO:nombre y termina. Conserva correctamente un argumento como Ada Lovelace. No llames directamente a Eco.main.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t07.Padre "Ada"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t07.Padre "Ada"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh talleres 07-otro-java`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd talleres 07-otro-java`.

## Variante sin ayudas

Crea un Eco.java sin paquete y ejecútalo como fuente con java Eco.java. Después compáralo con java -cp bin estudio.t07.Eco. El modo fuente requiere Java 11 o posterior.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/07-otro-java).
