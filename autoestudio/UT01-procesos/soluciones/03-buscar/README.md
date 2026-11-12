# 03 · Buscar archivos

Referencia completa. Intenta resolver primero talleres/03-buscar.

## Enunciado

Recibe una carpeta y lista sus archivos .txt, también en subcarpetas. No uses Files.walk: se está practicando CMD. El orden de salida no es un contrato. Si no hay coincidencias, dir puede devolver error.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t03.Padre "C:\PSP\zona de pruebas"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t03.Padre "$HOME/psp/zona de pruebas"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh soluciones 03-buscar`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd soluciones 03-buscar`.

## Variante sin ayudas

Busca .java en lugar de .txt. Predice qué cambia al quitar /s o /b.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/03-buscar).
