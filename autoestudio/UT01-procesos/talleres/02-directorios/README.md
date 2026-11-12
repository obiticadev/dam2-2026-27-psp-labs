# 02 · Explorar directorios

Tu espacio de trabajo. Las soluciones están fuera de esta carpeta.

## Enunciado

El padre recibe una carpeta. El hijo la establece como directorio de trabajo y ejecuta dir /b. La salida atraviesa dos lectores: CMD → hijo y después hijo → padre. No cambies el directorio global del equipo.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t02.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t02.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh talleres 02-directorios`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd talleres 02-directorios`.

## Variante sin ayudas

Muestra subdirectorios con /s. Explica la diferencia entre una ruta pasada como argumento y directory().

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/02-directorios).
