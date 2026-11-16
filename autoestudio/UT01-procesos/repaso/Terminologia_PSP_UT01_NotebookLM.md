# PSP UT01: vocabulario explicado para NotebookLM

## 1. Propósito y modo de estudio

Este documento reorganiza la unidad de procesos como un glosario razonado. Su objetivo es comprender la definición, la utilidad y las diferencias entre términos, no memorizar una secuencia de código. El alcance es UT01: procesos, comparación conceptual con hilos, gestión del sistema operativo y ejecución de procesos con Java en Windows.

Cada entrada distingue definición, uso y contraste. Los ejemplos son ilustrativos; no son preguntas oficiales. Los nombres de métodos se incluyen para reconocer su función, no para leer sus signos carácter a carácter en un podcast.

La base es el banco de teoría de panel/data.js, el alcance de FUENTES.md y los nueve talleres del repositorio. El vocabulario práctico complementa la teoría; no se presenta como una transcripción literal de todo el manual del módulo. No se desarrolla programación multihilo, redes ni criptografía.

Para una explicación oral útil: nombrar el término, definirlo, explicar para qué sirve, ofrecer un ejemplo y distinguirlo del concepto vecino. Una analogía ayuda a recordar, pero no sustituye la definición técnica.

## 2. Programa, proceso y recursos

### Programa

Definición: conjunto de instrucciones que expresa una tarea.

Uso: describe qué operaciones deben realizarse. Un programa puede almacenarse sin estar ejecutándose.

Distinción: programa es la descripción; proceso es una instancia activa de esa descripción. Compilar un programa no equivale a ejecutarlo.

### Archivo ejecutable

Definición: archivo preparado para iniciar la ejecución de un programa, directamente por el sistema o a través de un entorno de ejecución.

Uso: permite poner en marcha una aplicación. java.exe es un ejecutable; un JAR puede contener una aplicación ejecutable mediante Java.

Distinción: un archivo no tiene por sí mismo un PID ni un estado de ejecución. El mismo ejecutable puede originar varios procesos. No todo archivo con código es un ejecutable nativo.

### Proceso

Definición: instancia de un programa en ejecución, con un entorno, espacio de memoria y recursos asignados.

Uso: es una unidad que el sistema operativo administra, identifica y controla. Contiene uno o más hilos que ejecutan instrucciones.

Distinción: no es el archivo guardado. Si Padre arranca otra JVM para ejecutar Hijo, hay otro proceso; si solo llama a un método de otra clase, no crea por ello otro proceso.

### PID

Definición: identificador numérico que el sistema operativo asigna a un proceso.

Uso: permite referirse a una ejecución concreta, por ejemplo al inspeccionarla o gestionarla.

Distinción: no es el nombre del programa, la clase principal ni el código de salida. Un PID puede reutilizarse después de que termine el proceso.

### Sistema operativo

Definición: software que administra el hardware y ofrece servicios para ejecutar programas y gestionar sus recursos.

Uso: crea y controla procesos, gestiona memoria, archivos y dispositivos, y organiza la ejecución.

Distinción: Java puede solicitar la creación de un proceso, pero no sustituye al sistema operativo. CMD es un intérprete que se ejecuta sobre Windows, no el sistema operativo completo.

### CPU y núcleo

Definición: la CPU ejecuta instrucciones; un núcleo es una unidad de procesamiento dentro de un procesador.

Uso: varias unidades de procesamiento permiten ejecutar trabajo realmente en paralelo.

Distinción: tener varios programas abiertos no significa que todos estén usando CPU simultáneamente. Pueden estar listos o bloqueados.

### Espacio de direcciones y recursos

Definición: el espacio de direcciones es el conjunto de direcciones de memoria accesibles en el contexto de un proceso. Sus recursos incluyen elementos como archivos abiertos y conexiones.

Uso: proporcionan el entorno para ejecutar el programa y mantener sus datos.

Distinción: los procesos normalmente tienen espacios de direcciones separados. Esto no impide comunicarse ni compartir memoria mediante mecanismos explícitos. Tener variables con igual nombre en dos JVM no las convierte en la misma variable.

### Padre e hijo

Definición: nombres relativos de la relación entre un proceso creador y otro creado por él.

Uso: permiten describir quién inicia, observa y espera a quién.

Distinción: no son palabras reservadas de Java. En Padre → Hijo → CMD, Hijo es hijo del primero y padre del tercero. El Padre no recibe automáticamente toda la información del nieto.

