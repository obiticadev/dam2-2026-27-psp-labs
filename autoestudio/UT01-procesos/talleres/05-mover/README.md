# 05 · Mover y renombrar

Tu espacio de trabajo. Las soluciones están fuera de esta carpeta.

## Enunciado

Mueve entrada con espacios.txt a archivo\temporal.txt y renómbralo a final.txt. El archivo deja de estar en el origen, pero conserva los bytes. REN recibe un nuevo nombre, no otra ruta de destino.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t05.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t05.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh talleres 05-mover`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd talleres 05-mover`.

## Variante sin ayudas

Explica qué ocurriría si se repite la operación. Prepara datos nuevos antes de cada intento: mover consume el archivo original.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/05-mover).
