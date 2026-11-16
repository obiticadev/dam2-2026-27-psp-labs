# PSP · UT01: teoría fundamental de procesos

Documento de estudio para NotebookLM y repaso de examen.

## 1. Alcance y mapa general

PSP significa Programación de Servicios y Procesos. Este documento desarrolla la unidad de procesos y la teoría necesaria para comprender las prácticas de Java que ejecuta otros programas en Windows. No pretende cubrir todo el módulo de PSP: no desarrolla programación multihilo, semáforos, sockets, servicios de red ni criptografía.

El recorrido es: programa y proceso; procesos e hilos; concurrencia y paralelismo; estados, BCP y planificación; comunicación; creación y control de procesos con Java; argumentos, rutas y herramientas; errores y preguntas de examen.

La idea que conecta todo el tema es que el sistema operativo administra los procesos y que un programa Java puede solicitarle crear otros procesos, comunicarse con ellos y esperar su resultado.

Ejemplo conductor: una aplicación Padre inicia otra aplicación Hijo y esta inicia ping. Son tres procesos. La salida de ping no llega mágicamente al Padre: el Hijo debe leerla y reenviarla. El resultado final tampoco se propaga automáticamente: cada nivel debe devolver el código que corresponda.

## 2. Programa, ejecutable y proceso

Un programa es un conjunto de instrucciones para realizar una tarea. Un ejecutable es un archivo que permite poner en marcha un programa, directamente o mediante un entorno de ejecución. Un proceso es una instancia de un programa en ejecución.

El programa es una descripción; el proceso es una actividad. El mismo archivo puede originar varios procesos independientes. Abrir varias instancias de una aplicación no crea varios archivos ejecutables, sino distintas instancias en ejecución.

Cada proceso tiene un contexto de ejecución: un espacio de direcciones, recursos, información de estado y uno o más hilos. El sistema operativo le asigna un identificador de proceso, llamado PID. Un PID identifica una instancia durante su vida; puede reutilizarse posteriormente.

Un archivo Java de código fuente no es un proceso. Un archivo de bytecode tampoco. Un JAR es un archivo que agrupa clases y recursos. Al ejecutar una aplicación Java se crea un proceso que contiene una máquina virtual Java, o JVM.

### Ejemplo aplicado

Padre y Hijo son dos clases, pero lo decisivo es cómo se utilizan. Si Padre llama a Hijo.main como método, ambos siguen dentro de la misma JVM. Si Padre inicia otro ejecutable java para ejecutar Hijo, aparece una segunda JVM en otro proceso.

La frase «ejecuto otra clase» no demuestra que se haya creado otro proceso. Hay que comprobar si existe una nueva ejecución solicitada al sistema operativo.

## 3. Hilos y diferencias con los procesos

Un hilo es un flujo de ejecución secuencial dentro de un proceso. Cada hilo mantiene su contador de programa, sus registros y su pila. Los hilos del mismo proceso comparten el entorno de este, incluido el código, la memoria y muchos recursos.

Los procesos suelen tener espacios de memoria aislados entre sí. Una variable normal del Hijo no es la misma variable del Padre, aunque tengan el mismo nombre. Para intercambiar información necesitan mecanismos de comunicación explícitos.

Los procesos no son incapaces de compartir información: pueden hacerlo mediante tuberías, archivos, sockets o mecanismos de memoria compartida. La diferencia es que esa comunicación no equivale a compartir automáticamente todas las variables normales.

### Ventajas y límites de los hilos

Crear hilos y cambiar entre hilos del mismo proceso suele resultar menos costoso que hacerlo entre procesos independientes. Compartir memoria facilita el intercambio de información. También permite mantener una interfaz respondiendo mientras otro flujo espera o trabaja.

Esas ventajas tienen costes: si varios hilos modifican datos compartidos sin coordinación pueden producirse condiciones de carrera. Una condición de carrera aparece cuando el resultado depende del orden o momento de accesos concurrentes que no están correctamente coordinados.

Los hilos pueden aprovechar varios núcleos cuando el trabajo es paralelizable. Sin embargo, añadir hilos no garantiza mayor velocidad: influyen el hardware, las dependencias, la sincronización y el coste de coordinación.

