# 09 · Centro de operaciones

Tu espacio de trabajo. Las soluciones están fuera de esta carpeta.

## Enunciado

Sin plantilla guiada: el padre recibe la carpeta de trabajo, lanza al hijo y recupera sus mensajes. El hijo lista la carpeta, crea informe, copia el archivo de entrada a resultado.txt y muestra su contenido. Debe abortar ante el primer fallo. No cambies los archivos originales.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t09.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t09.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh talleres 09-integrador`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd talleres 09-integrador`.

## Variante sin ayudas

Sustituye la copia por la compilación y ejecución de un JAR. Haz que la selección de operación se decida con argumentos validados, nunca ejecutando una cadena arbitraria de CMD.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/09-integrador).