## 3. Hilos y formas de ejecución

### Hilo o subproceso

Definición: flujo de control secuencial dentro de un proceso, con registros, contador de programa y pila propios.

Uso: permite organizar varios flujos de ejecución en una aplicación. Los hilos del mismo proceso comparten su entorno, código y memoria.

Distinción: independencia del flujo no significa existencia fuera del proceso. Un hilo no es otra JVM. Su creación y cambio de contexto suelen ser menos costosos que los de procesos independientes, pero no garantizan aceleración.

### Registros, contador de programa y pila

Definición: los registros guardan valores de trabajo de la CPU; el contador de programa señala la posición de ejecución; la pila conserva información de llamadas y datos asociados a ellas.

Uso: forman parte del estado necesario para continuar un flujo donde se detuvo.

Distinción: los hilos pueden compartir datos del proceso sin compartir una única pila o un único contador de programa. Compartir memoria no significa que todo su estado sea idéntico.

### Concurrencia

Definición: progreso de varias tareas durante intervalos temporales que se solapan.

Uso: permite organizar varias actividades, incluso cuando unas esperan entrada/salida.

Distinción: puede existir con un único núcleo mediante alternancia; no exige simultaneidad real. Dos procesos concurrentes no están necesariamente ejecutando instrucciones al mismo instante.

### Paralelismo

Definición: ejecución realmente simultánea de varias tareas en distintas unidades de procesamiento.

Uso: puede reducir el tiempo de trabajos que admiten reparto y aprovechar varios núcleos.

Distinción: no aparece automáticamente por crear varios hilos. Depende del hardware, del trabajo disponible y de sus dependencias.

### Ejecución secuencial

Definición: realización de etapas en un orden en el que una depende de que termine la anterior.

Uso: compilar, empaquetar y ejecutar un JAR debe respetar ese orden.

Distinción: usar varios procesos no obliga a ejecutarlos en paralelo. Arrancar el empaquetador antes de terminar la compilación rompe una dependencia.

### Distribución

Definición: organización de componentes en diferentes equipos que se comunican.

Uso: permite repartir trabajo o servicios entre máquinas.

Distinción: no es sinónimo de concurrencia ni de paralelismo. Padre e Hijo ejecutados localmente no forman, por ese hecho, un sistema distribuido. Se incluye solo la distinción conceptual.

### Condición de carrera y sincronización

Definición: una condición de carrera aparece cuando el resultado depende del orden o momento de accesos concurrentes no coordinados. Sincronizar consiste en coordinar esos accesos o el orden de las tareas.

Uso: evita resultados incoherentes al compartir información.

Distinción: compartir memoria facilita la comunicación entre hilos, pero no la vuelve automáticamente segura. Aquí se estudia el concepto, no las técnicas de programación multihilo.

## 4. Estados del proceso

### Nuevo

Definición: estado de un proceso en creación, todavía no admitido a la ejecución normal en el modelo de cinco estados.

Uso: distingue la preparación inicial de un proceso listo para competir por CPU.

Distinción: nuevo no significa simplemente que el programa sea reciente ni que aún no se haya abierto su archivo en el editor.

### Listo

Definición: estado de un proceso que puede continuar, pero espera que se le asigne CPU.

Uso: identifica los candidatos entre los que decide el planificador.

Distinción: no espera datos de disco; espera procesador. Esa es la diferencia central con bloqueado.

### En ejecución

Definición: estado de una tarea que está utilizando una unidad de procesamiento.

Uso: indica que está ejecutando instrucciones en ese momento.

Distinción: que una aplicación esté abierta no implica que permanezca siempre en este estado.

### Bloqueado

Definición: estado de espera de un suceso necesario para continuar, como la llegada de datos o la finalización de una operación.

Uso: permite que otras tareas aprovechen la CPU mientras la primera no puede avanzar.

Distinción: asignarle CPU no resuelve por sí solo el suceso pendiente. Cuando este ocurre, pasa a listo; no necesariamente directamente a ejecución.

### Terminado

Definición: estado de un proceso que ha finalizado o ha sido retirado.

Uso: marca el final de su ejecución.

Distinción: terminado no significa correcto. Puede acabar con éxito o con error; para distinguirlo se consulta su resultado.

### Transición de estado

Definición: cambio de un estado a otro por un acontecimiento de ejecución o gestión.