No se deben confundir dos usos de la palabra independencia: un hilo puede tener un flujo de control independiente sin existir fuera de un proceso.

## 4. Concurrencia, paralelismo y distribución

Concurrencia significa que varias tareas progresan durante intervalos de tiempo que se solapan. Puede existir con un único núcleo: el sistema alterna tareas y conserva el estado necesario para reanudarlas.

Paralelismo significa que varias tareas ejecutan instrucciones realmente de forma simultánea. Para ello hacen falta varias unidades de procesamiento disponibles y trabajo que pueda ejecutarse a la vez.

Ejemplo: un cocinero alterna entre dos platos; hay concurrencia. Dos cocineros trabajan simultáneamente en distintos platos; hay paralelismo. La analogía ayuda a recordar, pero la definición técnica debe hablar de alternancia o simultaneidad real de ejecución.

Que haya varios procesos no asegura que estén ejecutándose todos al mismo tiempo. Algunos pueden estar listos o bloqueados. Tampoco concurrencia significa obligatoriamente que las tareas compitan por la misma variable.

Una cadena puede ser secuencial aunque use varios procesos. En el taller de JAR, compilar, empaquetar y ejecutar deben ir en ese orden, no lanzarse a la vez.

En términos generales, un sistema distribuido utiliza componentes en diferentes equipos que se comunican. Distribución no es sinónimo de concurrencia ni de paralelismo, y las prácticas locales Padre-Hijo no constituyen por sí solas un sistema distribuido.

## 5. Estados y ciclo de vida de un proceso

El modelo clásico de cinco estados distingue nuevo, listo, en ejecución, bloqueado y terminado. Es un modelo conceptual: los nombres concretos pueden variar según el sistema operativo.

- Nuevo: se está creando y todavía no ha sido admitido para su ejecución normal.
- Listo: puede continuar ejecutándose, pero espera que le asignen CPU.
- En ejecución: está utilizando la CPU.
- Bloqueado: no puede continuar hasta que ocurra un suceso, como finalizar una operación de entrada/salida.
- Terminado: ha finalizado o ha sido retirado.

### Transiciones esenciales

Un proceso nuevo pasa a listo al ser admitido. De listo pasa a ejecución cuando el planificador le asigna CPU. De ejecución puede pasar a terminado, a bloqueado si debe esperar un suceso, o a listo si se le retira la CPU.

Cuando termina la espera de un proceso bloqueado, pasa a listo. No pasa necesariamente a ejecución: aún debe competir por la CPU.

La distinción más importante es listo frente a bloqueado. El listo necesita CPU; el bloqueado necesita primero un suceso. Dar CPU a un proceso que sigue esperando los datos necesarios no resuelve su espera.

### Relación con las prácticas

Cuando un hilo del Padre llama a waitFor, ese hilo espera al proceso Hijo. El Hijo no queda detenido por esa llamada; continúa hasta finalizar. Cuando el Hijo termina, el hilo llamador puede continuar cuando vuelva a ser planificado.

Esta explicación no significa que todos los hilos del Padre deban quedar parados ni que se pueda equiparar sin matices un estado de hilo Java con un estado de proceso del sistema operativo.

## 6. BCP, cambio de contexto y swapping

El Bloque de Control de Proceso, abreviado BCP o PCB, es el registro mediante el que el sistema operativo mantiene información para administrar un proceso.

Incluye información como PID, estado, espacio de direcciones, recursos, datos de planificación y contexto de ejecución. El contexto de CPU comprende registros, contador de programa y puntero de pila. La organización exacta de estos datos depende del sistema.

Su finalidad no es simplemente listar procesos: permite identificarlos, controlar sus recursos, planificarlos y conservar lo necesario para reanudarlos.

### Cambio de contexto

Un cambio de contexto guarda el estado de la tarea que deja de ejecutarse y restaura el estado de la tarea que entra. Así cada tarea puede continuar donde se quedó.

La alternancia permite concurrencia, pero tiene un coste: mientras se guarda y restaura contexto no se están ejecutando instrucciones útiles de esas tareas. Un cambio entre hilos del mismo proceso suele tener menor coste que un cambio entre procesos, aunque no es una garantía idéntica para todos los sistemas.

