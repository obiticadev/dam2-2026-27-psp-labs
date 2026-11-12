# 01 · Diagnóstico de conexión

Tu espacio de trabajo. Las soluciones están fuera de esta carpeta.

## Enunciado

El padre recibe un destino y se lo pasa al hijo. El hijo ejecuta dos solicitudes ICMP, lee la salida y propaga el código de retorno. No se evalúa el texto localizado de ping. Un host puede bloquear ICMP sin estar caído.

## Preparación y ejecución (CMD, Windows)

Abre esta carpeta en VS Code. Los comandos parten de **esta carpeta**, no de src.
Prepara datos con el verificador o una carpeta desechable propia. Para los talleres
de archivos debe existir `entrada con espacios.txt`; para JAR, `Saludo.java`.

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\*.java
java -cp bin estudio.t01.Padre "localhost"
```

En Linux (ensayo en casa), desde esta misma carpeta:

```sh
mkdir -p bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin estudio.t01.Padre "localhost"
```

Verificación Linux desde UT01-procesos: `./herramientas/comprobar.sh talleres 01-ping`.

Si falla javac, no ejecutes clases antiguas de bin. Puedes comprobar todo desde
la raíz UT01-procesos con `herramientas\comprobar.cmd talleres 01-ping`.

## Variante sin ayudas

Cambia el número de solicitudes a cuatro. Distingue un destino que no responde de un comando que no puede arrancar.

## Criterio de dominio

Explica quién crea cada proceso, qué directorio usa el comando, qué flujo se lee,
por qué termina el bucle y qué ocurre si el código no es cero. Reconstruye ambos
main sin consultar la referencia. No añadas concurrencia con hilos.

## Teoría y animación

[Abrir el taller en el panel](../../panel/index.html#taller/01-ping).