Uso: explica el ciclo de vida. La asignación de CPU lleva de listo a ejecución; una espera lleva de ejecución a bloqueado; completar esa espera lleva a listo.

Distinción: retirar la CPU porque se acaba un turno lleva normalmente de ejecución a listo, no a bloqueado.

### Suspendido

Definición: en el modelo ampliado del tema, situación de un proceso fuera de memoria principal, pendiente de ser reactivado o devuelto a memoria para ejecutar.

Uso: permite gestionar la presión de memoria y distinguir listo/suspendido de bloqueado/suspendido.

Distinción: suspensión y bloqueo son ejes diferentes. Si llega el suceso esperado mientras sigue fuera de memoria, puede pasar de bloqueado/suspendido a listo/suspendido.

## 5. Control, contexto y memoria

### BCP o PCB

Definición: Bloque de Control de Proceso, registro del sistema operativo con la información necesaria para administrar un proceso.

Uso: reúne identificación, estado, información de memoria y planificación, recursos y contexto de ejecución, según la organización del sistema.

Distinción: PID es un identificador; BCP es una estructura con muchos datos. No es simplemente una lista de nombres de programas.

### Contexto de ejecución

Definición: estado que debe conservarse para poder reanudar una tarea, incluyendo su posición de ejecución y registros.

Uso: permite continuar correctamente después de una interrupción o cambio de tarea.

Distinción: no equivale al texto fuente ni a una copia completa del programa.

### Cambio de contexto

Definición: operación que guarda el estado de la tarea saliente y restaura el de otra entrante.

Uso: permite alternar tareas sin que pierdan el punto donde estaban.

Distinción: no es swapping. Tiene coste temporal; durante ese trabajo de gestión no se ejecutan instrucciones útiles de las tareas implicadas.

### RAM y almacenamiento de apoyo

Definición: RAM es memoria principal de trabajo. El almacenamiento de apoyo, como un disco, conserva datos y puede respaldar mecanismos de memoria.

Uso: la RAM sostiene la ejecución activa; el almacenamiento permite conservar información y apoyar la gestión de memoria.

Distinción: guardar un archivo fuente en disco no es lo mismo que tener un proceso ejecutándose en memoria.

### Swapping o intercambio

Definición: traslado de un proceso o parte de él entre memoria principal y almacenamiento de apoyo, según el modelo explicado en el tema.

Uso: libera memoria principal cuando resulta necesario.

Distinción: intercambiar ubicación en memoria no es elegir qué tarea obtiene CPU. Swapping trata de residencia; cambio de contexto, de conservar y restaurar ejecución.

### Cargador

Definición: componente que participa en preparar en memoria lo necesario para ejecutar un programa.

Uso: interviene en la puesta en marcha del entorno de ejecución.

Distinción: no debe confundirse con el planificador, que decide qué tarea lista usa la CPU.

## 6. Planificación y medidas

### Planificador

Definición: componente del sistema operativo que selecciona tareas listas para utilizar el procesador.

Uso: busca equidad, aprovechamiento de CPU, buen rendimiento y tiempos de respuesta y retorno adecuados.

Distinción: no crea por sí mismo el código del programa ni garantiza que todas las tareas ejecuten simultáneamente.

### Cola de listos

Definición: estructura lógica que organiza las tareas disponibles para ejecutar.

Uso: proporciona los candidatos a la política de planificación.

Distinción: no incluye como candidatos ejecutables a quienes siguen bloqueados por un suceso.

### Política expulsiva y no expulsiva

Definición: una política expulsiva permite retirar la CPU a la tarea actual; una no expulsiva espera a que termine su ráfaga, se bloquee o ceda según el modelo.

Uso: determina cuándo puede producirse un cambio de tarea por decisión de planificación.

Distinción: expulsar de la CPU no significa matar el proceso. Puede volver a listo y continuar más tarde.

### Ráfaga de CPU

Definición: intervalo de trabajo de CPU antes de una espera u otro final de esa fase de cómputo.

Uso: permite razonar sobre duración de trabajo y algoritmos como SJF.

Distinción: no es necesariamente toda la vida del proceso; puede alternar varias ráfagas con entrada/salida.

### FCFS

Definición: First Come, First Served, política que atiende por orden de llegada.

Uso: ofrece una organización sencilla. En su forma no expulsiva deja terminar la ráfaga actual.

Distinción: no elige la duración menor. Una tarea larga delante puede retrasar a otras cortas.

### SJF y SRTF