El cambio de contexto no consiste en copiar el programa entero al disco.

### Swapping y suspensión

El swapping o intercambio consiste, en el modelo del tema, en trasladar un proceso o parte de él entre memoria principal y almacenamiento de apoyo para liberar memoria.

En el modelo ampliado se distinguen listo/suspendido y bloqueado/suspendido. Un proceso puede no estar disponible en memoria y, además, estar esperando o no un suceso.

Bloqueado y suspendido no son sinónimos. Bloqueado indica espera de un suceso; suspendido hace referencia a su situación de activación o residencia según el modelo. Si el suceso llega mientras está bloqueado/suspendido, puede pasar a listo/suspendido antes de volver a memoria y ejecutar.

## 7. Planificación de procesos

El planificador del sistema operativo decide qué tarea lista debe utilizar el procesador. No es lo mismo que el cargador, que participa en preparar la ejecución del programa.

Los objetivos incluyen equidad, buen aprovechamiento de CPU, rendimiento y tiempos de respuesta y retorno razonables. Los objetivos pueden entrar en conflicto: no existe un algoritmo universalmente mejor para todas las cargas.

Una política expulsiva permite retirar la CPU a la tarea actual. Una política no expulsiva deja que la tarea la libere, termine su ráfaga o se bloquee, en lugar de retirársela arbitrariamente.

### FCFS

First Come, First Served atiende por orden de llegada. En su forma no expulsiva deja continuar la ráfaga que está ejecutándose. Es sencillo, pero una tarea larga puede retrasar a tareas cortas.

### SJF

Shortest Job First elige la ráfaga más corta entre las disponibles. No elige simplemente la última tarea que llegó. Su aplicación requiere conocer o estimar duraciones y puede perjudicar a trabajos largos. La variante expulsiva basada en el tiempo restante se denomina SRTF.

### Round Robin

Round Robin utiliza una cola circular y un intervalo máximo de CPU llamado quantum. Si la tarea agota el quantum sin terminar ni bloquearse, vuelve a la cola.

Un quantum muy pequeño mejora la frecuencia de turnos, pero aumenta cambios de contexto. Uno muy grande aproxima el comportamiento al de FCFS para muchas cargas. Si una tarea se bloquea antes de agotarlo, deja de ejecutar sin tener que consumir todo el intervalo.

### Medidas básicas

- Tiempo de retorno: instante de finalización menos instante de llegada.
- Tiempo de respuesta inicial: instante de la primera ejecución menos instante de llegada.
- Tiempo de espera: tiempo total en la cola de listos. No incluye por definición toda espera de entrada/salida.

En ejercicios simplificados con una sola ráfaga y sin entrada/salida, espera puede calcularse como retorno menos tiempo de CPU. No apliques esa resta indiscriminadamente a modelos con bloqueos u otras fases.

## 8. Tipos de procesos y requisitos

Los procesos por lotes reciben tareas y datos y se orientan a producir un resultado sin interacción continua. Un ejemplo es una compilación automática.

Los interactivos mantienen comunicación con el usuario. Importa que respondan de manera razonable a sus acciones. Un editor es un ejemplo.

En tiempo real importa cumplir los plazos definidos por el sistema. No basta con ser rápido en promedio. Una respuesta muy rápida pero posterior al plazo puede ser incorrecta para el requisito.

Estas clasificaciones dependen del comportamiento y los requisitos, no del lenguaje utilizado. Una aplicación escrita en Java no pertenece automáticamente a un tipo concreto.

## 9. Creación de procesos en Java

Java dispone de mecanismos como Runtime.exec y ProcessBuilder. Para estas prácticas se utiliza ProcessBuilder porque permite expresar claramente ejecutable, argumentos, directorio, entorno y redirecciones.

ProcessBuilder es configuración. Construir un objeto ProcessBuilder no ejecuta nada. La llamada start solicita al sistema operativo crear el proceso y devuelve un objeto Process.

Process representa la ejecución iniciada y permite acceder a sus flujos, esperar y consultar su resultado. No es otro constructor de comandos ni equivale al archivo ejecutable.

