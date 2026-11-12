# Verificación y límites

Fecha de elaboración: 2026-11-12. Entorno: Linux, JDK local con `--release 17`.

## Java: pruebas ejecutadas

Las soluciones tienen dos ramas (`if (WINDOWS)`): CMD para el examen y
ping -c/ls/find/cp/mv/cat para ensayar en Linux. Cada sistema certifica solo su rama.

`./herramientas/comprobar.sh soluciones` (Linux, 2026-11-12): **9 PASS, 0 FAIL**,
con procesos reales, incluidas las pruebas negativas (carpeta inexistente, origen
ausente, error de compilación sin JAR posterior).

`./herramientas/comprobar.sh talleres`: las nueve plantillas compilan y fallan con
`UnsupportedOperationException`, como se espera de ejercicios sin resolver.

Las pruebas trabajan en carpetas nuevas bajo `target/verificacion`; conservan
salidas y un resultado.txt. No se han borrado ni movido archivos del usuario.
Hay timeout de 30 segundos por proceso de prueba y limpieza de sus descendientes
si se agota; no se aplica a procesos ajenos.

## Panel

Cambios del 2026-11-12: escena con tres procesos (estado y PID ilustrativos),
consola por proceso, tuberías animadas, carpeta de trabajo que cambia, código
Padre/Hijo con resaltado y escenario «con error»; 27 ejercicios añadidos; selector
Windows/Linux; modo examen con preparación, concentración, entrega, nota
autoevaluada e historial. `herramientas/probar-panel.cjs` cubre todo lo anterior y
pasa sin errores JS ni desbordamiento a 1440 y 390 px.

Historial anterior:

Comprobación sintáctica con `node --check` sobre data.js, scenes.js y app.js.
Pruebas de navegador mediante Chromium/Playwright sobre file://; script reproducible
en `herramientas/probar-panel.cjs`. Evidencias bajo `.impeccable/review` en la raíz
del repositorio. El informe JSON registra las rutas y comprobaciones que pasan.

Resultado ejecutado: 43 rutas por cada ancho (1440 y 390 píxeles), sin errores
JavaScript ni desbordamiento horizontal. Se recorrieron los pasos de las nueve
escenas; se probaron persistencia, respuestas desplegables, autoevaluación,
búsqueda y estado vacío, reloj y finalización, transición bloqueado→listo y enlaces
locales. Las capturas incluyen inicio de escritorio/móvil, JAR y pregunta de hilo.
El detector de Impeccable señaló etiquetas demasiado pequeñas; se elevaron los
tamaños secundarios y se repitió la prueba y la captura. No es una auditoría WCAG
completa ni certifica las voces o pantalla completa en todos los navegadores.

Prueba adicional ejecutada: abrir/cerrar navegación móvil al cambiar de página,
reproducción automática de un paso y pausa. Revisión visual independiente de las
cuatro capturas: **ship**, sin correcciones materiales; el dictamen se refiere al
dashboard, no certifica ejecución Windows. Preservar la distinción simulación/prueba.

Las escenas son simulaciones interactivas, no una ejecución de Java ni vídeos MP4.
No se requiere red. Lectura en voz alta y pantalla completa dependen del navegador;
la lectura tiene control de activación y no se reproduce automáticamente.

## Comprobación pendiente en el equipo Windows

Desde `UT01-procesos`, abrir CMD y ejecutar:

```bat
herramientas\comprobar.cmd soluciones
```

Resultado esperado en Windows: nueve talleres correctos y cero fallidos. Es la única forma de certificar la rama CMD. Si no se
cumple, conservar `target/verificacion/.../resultado.txt` y los proceso-*.txt para
diagnosticar. Comprobar especialmente las rutas con espacios en COPY/MOVE/REN,
la página de códigos de la consola y la disponibilidad del JDK en PATH.

Las pruebas usan texto ASCII para no confundir codificación de CMD con el patrón
de procesos. El lector usa la codificación por defecto de la JVM; si se practica
texto con tildes debe acordarse la codificación del comando y del lector.

## Fuera de alcance

No se ha ejecutado un examen real ni verificado la configuración del aula Windows.
No se han cambiado Obsidian, prácticas existentes, configuración global de VS Code,
remotos Git, commits ni publicaciones. El progreso del navegador es autoevaluación.