Definición: SJF, Shortest Job First, selecciona la ráfaga más corta entre las disponibles. SRTF es la variante expulsiva que compara tiempo restante.

Uso: favorecen trabajos cortos; requieren conocer o estimar duraciones.

Distinción: SJF no expulsivo no retira obligatoriamente la CPU al llegar un trabajo más corto. Los largos pueden sufrir esperas excesivas.

### Round Robin y quantum

Definición: Round Robin reparte turnos mediante una cola circular. Quantum es el intervalo máximo de CPU concedido en un turno.

Uso: reparte oportunidades de ejecución. Si la tarea agota el quantum y sigue lista para trabajar, vuelve a la cola.

Distinción: quantum no es la duración total del proceso. Puede bloquearse o terminar antes de consumirlo. Un quantum pequeño aumenta cambios de contexto; uno grande puede aproximar el comportamiento a FCFS.

### Prioridad e inanición

Definición: prioridad es una preferencia aplicada por una política de planificación. Inanición es el aplazamiento indefinido de una tarea que no consigue acceso al recurso necesario.

Uso: la prioridad diferencia necesidades de servicio; reconocer la inanición permite evaluar la equidad.

Distinción: una espera larga no demuestra por sí sola inanición. Tampoco prioridad es sinónimo de orden de llegada.

### Tiempo de respuesta, retorno y espera

Definición: en el modelo de ejercicios, respuesta inicial es el tiempo entre llegada y primera ejecución; retorno es el tiempo entre llegada y finalización; espera es el tiempo acumulado en la cola de listos.

Uso: permiten comparar políticas desde perspectivas diferentes.

Distinción: respuesta no es finalización. Espera en listos no incluye toda la espera de entrada/salida. Solo en modelos simplificados sin otras fases puede calcularse espera como retorno menos CPU.

### Rendimiento y utilización

Definición: rendimiento o throughput es la cantidad de trabajos completados por unidad de tiempo. Utilización de CPU es la proporción de tiempo en que el procesador permanece ocupado.

Uso: miden productividad y aprovechamiento.

Distinción: una CPU muy ocupada no garantiza buena respuesta al usuario ni muchos trabajos útiles completados; puede haber costes de gestión.

## 7. Tipos de procesos

### Por lotes

Definición: trabajo preparado con sus datos iniciales y orientado a obtener un resultado sin interacción continua.

Uso: compilaciones automáticas o tratamientos de archivos.

Distinción: no significa necesariamente lento ni escrito en un lenguaje particular.

### Interactivo

Definición: proceso que mantiene intercambio con el usuario y responde a sus acciones durante la ejecución.

Uso: editores o aplicaciones con entrada continua.

Distinción: importa la capacidad de respuesta, no solo completar el trabajo total.

### Tiempo real y plazo

Definición: en tiempo real, la corrección exige responder dentro de plazos definidos por el sistema. El plazo es el límite temporal exigido.

Uso: tareas con requisitos temporales.

Distinción: tiempo real no significa simplemente rápido en promedio. Una respuesta tardía puede incumplir el requisito aunque su contenido sea correcto.

## 8. Creación y control de procesos en Java

### JVM, entorno de ejecución y JDK

Definición: JVM es la máquina virtual que ejecuta bytecode Java. El entorno de ejecución proporciona lo necesario para ejecutar aplicaciones. JDK es el conjunto de desarrollo que incorpora herramientas como el compilador.

Uso: la JVM ejecuta; el JDK permite además desarrollar, compilar y empaquetar.

Distinción: poder ejecutar java no garantiza disponer de javac y jar en todas las instalaciones o empaquetados. Otra ejecución de java suele crear otra JVM en otro proceso.

### Runtime.exec

Definición: API de Java que permite iniciar procesos externos.

Uso: es otra forma de solicitar una ejecución al sistema operativo.

Distinción: no es un intérprete de órdenes automático. En estas prácticas se prefiere ProcessBuilder por hacer explícita la configuración de comando, entorno y redirecciones.

### ProcessBuilder

Definición: objeto que configura cómo iniciar un proceso: ejecutable, argumentos, directorio, entorno y redirecciones.

Uso: prepara la solicitud antes de ejecutar.

Distinción: construirlo no arranca nada. Modificarlo después no reconfigura retroactivamente un proceso ya iniciado.

### start y Process

Definición: start solicita crear el proceso y devuelve un objeto Process que representa esa ejecución.