Modificar el builder después de arrancar un proceso no reconfigura retroactivamente ese proceso. Un builder puede usarse para iniciar más procesos, pero cada llamada a start crea una nueva ejecución. No se debe volver a llamar a start para intentar leer el mismo proceso.

### Secuencia que debe razonarse

Primero configurar el comando, sus argumentos, la carpeta y las redirecciones. Después arrancar una sola vez para esa etapa. A continuación consumir la salida, esperar la finalización, comprobar el código y decidir si se continúa.

En una cadena de etapas dependientes, no basta con esperar a todos al final. Si se arrancan compilador y empaquetador antes de esperar, el empaquetador puede comenzar sin que existan las clases.

## 10. Comunicación por argumentos y flujos

Los argumentos son datos iniciales que el proceso creador entrega al nuevo programa. En Java llegan al parámetro String[] args del método main.

args.length indica cuántos argumentos se recibieron. args[0] es el primero y args[1] es el segundo. Antes de acceder a una posición hay que comprobar que exista, o definir un contrato que valide y rechace la ausencia.

La expresión args.length > 0 ? args[0] : "." significa: usar el primer argumento si existe; en caso contrario usar el texto punto, que se interpreta como carpeta actual cuando se utiliza como ruta.

El ejecutable java y el nombre de la clase o fuente no forman parte de args. Los datos que se escriben después sí.

En un ProcessBuilder que inicia directamente un ejecutable, cada elemento corresponde al programa o a uno de sus argumentos. Un dato como Ada Lovelace debe ser un único elemento, sin dividirlo por el espacio. Las variables Padre.nombre e Hijo.nombre son independientes.

### Entrada, salida y errores

Un proceso dispone habitualmente de entrada estándar, salida estándar y salida de errores, llamadas stdin, stdout y stderr.

Desde el objeto Process del Padre, getInputStream permite leer stdout del Hijo. getErrorStream permite leer stderr. getOutputStream permite escribir en stdin del Hijo. Los nombres se entienden desde la perspectiva del programa que maneja el objeto Process.

InputStreamReader interpreta bytes como caracteres. BufferedReader facilita leer líneas. El bucle termina cuando readLine devuelve null. Una cadena vacía significa una línea vacía, no fin de archivo.

La codificación importa: la salida de CMD no tiene por qué utilizar UTF-8. Los caracteres incorrectos pueden ser un problema de decodificación, no una pérdida de procesos o líneas.

### Errores y tuberías

redirectErrorStream(true), antes de start, fusiona stderr con stdout para poder leer ambos por un único flujo. La variante sin argumentos solo consulta el valor de esa configuración.

La fusión se aplica a ese proceso directo. Si Hijo inicia CMD, Hijo también debe consumir o redirigir sus errores. La fusión del Padre no configura automáticamente los procesos nietos.

Una tubería tiene capacidad limitada. Si el Hijo genera mucha salida y nadie la consume, puede quedar esperando espacio. Si al mismo tiempo el Padre espera su finalización sin leer, ambos pueden quedar detenidos. Por eso el patrón básico consume la salida antes de esperar.

Si se mantienen dos flujos separados, hay que garantizar que ambos se consuman sin provocar bloqueos. En estas prácticas, fusionarlos simplifica el problema. El patrón no pretende resolver protocolos interactivos complejos.

## 11. Espera, resultado y propagación del fallo

waitFor bloquea al hilo que lo llama hasta que termina el proceso representado y devuelve su código de salida. No congela al Hijo ni decide si el trabajo estaba bien hecho.

Un código cero suele indicar éxito y uno distinto de cero suele indicar un problema, según el contrato del programa. El significado exacto de cada número depende de la herramienta.

exitValue consulta el código sin esperar. Si el proceso sigue activo, falla con IllegalThreadStateException. Esperar y consultar sin esperar son operaciones distintas.

Si start lanza IOException, puede que no se haya creado el proceso: ejecutable inexistente, carpeta de trabajo inválida o permisos. Si el comando arrancó y devolvió un código de error, sí hubo proceso, pero falló la operación.

### La trampa del éxito falso

Supongamos que Hijo ejecuta copy y el archivo original no existe. CMD puede devolver un fallo. Si Hijo ignora ese resultado y termina normalmente, su JVM puede devolver cero al Padre.

