# Autoestudio de PSP

Preparación de **UT01, procesos**, orientada al examen práctico en **Windows/CMD**
y a preguntas conceptuales de desarrollo breve. No modifica las prácticas del aula.

## Empieza aquí

Abre `UT01-procesos/panel/index.html` con tu navegador. Funciona directamente, sin
servidor, instalaciones, CDN ni conexión a Internet. Abre la carpeta
`UT01-procesos` en VS Code para programar. El dashboard explica toda la ruta.
En Windows también puedes hacer doble clic en `UT01-procesos/Abrir panel.cmd`.

También puedes usar un servidor local: `python -m http.server 8765` desde `panel`.

## Dos espacios distintos

- `talleres/`: tus intentos. Las plantillas compilan, pero lanzan un mensaje de
  ejercicio pendiente hasta que implementes los TODO.
- `soluciones/`: referencia completa. No se copia encima de tus intentos.
- `herramientas/`: verificador Java sin dependencias y lanzadores de CMD.
- `panel/`: teoría, fichas, animaciones paso a paso, respuestas y simulacros.

Requisito práctico: **JDK 17 o posterior**, con `java`, `javac` y `jar` en PATH.
VS Code: Extension Pack for Java. Selecciona **Command Prompt (CMD)** como terminal
en Windows. No pegar los ejemplos de CMD en Bash o PowerShell.

## Comprobar en Windows

Desde `UT01-procesos`, en CMD:

```bat
herramientas\comprobar.cmd soluciones
herramientas\comprobar.cmd talleres 02-directorios
```

El primero comprueba las referencias completas; el segundo tu taller concreto.
Sin segundo argumento se recorren todos. Las pruebas solo operan en carpetas
temporales creadas por el verificador; no borran ni mueven archivos personales.
Ping usa loopback; no se consulta YouTube ni se abre el navegador.

Consulta [procedencia, alcance y cobertura](UT01-procesos/FUENTES.md) y
[verificación y límites](UT01-procesos/VERIFICACION.md).

## Método

Primero ejecuta el comando en CMD, después tradúcelo a argumentos de ProcessBuilder.
Lee el enunciado antes de las pistas. Predice la salida. Resuelve la variante sin
copiar. Un test verde no demuestra por sí solo que sabes reconstruir el patrón.
El progreso del panel es autoevaluación, no una calificación ni una lectura de VS Code.
