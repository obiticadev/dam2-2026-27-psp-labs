# 08 · Compilar, empaquetar y ejecutar

Referencia completa. Intenta resolver primero talleres/08-crear-jar.

## Enunciado

En la carpeta recibida hay un Saludo.java sin paquete. El hijo lo compila a clases, crea Aplicacion.jar con Saludo como clase principal y lo ejecuta con Ada como argumento. Si una etapa falla, no debe empezar la siguiente. No uses Maven para ocultar los comandos.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t08.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t08.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh soluciones 08-crear-jar`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd soluciones 08-crear-jar`.

## Variante sin ayudas

Introduce un error de sintaxis y comprueba que se detiene antes de jar. Después mueve Saludo a un paquete y adapta clase principal, fuente y ejecución.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/08-crear-jar).