El Padre solo ha observado que Hijo terminó sin declarar un fallo; no ha comprobado directamente la copia. Para propagar el problema, Hijo debe recoger el código del comando y terminar con ese código, por ejemplo mediante System.exit(codigo). Padre debe hacer lo mismo con el resultado del Hijo.

Imprimir el número no lo convierte en el código de salida de la JVM. Escribir un mensaje de éxito tampoco demuestra que se haya copiado ningún archivo.

Las excepciones no deben ocultarse en un catch vacío. Se deben informar o propagar y evitar continuar como si la operación hubiera tenido éxito.

## 12. Directorio, rutas y entorno

El directorio de trabajo es la base desde la que se interpretan las rutas relativas. No es necesariamente la carpeta donde se encuentra el archivo de código fuente.

pb.directory(new File(carpeta)) configura el directorio del proceso que va a crearse. No crea la carpeta, no mueve archivos y no cambia el directorio global del equipo ni el del Padre.

Una ruta relativa como src/Hijo.java funciona si la carpeta actual contiene src. Si cambia la base, puede dejar de funcionar. System.getProperty("user.dir") ayuda a comprobar el directorio de trabajo de la JVM.

La propiedad java.home tiene otro propósito: indica la instalación del Java que ejecuta esa JVM. Su nombre exacto incluye un punto; no es java/home.

Una ruta absoluta identifica la ubicación sin depender de la carpeta actual. Para lanzar un Hijo que trabajará en otra base, puede ser necesario resolver antes a rutas absolutas el fuente o el classpath.

### PATH y herramientas

PATH es una variable de entorno con carpetas en las que se buscan programas. No es el programa Java. No poder escribir java en CMD no demuestra que no haya ninguna instalación.

System.getProperty("java.home") + "/bin/java.exe" permite construir, en Windows, la ruta del ejecutable de la instalación actual. El mismo patrón puede apuntar a javac.exe o jar.exe solo si esos archivos existen.

El Extension Pack de Java puede disponer de un runtime privado y no configurar PATH. No garantiza que el entorno disponible contenga los tres ejecutables necesarios. Una ruta completa evita buscar por PATH, pero no instala herramientas ausentes.

El comportamiento de búsqueda del ejecutable al arrancar directamente desde Java puede diferir del de CMD. Que un caso encuentre java no demuestra que todos los intérpretes o equipos lo encontrarán igual.

## 13. CMD, programas externos y ayuda offline

Un intérprete de órdenes procesa comandos. CMD es el intérprete clásico de Windows; PowerShell es otro entorno con reglas propias. La terminal visible en el editor no convierte automáticamente cada proceso creado por Java en una orden de ese intérprete.

ping y las herramientas java, javac y jar son ejecutables externos. Se pueden iniciar directamente si se conoce una ruta válida o el sistema puede encontrarlos.

dir, copy, move, ren, mkdir y type son órdenes internas de CMD. Para utilizarlas desde ProcessBuilder se inicia CMD y se le pide interpretar la orden.

cmd /c ejecuta una orden y termina. cmd /k ejecuta y deja abierto el intérprete, por lo que puede impedir que termine la práctica. La opción /d desactiva AutoRun y reduce la dependencia de configuraciones personales.

ProcessBuilder no interpreta una cadena como si fuera una terminal. Si el primer elemento es "ping -n 2", intentará usar ese texto como nombre del programa. En lanzamiento directo se separan ejecutable y argumentos.

Al utilizar cmd /c sí interviene un intérprete. Eso permite interpretar una cadena de comando, pero introduce comillas, expansiones y metacaracteres como &, |, > y %. Separar elementos no vuelve seguro cualquier texto arbitrario enviado a CMD. En las prácticas se deben usar órdenes controladas y datos validados.

### Cómo consultar ayuda

En CMD, help muestra órdenes internas y help dir explica DIR. La opción /? suele mostrar la ayuda de comandos Windows: dir /?, copy /? o ping /?.

Las herramientas Java ofrecen java --help, javac --help y jar --help. Puede utilizarse su ruta completa si no están en PATH. Estas ayudas no requieren Internet.