Uso: Process permite acceder a flujos, esperar y consultar el resultado.

Distinción: builder es configuración; Process es la ejecución representada. Cada nueva llamada a start inicia otra ejecución, no vuelve a conectar con la anterior.

### waitFor

Definición: método que hace esperar al hilo llamador hasta que termina el proceso representado y devuelve su código de salida.

Uso: respeta dependencias y recoge el resultado antes de continuar.

Distinción: no detiene al Hijo, no imprime su texto y no consume sus tuberías. En una aplicación con varios hilos no implica detenerlos todos.

### exitValue

Definición: consulta el código de un proceso terminado sin esperar.

Uso: obtiene el resultado cuando ya se sabe que finalizó.

Distinción: si sigue activo, lanza IllegalThreadStateException. waitFor espera; exitValue no.

### Código de salida

Definición: número con el que un proceso comunica cómo terminó.

Uso: decidir si continuar una cadena. Cero suele representar éxito y un valor no nulo un fallo, según el contrato de la herramienta.

Distinción: no es PID ni texto impreso. Un proceso puede imprimir un mensaje y aun así devolver fallo, o imprimir un error y terminar indebidamente con cero.

### System.exit y propagación

Definición: System.exit solicita terminar la JVM actual con el código indicado. Propagar el fallo es devolver al creador el resultado que debe observar.

Uso: si CMD falla, Hijo puede devolver ese mismo código, y Padre puede devolver el recibido de Hijo.

Distinción: System.exit no convierte cero en error; conserva el valor indicado. Imprimir el código no lo propaga. Tras una etapa intermedia exit solo debe usarse si hay que terminar; de lo contrario impediría continuar incluso con éxito.

### Excepción y fallo de comando

Definición: una excepción informa de una situación excepcional en Java; un código no nulo informa de cómo declaró terminar un proceso iniciado.

Uso: distinguir un fallo al arrancar de un fallo en la operación externa.

Distinción: start puede lanzar IOException porque no existe el ejecutable, sin que ese comando llegue a ejecutarse. Un javac que encuentra un error de sintaxis sí arrancó, pero termina con fallo.

## 9. Argumentos, directorios y entorno

### Argumento y args

Definición: argumento es un dato inicial entregado al programa. En main, String[] args contiene los argumentos de la aplicación.

Uso: pasar una carpeta o un nombre sin fijarlos en el código. args.length indica la cantidad y args[0] el primero.

Distinción: nombre del ejecutable y clase lanzada no forman parte de args. Ada Lovelace debe enviarse como un único elemento del ProcessBuilder si es un único dato; no se concatena con el ejecutable.

### Ruta relativa y absoluta

Definición: una ruta relativa se interpreta desde una base; una absoluta identifica una ubicación sin depender del directorio actual.

Uso: localizar fuentes, ejecutables y resultados.

Distinción: src/Hijo.java se busca desde el directorio de trabajo, no desde la carpeta donde vive Padre.java. Una barra inicial puede cambiar la raíz desde la que se busca; no es equivalente a una ruta relativa sin esa barra.

### Directorio de trabajo y directory

Definición: directorio de trabajo es la base de las rutas relativas de un proceso. ProcessBuilder.directory lo configura para el proceso que se creará.

Uso: si se fija pruebas, javac encuentra Saludo.java en pruebas y guarda -d clases en pruebas/clases.

Distinción: no crea carpetas ni cambia la carpeta del Padre o de la terminal. Es análogo a cambiar de carpeta antes de lanzar el comando, pero solo para esa nueva ejecución.

### File

Definición: objeto Java que representa una ruta de archivo o directorio.

Uso: suministrar una carpeta a directory o resolver una ruta absoluta.

Distinción: new File no crea por sí mismo el archivo, la carpeta ni un proceso.

### Entorno y PATH

Definición: el entorno es un conjunto de variables asociado al proceso. PATH enumera carpetas donde se buscan ejecutables, según las reglas del sistema y del lanzador.

Uso: localizar herramientas sin escribir toda su ruta.

Distinción: PATH no es classpath. Que el editor encuentre Java no garantiza que CMD lo encuentre ni que otra forma de lanzamiento use exactamente la misma búsqueda.

### java.home y user.dir

Definición: java.home es una propiedad de la JVM que indica su instalación de Java; user.dir indica su directorio de trabajo inicial habitual.

Uso: java.home permite construir rutas como bin/java.exe; user.dir ayuda a diagnosticar rutas relativas.

