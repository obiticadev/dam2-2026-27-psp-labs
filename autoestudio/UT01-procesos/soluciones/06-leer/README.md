# 06 · Recuperar el contenido

Referencia completa. Intenta resolver primero talleres/06-leer.

## Enunciado

Ejecuta TYPE para mostrar entrada con espacios.txt. El padre debe recibir el texto, no únicamente el código 0. No resuelvas la lectura con FileReader: el objetivo es capturar la salida de un comando.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t06.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t06.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh soluciones 06-leer`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd soluciones 06-leer`.

## Variante sin ayudas

Prueba un archivo vacío y uno inexistente. No confundas una línea vacía con el final del flujo.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/06-leer).