where ping busca el ejecutable en la carpeta actual y en PATH. where dir normalmente no encuentra nada porque DIR es interno, pero help dir sí lo explica. Si where java no encuentra nada, puede faltar PATH o la instalación.

which es habitual en entornos Bash/Linux y busca programas; type en Bash puede identificar también órdenes internas. No se debe presentar which como orden nativa equivalente de CMD. En PowerShell, where puede ser un alias diferente; where.exe invoca la herramienta de búsqueda de Windows.

## 14. Qué enseña cada familia de ejercicios

Ping enseña a pasar un destino y recuperar salida. En Windows, -n 2 solicita dos peticiones; -w indica espera por respuesta y -t mantiene el ping continuo. Linux utiliza otras opciones, como -c para el número de solicitudes. Que ICMP falle no prueba que una página web esté caída.

El listado de directorios enseña la relación entre carpeta de trabajo y comando. DIR /b produce formato simple. /s recorre subcarpetas y /a-d excluye directorios. El patrón *.txt busca nombres con esa extensión; .txt no equivale a ese patrón.

Copiar conserva el origen y crea otra copia. COPY /y permite reemplazar sin pedir confirmación; no genera números automáticamente. La carpeta de destino debe existir o crearse antes.

Mover cambia la ubicación y consume el origen. MOVE /y evita confirmaciones de reemplazo. REN cambia un nombre y recibe como segundo argumento un nombre nuevo, no una ruta a otra carpeta. Repetir una prueba de movimiento exige restaurar los datos iniciales.

TYPE muestra el contenido de un archivo de texto. El ejercicio no consiste en leer ese archivo directamente con FileReader: se practica que el comando produzca salida y Java consuma la tubería.

Java que lanza Java demuestra la creación de JVM independientes y la conservación de argumentos con espacios. Llamar al método main de otra clase no es equivalente.

El taller integrador combina etapas. Antes de empezar cada una debe haberse leído, esperado y validado la anterior. Un helper puede reutilizar el patrón, pero no debe ocultar ni transformar fallos en éxitos.

## 15. JDK, bytecode, classpath y JAR

La JVM ejecuta bytecode Java. Un entorno de ejecución aporta lo necesario para ejecutar aplicaciones. El JDK es el conjunto de desarrollo que incorpora herramientas como el compilador. Las distribuciones y su empaquetado pueden variar; hay que verificar las herramientas reales.

javac compila fuentes y genera archivos .class con bytecode. La opción -encoding UTF-8 indica cómo leer el fuente y -d clases indica la carpeta de salida.

java -cp bin paquete.Clase ejecuta una clase ya compilada. El classpath señala la raíz de búsqueda de las clases. Si la clase es estudio.Saludo, debe encontrarse la estructura estudio/Saludo.class dentro de esa raíz. No se añade .class al nombre cualificado.

java Saludo.java utiliza el modo de ejecución de fuente, disponible desde Java 11. Es un modo distinto de ejecutar una clase compilada; no prepara por sí mismo un JAR distribuible. La coherencia entre paquete y ruta importa, especialmente en el IDE y en lanzadores de fuente actuales.

Un JAR agrupa clases y recursos. Para que java -jar Aplicacion.jar sepa qué iniciar, necesita el punto de entrada indicado en el manifiesto.

El comando jar cfe Aplicacion.jar Saludo -C clases . crea el JAR y declara Saludo como principal: c significa crear; f indica archivo; e especifica punto de entrada. La parte -C clases . incorpora el contenido de clases desde esa raíz, sin envolverlo en una carpeta adicional llamada clases.

Si Saludo pertenece al paquete estudio, la principal es estudio.Saludo. La estructura interna debe conservar el paquete.

java -jar Aplicacion.jar Ada ejecuta la aplicación y entrega Ada como argumento. El código de salida del proceso depende de cómo termine la aplicación.

### Orden y evidencias

La cadena correcta es compilar, comprobar; empaquetar, comprobar; ejecutar, comprobar. Si la compilación falla, no se empaqueta ni se ejecuta. Un archivo antiguo no demuestra que el último intento haya tenido éxito.