Distinción: java.home no es la carpeta del proyecto ni instala javac o jar ausentes. Cambiar una propiedad no equivale a ejecutar cd ni a configurar directory de un hijo.

## 10. Flujos y comunicación

### Comunicación entre procesos y tubería

Definición: comunicación entre procesos es intercambio explícito de información. Una tubería transporta bytes entre extremos de lectura y escritura.

Uso: hacer llegar al Padre el texto producido por el Hijo.

Distinción: las variables de dos JVM no se comparten automáticamente. Las tuberías tienen capacidad limitada; si no se consumen, pueden atascar la ejecución.

### stdin, stdout y stderr

Definición: stdin es entrada estándar; stdout, salida estándar; stderr, salida estándar de errores.

Uso: recibir datos, emitir resultados y emitir diagnósticos.

Distinción: stderr no es el código de salida. Un proceso puede emitir por ambos flujos y devolver además un número al terminar.

### getInputStream, getErrorStream y getOutputStream

Definición: desde el objeto Process, getInputStream lee stdout del Hijo; getErrorStream lee stderr; getOutputStream permite escribir en stdin del Hijo.

Uso: conectar el programa creador con el creado.

Distinción: los nombres se interpretan desde el lado del programa que maneja Process. La salida del Hijo se convierte en entrada para el Padre.

### redirectErrorStream

Definición: redirectErrorStream(true) configura la fusión de stderr con stdout del proceso antes de iniciarlo.

Uso: consumir ambos por getInputStream y simplificar estas prácticas.

Distinción: no hay que seguir leyendo getErrorStream esperando encontrar ahí los errores fusionados. La configuración afecta al hijo directo, no automáticamente a toda la descendencia.

### InputStreamReader y codificación

Definición: InputStreamReader transforma bytes en caracteres usando una codificación. La codificación define la correspondencia entre bytes y texto.

Uso: interpretar correctamente la salida textual.

Distinción: no ejecuta comandos. Caracteres deformados pueden indicar una codificación incompatible; no prueban que el comando haya fallado. CMD y Java no siempre usan la misma codificación.

### BufferedReader, readLine y fin de flujo

Definición: BufferedReader facilita la lectura de texto; readLine obtiene una línea sin su terminador y devuelve null al alcanzar el final.

Uso: recorrer la salida completa de un proceso.

Distinción: una cadena vacía es una línea vacía y debe poder imprimirse. No es null ni significa fin de archivo.

### Bloqueo por tuberías

Definición: espera provocada porque un proceso intenta escribir en una tubería llena que nadie está consumiendo.

Uso: explica por qué no basta con esperar la terminación antes de leer una salida abundante.

Distinción: un comando puede no estar mal programado y aun así quedar esperando por la forma en que su creador gestiona los flujos. El patrón básico fusiona errores, consume salida y después espera.

## 11. CMD y vocabulario de los talleres

### Terminal e intérprete

Definición: terminal es la interfaz donde se interactúa con programas de consola; intérprete es el programa que analiza y ejecuta órdenes, como CMD o PowerShell.

Uso: escribir comandos y ver sus resultados.

Distinción: elegir una terminal en VS Code no hace que ProcessBuilder interprete por sí mismo órdenes de ese intérprete.

### Orden interna y ejecutable externo

Definición: una orden interna pertenece al intérprete; un ejecutable externo es un programa independiente.

Uso: DIR, COPY, MOVE, REN, MKDIR y TYPE se ejecutan mediante CMD. ping, java, javac y jar son ejecutables externos.

Distinción: ProcessBuilder no puede tratar DIR como un ejecutable independiente. Tampoco debe recibir todo un comando directo pegado como si fuera un nombre de archivo.

### cmd /c, /k y /d

Definición: /c ejecuta la orden y termina CMD; /k ejecuta y deja abierto el intérprete; /d desactiva AutoRun.

Uso: en estas prácticas, /c permite que la ejecución acabe y waitFor pueda retornar; /d reduce dependencia de personalizaciones.

Distinción: dejar CMD abierto con /k no significa que la tarea haya fallado, pero puede mantener la espera indefinidamente. Al intervenir CMD aparecen sus reglas de comillas y metacaracteres.

### Comodín y metacarácter

Definición: un comodín representa patrones de nombres, como *.txt. Un metacarácter tiene significado especial para el intérprete, como & o |.

