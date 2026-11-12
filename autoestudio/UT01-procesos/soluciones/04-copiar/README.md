# 04 · Preparar una entrega

Referencia completa. Intenta resolver primero talleres/04-copiar.

## Enunciado

En una carpeta de pruebas con entrada con espacios.txt, crea entrega, copia el archivo a entrega\copia.txt y muestra la carpeta. El origen debe conservarse. /Y permite reemplazar solo la copia generada en el taller.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t04.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t04.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh soluciones 04-copiar`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd soluciones 04-copiar`.

## Variante sin ayudas

Quita el archivo de origen y comprueba que no aparece un mensaje falso de éxito. Explica por qué mkdir se condiciona si la carpeta ya existe.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/04-copiar).