Se comprueban cosas diferentes: la existencia del archivo demuestra que hay un archivo; el manifiesto indica el punto de entrada; el código expresa cómo declaró terminar una etapa; y una comparación de contenido verifica el resultado material cuando el ejercicio lo requiere.

## 16. Diez errores que no deben aparecer en la explicación

1. Definir proceso como un archivo almacenado.
2. Afirmar que los hilos existen independientemente de cualquier proceso.
3. Decir que varios hilos siempre aceleran una aplicación.
4. Utilizar concurrencia y paralelismo como sinónimos.
5. Confundir listo, bloqueado y suspendido.
6. Describir el cambio de contexto como copiar todo el programa al disco.
7. Pensar que construir un ProcessBuilder ya ejecuta el comando.
8. Decir que waitFor detiene el Hijo o que imprime automáticamente su salida.
9. Confundir terminar con éxito: el proceso puede terminar con error.
10. Suponer que el código del comando, sus errores y su salida atraviesan solos todos los niveles de la cadena.

## 17. Preguntas para practicar recuperación activa

Estas son las mismas 16 preguntas del banco de teoría del panel local. Las secciones anteriores contienen los conceptos necesarios para responderlas. Una respuesta de examen debe empezar por la definición, añadir la distinción esencial y cerrar con una consecuencia o ejemplo.

1. ¿Qué es un proceso?
2. ¿Qué es un hilo?
3. ¿Qué diferencias hay entre procesos e hilos?
4. ¿Qué ventajas tienen los hilos frente a varios procesos?
5. Diferencia entre programa, ejecutable y proceso.
6. Diferencia entre concurrencia y paralelismo.
7. Explica los estados de un proceso.
8. ¿Qué es el BCP y qué contiene?
9. ¿Qué es un cambio de contexto?
10. ¿Qué es el planificador y qué objetivos tiene?
11. Explica FCFS, SJF y Round Robin.
12. ¿Qué es swapping y qué significa suspendido?
13. Diferencia entre ProcessBuilder y Process.
14. ¿Qué hace waitFor() y qué indica el código de salida?
15. ¿Cómo se recupera la salida de un proceso?
16. ¿Qué tipos de procesos describe el tema?

### Casos breves de aplicación

Caso A. El Padre inicia un Hijo que ejecuta copy. El archivo no existe y el Padre muestra cero. La explicación probable es que Hijo ignoró el código de CMD y terminó normalmente. Hay que revisar la propagación del fallo en cada nivel.

Caso B. DIR funciona al escribirlo en la terminal, pero no como ejecutable directo en ProcessBuilder. DIR es interno de CMD y debe ejecutarse a través del intérprete.

Caso C. Un proceso espera datos de disco mientras otro usa CPU. El primero está bloqueado; al llegar los datos pasará a listo. Esto no demuestra paralelismo.

Caso D. El JAR se crea aun cuando el fuente contiene un error nuevo. Puede haberse continuado tras un fallo de compilación y empaquetado clases antiguas. Se requieren barreras y validación entre etapas.

Caso E. El Padre inicia el fuente src/Hijo.java y no ve nada. Deben examinarse stderr, directorio de trabajo, ruta y paquete antes de atribuirlo a que el bucle de lectura está mal.

## 18. Síntesis y procedencia

Un proceso es un programa ejecutándose y administrado por el sistema operativo. Tiene estado y recursos; sus hilos ejecutan instrucciones. La planificación permite alternar tareas y el contexto conserva dónde continuar. La concurrencia no exige simultaneidad y el paralelismo sí.

En Java, ProcessBuilder configura y start crea. Process permite comunicarse y esperar. Los argumentos aportan datos iniciales; los flujos transportan entrada, salida y errores; el código de salida permite decidir si continuar.

La secuencia central es configurar, arrancar, consumir, esperar, comprobar y propagar. Cada proceso controla a su hijo inmediato; no se debe suponer comunicación o propagación automática a través de toda la cadena.

Material elaborado a partir del banco conceptual de panel/data.js, los enunciados de los nueve talleres y el alcance descrito en FUENTES.md de este repositorio. No se presenta como transcripción íntegra del manual oficial ni como garantía de las preguntas que aparecerán en el examen.