Uso: seleccionar archivos o componer órdenes controladas.

Distinción: *.txt no es lo mismo que .txt. Separar elementos del ProcessBuilder no vuelve segura una cadena arbitraria enviada a CMD; deben validarse los datos y las operaciones.

### Listar, crear, copiar, mover, renombrar y mostrar

Definición: DIR lista; MKDIR crea directorios; COPY duplica archivos; MOVE cambia su ubicación; REN cambia su nombre; TYPE muestra texto.

Uso: son las operaciones encadenadas en los talleres.

Distinción: copiar conserva el origen; mover no. TYPE muestra contenido, no devuelve solo un código. /y evita confirmaciones de reemplazo en comandos que la admiten, no inventa nombres únicos.

### Ayuda offline y búsqueda de ejecutables

Definición: help y /? consultan documentación local de CMD; --help se usa en las herramientas Java; where busca ejecutables en Windows.

Uso: recordar sintaxis sin Internet. help dir explica una orden interna y where java busca el ejecutable.

Distinción: where dir normalmente no encuentra un programa porque DIR es interno. which es habitual en Bash, no una orden nativa equivalente de CMD; en PowerShell puede ser necesario escribir where.exe para evitar un alias.

## 12. Compilación y empaquetado

### Fuente, compilación y javac

Definición: el fuente es el texto .java. Compilarlo con javac analiza y traduce ese código a bytecode almacenado normalmente en archivos .class.

Uso: preparar clases para ejecutarlas o empaquetarlas.

Distinción: compilar no ejecuta main ni crea automáticamente un JAR. Un solo fuente puede producir varias clases compiladas. El nombre de cada clase determina su .class; no se elige con -d.

### Bytecode y archivo .class

Definición: bytecode es el formato de instrucciones de la JVM; .class contiene la representación compilada de una clase.

Uso: la JVM carga y ejecuta esas clases.

Distinción: no es el texto fuente ni un ejecutable nativo de Windows. Para lanzar una clase se indica su nombre, sin la extensión .class.

### -d y -encoding

Definición: en javac, -d indica la raíz de salida de las clases y -encoding cómo leer el texto fuente.

Uso: javac -encoding UTF-8 -d clases Saludo.java guarda Saludo.class dentro de clases si Saludo no tiene paquete.

Distinción: -d no indica dónde buscar Saludo.java. clases es una carpeta elegida por nosotros, no una palabra reservada.

### Paquete y nombre cualificado

Definición: un paquete organiza nombres de clases; el nombre cualificado incluye paquete y clase, como estudio.Saludo.

Uso: evita colisiones y estructura la aplicación.

Distinción: una carpeta cualquiera no obliga al compilador a considerar la clase dentro de un paquete. La declaración package y la estructura de búsqueda deben ser coherentes; el IDE puede exigir una organización concreta. El Saludo del taller 08 se pide sin paquete.

### Classpath y -cp

Definición: classpath es el conjunto de raíces y archivos donde el cargador busca clases y recursos. -cp permite configurarlo.

Uso: java -cp clases Saludo busca la clase Saludo en esa raíz.

Distinción: PATH busca programas; classpath busca clases y recursos. Con estudio.Saludo, la raíz debe contener estudio/Saludo.class, no apuntar directamente a la carpeta estudio.

### Modo fuente

Definición: ejecución con java de un archivo fuente, como java Saludo.java, disponible desde Java 11.

Uso: ejecutar programas fuente sin una compilación manual separada en casos compatibles.

Distinción: no es java -cp clases Saludo ni prepara por sí mismo los .class y el JAR del taller. Las reglas de paquetes y rutas dependen también del modo y versión del lanzador.

### JAR y jar.exe

Definición: JAR es un archivo que agrupa clases y recursos; jar.exe es la herramienta que lo crea o inspecciona.

Uso: empaquetar una aplicación para distribuirla o ejecutarla.

Distinción: el archivo JAR no es un proceso y jar no compila fuentes. El proceso aparece cuando java ejecuta la aplicación.

### Manifiesto y clase principal

Definición: manifiesto es un archivo de metadatos dentro del JAR. Main-Class indica la clase cuyo main se quiere iniciar.

Uso: permite que java -jar conozca el punto de entrada.

Distinción: declarar Saludo como principal no incluye por sí solo Saludo.class en el archivo. Hay que indicar también el contenido que se empaqueta.

### cfe, -C y punto

Definición: en jar cfe Aplicacion.jar Saludo -C clases ., c pide crear, f anuncia el archivo de salida y e la clase principal. -C clases toma el contenido desde esa carpeta; el punto selecciona ese directorio y su contenido para incorporarlo.

Uso: crea Aplicacion.jar, declara Saludo como principal e incluye lo que hay dentro de clases.

Distinción: Saludo responde a por dónde se empieza; -C clases . responde a qué se mete dentro. No duplica la selección de la clase. La carpeta clases no queda como envoltorio extra en la raíz del JAR.

### java -jar y argumentos de la aplicación

Definición: -jar pide ejecutar la aplicación contenida en el JAR, usando su punto de entrada.

Uso: java -jar Aplicacion.jar Ada entrega Ada al args[0] de la clase principal.

Distinción: el argumento Ada no es otra clase ni el nombre del JAR. La ruta del JAR sigue interpretándose desde el directorio de trabajo si es relativa.

### Etapa, dependencia y archivo antiguo

Definición: una etapa es una operación de la cadena; una dependencia exige un resultado válido anterior. Un archivo antiguo es un resultado de otra ejecución que sigue en disco.

Uso: compilar, comprobar, empaquetar, comprobar y ejecutar.

Distinción: la existencia de un .class o un JAR no demuestra éxito del último intento. Si javac falla hay que detenerse, aunque todavía exista una versión anterior.

## 13. Distinciones para recuperar de memoria

1. Programa describe instrucciones; proceso es una instancia activa.
2. PID identifica; código de salida informa del resultado.
3. Proceso aísla un entorno; los hilos de ese proceso lo comparten.
4. Pila y registros son propios de cada hilo; no toda su memoria lo es.
5. Concurrencia admite alternancia; paralelismo exige simultaneidad real.
6. Listo espera CPU; bloqueado espera un suceso.
7. Suspendido trata de activación o residencia; bloqueado, de la espera pendiente.
8. BCP conserva información de gestión; el planificador decide quién ejecuta.
9. Cambio de contexto conserva y restaura ejecución; swapping traslada entre memoria y almacenamiento.
10. FCFS mira llegada; SJF duración; Round Robin turnos y quantum.
11. Respuesta inicial termina al primer servicio; retorno termina al finalizar.
12. Tiempo real exige cumplir plazos; no simplemente ser rápido.
13. ProcessBuilder configura; start inicia; Process representa la ejecución.
14. waitFor espera al hijo; no lo detiene ni lee su texto.
15. stdout es texto de salida; stderr es texto de diagnóstico; código de salida es un número de terminación.
16. System.out.println muestra; System.exit devuelve el código al terminar la JVM.
17. directory fija la base del nuevo proceso; no crea ni mueve archivos.
18. PATH localiza programas; classpath localiza clases.
19. java.home señala una instalación; user.dir ayuda a identificar la base de trabajo.
20. javac compila; jar empaqueta; java ejecuta.
21. -d señala una carpeta de salida; no el nombre de un .class.
22. Clase principal indica por dónde iniciar; contenido del JAR indica qué archivos se incluyen.
23. Línea vacía es texto; null indica fin del flujo.
24. Llamar a otro main es una llamada; lanzar java mediante un proceso crea otra JVM.

## 14. Guion conceptual para el podcast

Organizar la conversación por familias de términos, no por talleres ni por líneas de código. Para cada término dar definición técnica breve, utilidad, un ejemplo y un contraste explícito. No sustituir estas cuatro piezas por una enumeración.

Dedicar especial atención a términos del sistema operativo antes de pasar al vocabulario Java. Relacionar después ambos mundos: proceso con Process; estado de espera con waitFor sin equiparar todos los estados de hilo y proceso; comunicación con flujos; resultado con propagación.

Usar como ejemplo conductor Padre → Hijo → herramienta. Para compilación, explicar Saludo.java → javac → Saludo.class → jar → Aplicacion.jar → java. Diferenciar siempre archivo, herramienta y proceso.

Intercalar preguntas como: qué espera un proceso listo, qué identifica un PID, qué conserva el BCP, quién queda esperando al llamar waitFor, qué diferencia PATH y classpath, y por qué declarar una clase principal no basta para incluirla en el JAR.

Cerrar con las distinciones del apartado anterior. Evitar afirmar que estas preguntas son las que aparecerán en el examen. Esta sección orienta la transformación en audio; no añade nuevos contenidos técnicos.
