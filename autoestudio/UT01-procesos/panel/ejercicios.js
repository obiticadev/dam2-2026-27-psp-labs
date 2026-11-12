/* Ejercicios añadidos por taller (mismo patrón, más exigencia) y equivalentes Linux
   para ensayar en casa. Material de práctica elaborado; no son enunciados oficiales. */
window.PSP_EXTRA = {
  '01-ping': {
    linux:{cmd:'ping -c 2 127.0.0.1', tokens:['ping','-c','2','127.0.0.1']},
    extra:[
      {lvl:'Básico', t:'Cuatro solicitudes y cierre', task:'El hijo hace ping -n 4 al destino recibido. Al acabar el bucle de lectura imprime HIJO:fin y sale con el código de ping.',
        out:'Respuesta desde 127.0.0.1: bytes=32 tiempo<1m TTL=128\n… (4 respuestas)\nHIJO:fin\nPADRE:codigo=0', check:['-n 4, nunca -t','HIJO:fin se imprime después del bucle, no dentro','El código final es el de ping'],
        hint:'La línea final va entre el while y el waitFor(): cuando readLine() devuelve null, ping ya ha cerrado su salida.', lx:'ping -c 4',
        sol:String.raw`Process ping = new ProcessBuilder("ping", "-n", "4", destino)
        .redirectErrorStream(true).start();
// ... while ((linea = br.readLine()) != null) System.out.println(linea);
System.out.println("HIJO:fin");
System.exit(ping.waitFor());`},
      {lvl:'Medio', t:'Contar respuestas', task:'El hijo reenvía la salida y además cuenta las líneas que contienen TTL=. Al final imprime RESPUESTAS:n. Si n es 0, sale con código 1 aunque ping haya devuelto 0.',
        out:'…\nRESPUESTAS:2\nPADRE:codigo=0', check:['Cuenta dentro del bucle, sin leer dos veces la misma línea','Decide el código con un criterio explícito','No depende de los milisegundos'],
        hint:'Guarda la línea en una variable y úsala dos veces: println y contains("TTL="). Llamar dos veces a readLine() se salta líneas.',
        sol:String.raw`int respuestas = 0;
while ((linea = br.readLine()) != null) {
    System.out.println(linea);
    if (linea.contains("TTL=")) respuestas++;
}
int codigo = ping.waitFor();
System.out.println("RESPUESTAS:" + respuestas);
System.exit(respuestas == 0 ? 1 : codigo);`},
      {lvl:'Examen', t:'Varios destinos en secuencia', task:'El padre recibe varios destinos y los pasa todos al hijo. El hijo lanza un ping -n 1 por destino, uno detrás de otro, y se detiene en el primero que falle propagando su código.',
        out:'--- 127.0.0.1 ---\nRespuesta desde 127.0.0.1 …\n--- noexiste ---\nLa solicitud de ping no pudo encontrar el host noexiste…\nPADRE:codigo=1', check:['Un proceso ping por destino, no uno con todos','Se espera a cada ping antes del siguiente','El primer fallo detiene la cadena'],
        hint:'En el padre añade todos los args al comando del hijo (List<String> + addAll). En el hijo, un for que llama a un método que arranca, lee y devuelve waitFor().', lx:'ping -c 1',
        sol:String.raw`for (String destino : args) {
    System.out.println("--- " + destino + " ---");
    int codigo = ejecutar(new ProcessBuilder("ping", "-n", "1", destino));
    if (codigo != 0) System.exit(codigo);
}`}
    ]
  },
  '02-directorios': {
    linux:{cmd:'ls -1', tokens:['ls','-1'], note:'ls es un ejecutable: no hace falta sh -c.'},
    extra:[
      {lvl:'Básico', t:'Solo archivos', task:'Lista la carpeta recibida mostrando solo archivos, sin subcarpetas. La salida debe llegar al padre.',
        out:'entrada con espacios.txt\nPADRE:codigo=0', check:['/a-d es un argumento propio','directory() sigue fijando la carpeta','No filtras en Java: filtra DIR'],
        hint:'Añade "/a-d" a la lista de argumentos de cmd: "cmd", "/d", "/c", "dir", "/b", "/a-d".', lx:'find . -maxdepth 1 -type f',
        sol:String.raw`ProcessBuilder pb = new ProcessBuilder("cmd", "/d", "/c", "dir", "/b", "/a-d");
pb.directory(new File(carpeta));`},
      {lvl:'Medio', t:'Listado numerado con total', task:'El hijo numera cada línea (1: nombre) y al final imprime TOTAL:n. El padre no cambia.',
        out:'1: entrada con espacios.txt\n2: subcarpeta\nTOTAL:2\nPADRE:codigo=0', check:['El contador vive en el hijo','TOTAL se imprime tras el bucle','El código sigue siendo el de CMD'],
        hint:'Un int n antes del while; dentro, println(++n + ": " + linea).',
        sol:String.raw`int n = 0;
while ((linea = br.readLine()) != null) System.out.println(++n + ": " + linea);
int codigo = cmd.waitFor();
System.out.println("TOTAL:" + n);
System.exit(codigo);`},
      {lvl:'Examen', t:'Validar antes de lanzar', task:'El padre comprueba con File.isDirectory() la carpeta recibida. Si no existe imprime PADRE:carpeta inexistente y sale con 2 sin crear ningún proceso. Si existe, el hijo lista ordenado por nombre con /o:n.',
        out:'PADRE:carpeta inexistente   (código 2)\n— o bien —\nentrada con espacios.txt\nsubcarpeta\nPADRE:codigo=0', check:['La validación ocurre antes de start()','Código 2 distinto del de un fallo de CMD','/o:n como argumento separado'],
        hint:'Valida datos en el borde: es más barato y más claro que dejar que start() lance IOException.', lx:'ls -1 (ya ordena por nombre)',
        sol:String.raw`if (!new File(carpeta).isDirectory()) {
    System.out.println("PADRE:carpeta inexistente");
    System.exit(2);
}`}
    ]
  },
  '03-buscar': {
    linux:{cmd:"find . -name '*.txt'", tokens:['find','.','-name','*.txt'], note:'Sin shell, nadie expande el comodín: find lo recibe literal, como DIR en CMD.'},
    extra:[
      {lvl:'Básico', t:'Buscar fuentes Java', task:'Cambia la búsqueda para listar los .java de la carpeta y sus subcarpetas.',
        out:'C:\\PSP\\zona de pruebas\\Saludo.java\nPADRE:codigo=0', check:['El patrón sigue siendo un único argumento','/s y /b se mantienen'],
        hint:'Solo cambia "*.txt" por "*.java". Si no hay ninguno, DIR devuelve 1: prepara un .java de prueba.', lx:"find . -name '*.java'",
        sol:String.raw`new ProcessBuilder("cmd", "/d", "/c", "dir", "/s", "/b", "*.java")`},
      {lvl:'Medio', t:'Extensión de una lista cerrada', task:'El padre recibe carpeta y extensión y pasa ambas al hijo. El hijo solo acepta txt, java o csv: con otra sale con 2 sin lanzar CMD. Construye el patrón "*." + ext.',
        out:'(ext=csv)\nC:\\PSP\\zona de pruebas\\ignorar.csv\nPADRE:codigo=0\n(ext=exe) → PADRE:codigo=2', check:['Validación con lista cerrada, no con lo que escriba el usuario','El patrón se pasa como un argumento','El padre transmite dos argumentos'],
        hint:'List.of("txt", "java", "csv").contains(ext). Nunca concatenes texto libre a una orden de CMD.',
        sol:String.raw`String ext = args[1];
if (!List.of("txt", "java", "csv").contains(ext)) System.exit(2);
ProcessBuilder pb = new ProcessBuilder("cmd", "/d", "/c", "dir", "/s", "/b", "*." + ext);`},
      {lvl:'Examen', t:'Contar coincidencias y decidir el código', task:'El hijo imprime cada ruta y al final ENCONTRADOS:n. DIR devuelve 1 cuando no hay coincidencias: decide que eso es un resultado válido (ENCONTRADOS:0, código 0) y que cualquier otro código sí es un fallo.',
        out:'…\nENCONTRADOS:2\nPADRE:codigo=0', check:['Distingue "no hay nada" de "ha fallado"','El criterio está escrito en el código, no implícito','Explica en tus notas por qué'],
        hint:'Cuenta en el bucle. Tras waitFor(): if (codigo == 1 && n == 0) codigo = 0;',
        sol:String.raw`int codigo = cmd.waitFor();
System.out.println("ENCONTRADOS:" + n);
if (codigo == 1 && n == 0) codigo = 0; // sin coincidencias no es un error aquí
System.exit(codigo);`}
    ]
  },
  '04-copiar': {
    linux:{cmd:'cp "entrada con espacios.txt" entrega/copia.txt', tokens:['cp','entrada con espacios.txt','entrega/copia.txt'], note:'En Linux la barra es /. cp sobrescribe sin preguntar: no necesita /Y.'},
    extra:[
      {lvl:'Básico', t:'Dos copias', task:'Crea entrega y copia el archivo dos veces: entrega\\copia1.txt y entrega\\copia2.txt. Lista la carpeta al final.',
        out:'        1 archivo(s) copiado(s).\n        1 archivo(s) copiado(s).\ncopia1.txt\ncopia2.txt\nPADRE:codigo=0', check:['Dos procesos COPY, cada uno validado','El origen se conserva'],
        hint:'Reutiliza ejecutarEn: es una línea por etapa.', lx:'cp dos veces + ls -1 entrega',
        sol:String.raw`ejecutarEn(base, "copy", "/Y", "entrada con espacios.txt", "entrega\\copia1.txt");
ejecutarEn(base, "copy", "/Y", "entrada con espacios.txt", "entrega\\copia2.txt");
ejecutarEn(base, "dir", "/b", "entrega");`},
      {lvl:'Medio', t:'Copiar todos los .txt', task:'Copia todos los .txt de la carpeta a entrega con un único COPY y comodín. Muestra el contenido de entrega.',
        out:'entrada con espacios.txt\n        1 archivo(s) copiado(s).\nentrada con espacios.txt\nPADRE:codigo=0', check:['El comodín lo expande CMD','Destino terminado en barra: es una carpeta'],
        hint:'"*.txt" y "entrega\\" son dos argumentos. COPY muestra los nombres que va copiando.', lx:'en Linux cp no expande *: usa find . -maxdepth 1 -name "*.txt" -exec cp {} entrega/ ;',
        sol:String.raw`ejecutarEn(base, "copy", "/Y", "*.txt", "entrega\\");`},
      {lvl:'Examen', t:'Verificar la copia con FC', task:'Después de copiar, ejecuta fc /b sobre el original y la copia. FC devuelve 0 si son idénticos y 1 si difieren. El hijo imprime COPIA:ok o COPIA:distinta y propaga el código.',
        out:'        1 archivo(s) copiado(s).\nComparando archivos entrada con espacios.txt y ENTREGA\\COPIA.TXT\nFC: no se han encontrado diferencias\nCOPIA:ok\nPADRE:codigo=0', check:['FC es otra etapa con su propio proceso','Usa el código de salida, no el texto de FC','Rutas con espacios como un argumento'],
        hint:'Para este paso necesitas el código sin salir: haz que tu helper devuelva int en lugar de llamar a System.exit.', lx:'cmp "entrada con espacios.txt" entrega/copia.txt',
        sol:String.raw`int fc = ejecutarEn(base, "fc", "/b", "entrada con espacios.txt", "entrega\\copia.txt");
System.out.println(fc == 0 ? "COPIA:ok" : "COPIA:distinta");
System.exit(fc);`}
    ]
  },
  '05-mover': {
    linux:{cmd:'mv "entrada con espacios.txt" archivo/temporal.txt', tokens:['mv','entrada con espacios.txt','archivo/temporal.txt'], note:'Linux no tiene REN: renombrar es mv dentro de la misma carpeta.'},
    extra:[
      {lvl:'Básico', t:'Nombre nuevo con espacios', task:'Igual que el taller, pero el nombre final es "final con espacios.txt". Comprueba que REN lo recibe como un único argumento.',
        out:'final con espacios.txt\nPADRE:codigo=0', check:['El nombre nuevo es un único String','No añades comillas dentro del String Java'],
        hint:'ProcessBuilder ya delimita cada argumento: "final con espacios.txt" sin comillas extra.', lx:'mv archivo/temporal.txt "archivo/final con espacios.txt"',
        sol:String.raw`ejecutarEn(base, "ren", "archivo\\temporal.txt", "final con espacios.txt");`},
      {lvl:'Medio', t:'Deshacer el movimiento', task:'Tras mover y renombrar, devuelve el archivo a su carpeta original con su nombre original usando un solo MOVE. Lista la carpeta base y archivo.',
        out:'entrada con espacios.txt\nsubcarpeta\narchivo\n(archivo vacío)\nPADRE:codigo=0', check:['MOVE puede mover y renombrar a la vez','La carpeta archivo queda vacía','Cada etapa validada'],
        hint:'move /Y archivo\\final.txt "entrada con espacios.txt" — destino con nombre de archivo, no de carpeta.', lx:'mv archivo/final.txt "entrada con espacios.txt"',
        sol:String.raw`ejecutarEn(base, "move", "/Y", "archivo\\final.txt", "entrada con espacios.txt");
ejecutarEn(base, "dir", "/b");`},
      {lvl:'Examen', t:'Archivar por extensión', task:'Crea las carpetas txt y csv si no existen. Mueve *.txt a txt\\ y *.csv a csv\\ con dos MOVE. Termina con dir /s /b de la carpeta para mostrar el resultado.',
        out:'…\\csv\\ignorar.csv\n…\\txt\\entrada con espacios.txt\n…\\subcarpeta\\otro.txt\nPADRE:codigo=0', check:['mkdir condicionado para poder repetir','El comodín lo expande CMD, no Java','Si no hay .csv, decide qué hacer y explícalo'],
        hint:'Prepara los datos con el verificador o crea ignorar.csv a mano. MOVE con comodín y destino carpeta: "*.txt", "txt\\".',
        sol:String.raw`for (String ext : List.of("txt", "csv")) {
    if (!new File(base, ext).exists()) ejecutarEn(base, "mkdir", ext);
    ejecutarEn(base, "move", "/Y", "*." + ext, ext + "\\");
}
ejecutarEn(base, "dir", "/s", "/b");`}
    ]
  },
  '06-leer': {
    linux:{cmd:'cat "entrada con espacios.txt"', tokens:['cat','entrada con espacios.txt']},
    extra:[
      {lvl:'Básico', t:'Numerar las líneas', task:'El hijo imprime cada línea precedida de su número, también las vacías: 1: CONTENIDO_PSP, 2: , 3: segunda linea.',
        out:'1: CONTENIDO_PSP\n2: \n3: segunda linea\nPADRE:codigo=0', check:['La línea vacía cuenta','El bucle termina en null, no en ""'],
        hint:'No uses if (linea.isEmpty()) break: una línea vacía no es el final del archivo.',
        sol:String.raw`int n = 0;
while ((linea = br.readLine()) != null) System.out.println(++n + ": " + linea);`},
      {lvl:'Medio', t:'Leer dos archivos', task:'El hijo muestra entrada con espacios.txt y subcarpeta\\otro.txt con dos TYPE, separados por una línea --- nombre ---. Si el primero falla, no se lee el segundo.',
        out:'--- entrada con espacios.txt ---\nCONTENIDO_PSP\n\nsegunda linea\n--- subcarpeta\\otro.txt ---\nOTRO\nPADRE:codigo=0', check:['Dos procesos TYPE en orden','El separador lo imprime Java','Parada al primer fallo'],
        hint:'Recorre una lista de nombres y llama a ejecutarEn con cada uno.', lx:'cat',
        sol:String.raw`for (String f : List.of("entrada con espacios.txt", "subcarpeta\\otro.txt")) {
    System.out.println("--- " + f + " ---");
    ejecutarEn(base, "type", f);
}`},
      {lvl:'Examen', t:'Buscar texto con FINDSTR', task:'Lanza findstr /n "PSP" sobre el archivo. FINDSTR imprime las líneas que coinciden con su número y devuelve 1 si no encuentra nada. El hijo imprime COINCIDENCIAS:sí/no según el código.',
        out:'1:CONTENIDO_PSP\nCOINCIDENCIAS:sí\nPADRE:codigo=0', check:['El texto buscado es un argumento propio','El código 1 de FINDSTR se interpreta, no se ignora','Explica por qué aquí 1 no es un error'],
        hint:'findstr es un ejecutable (findstr.exe): puede ir con o sin cmd /c. Con tu helper, ve con cmd.', lx:'grep -n PSP "entrada con espacios.txt"',
        sol:String.raw`int c = ejecutarEn(base, "findstr", "/n", "PSP", "entrada con espacios.txt");
System.out.println("COINCIDENCIAS:" + (c == 0 ? "sí" : "no"));
System.exit(c <= 1 ? 0 : c);`}
    ]
  },
  '07-otro-java': {
    linux:{cmd:'java -cp bin estudio.t07.Eco "Ada Lovelace"', tokens:['java','-cp','bin','estudio.t07.Eco','Ada Lovelace'], note:'Idéntico en los dos sistemas: java es un ejecutable.'},
    extra:[
      {lvl:'Básico', t:'Dos argumentos', task:'Eco recibe nombre y edad y responde ECO:Ada Lovelace (36). Hijo pasa los dos argumentos que le llegan del padre.',
        out:'ECO:Ada Lovelace (36)\nPADRE:codigo=0', check:['Dos argumentos, el primero con espacio','Padre → Hijo → Eco sin perder ninguno'],
        hint:'En Eco: args[0] + " (" + args[1] + ")". En Hijo, añade args[1] al ProcessBuilder.',
        sol:String.raw`new ProcessBuilder("java", "-cp", cp, "estudio.t07.Eco", args[0], args[1])`},
      {lvl:'Medio', t:'Código de salida propio', task:'Eco sale con 3 si no recibe argumentos. Lanza Eco sin argumentos y comprueba que el padre acaba mostrando PADRE:codigo=3.',
        out:'ECO:falta el nombre\nPADRE:codigo=3', check:['El 3 viaja por las dos esperas','Nadie lo convierte en 0 ni en 1'],
        hint:'System.exit(3) en Eco; Hijo hace System.exit(eco.waitFor()); Padre igual.',
        sol:String.raw`// Eco
if (args.length == 0) { System.out.println("ECO:falta el nombre"); System.exit(3); }`},
      {lvl:'Examen', t:'Escribir en la entrada del nieto', task:'Eco lee una línea de System.in y la devuelve en mayúsculas. Hijo escribe el nombre en getOutputStream() del proceso Eco, cierra ese flujo y después lee su salida.',
        out:'ECO:ADA LOVELACE\nPADRE:codigo=0', check:['getOutputStream() escribe en stdin del hijo','Cerrar la escritura antes de leer evita una espera infinita','Nombres de flujos desde el punto de vista del padre'],
        hint:'try (PrintWriter w = new PrintWriter(eco.getOutputStream())) { w.println(nombre); } — al cerrarse, Eco recibe fin de entrada.',
        sol:String.raw`Process eco = new ProcessBuilder("java", "-cp", cp, "estudio.t07.Eco")
        .redirectErrorStream(true).start();
try (PrintWriter w = new PrintWriter(eco.getOutputStream())) {
    w.println(nombre);
}
// ... leer eco.getInputStream() hasta null y waitFor()`}
    ]
  },
  '08-crear-jar': {
    linux:{cmd:'jar cfe Aplicacion.jar Saludo -C clases .', tokens:['jar','cfe','Aplicacion.jar','Saludo','-C','clases','.'], note:'javac, jar y java funcionan igual. Solo cambia el separador de rutas.'},
    extra:[
      {lvl:'Básico', t:'Otra aplicación', task:'Con Despedida.java (imprime ADIOS: + args[0]) crea Despedida.jar y ejecútalo con tu nombre.',
        out:'ADIOS:Ada\nPADRE:codigo=0', check:['Clase principal y nombre de JAR coherentes','Tres etapas validadas'],
        hint:'Cambia nombres en las tres órdenes. El orden de cfe es: archivo, luego clase principal.',
        sol:String.raw`ejecutar(new ProcessBuilder("jar", "cfe", "Despedida.jar", "Despedida", "-C", "clases", ".").directory(base));`},
      {lvl:'Medio', t:'Inspeccionar el JAR', task:'Entre jar cfe y java -jar, añade una etapa jar tf Aplicacion.jar que liste su contenido. Deben aparecer META-INF/MANIFEST.MF y Saludo.class.',
        out:'META-INF/\nMETA-INF/MANIFEST.MF\nSaludo.class\nSALUDO:Ada\nPADRE:codigo=0', check:['jar tf es otro proceso','Saludo.class en la raíz, no en clases/'],
        hint:'Si ves clases/Saludo.class, te falta -C clases . al crear el JAR.',
        sol:String.raw`ejecutar(new ProcessBuilder("jar", "tf", "Aplicacion.jar").directory(base));`},
      {lvl:'Examen', t:'Dos clases en un paquete', task:'Saludo usa Formato.texto(nombre). Ambas en package app (carpeta app\\). Compila las dos, crea el JAR con app.Saludo como principal y ejecútalo.',
        out:'SALUDO:<<Ada>>\nPADRE:codigo=0', check:['javac recibe los dos fuentes','Main-Class con nombre completo app.Saludo','-C clases . sigue apuntando a la raíz'],
        hint:'javac -d clases app\\Saludo.java app\\Formato.java ; jar cfe Aplicacion.jar app.Saludo -C clases .',
        sol:String.raw`ejecutar(new ProcessBuilder("javac", "-d", "clases",
        "app\\Saludo.java", "app\\Formato.java").directory(base));
ejecutar(new ProcessBuilder("jar", "cfe", "Aplicacion.jar", "app.Saludo",
        "-C", "clases", ".").directory(base));`}
    ]
  },
  '09-integrador': {
    linux:{cmd:'ls -1 → mkdir → cp → cat', tokens:['ls -1','mkdir informe','cp origen destino','cat informe/resultado.txt']},
    extra:[
      {lvl:'Básico', t:'Informe con dos archivos', task:'Copia entrada con espacios.txt y subcarpeta\\otro.txt a informe y muestra ambos con TYPE.',
        out:'…\nCONTENIDO_PSP\nsegunda linea\nOTRO\nPADRE:codigo=0', check:['Dos COPY y dos TYPE validados','Los originales se conservan'],
        hint:'Cuatro llamadas a ejecutarEn; cada una se detiene si falla.', lx:'cp + cat',
        sol:String.raw`ejecutarEn(base, "copy", "/Y", "subcarpeta\\otro.txt", "informe\\otro.txt");
ejecutarEn(base, "type", "informe\\otro.txt");`},
      {lvl:'Medio', t:'Limpieza al final', task:'Después de mostrar el informe, borra informe\\resultado.txt con DEL y demuestra con dir /b informe que ya no está. Trabaja solo en la carpeta de pruebas.',
        out:'CONTENIDO_PSP\nsegunda linea\n(dir vacío o "No se encuentra el archivo")\nPADRE:codigo=?', check:['DEL solo sobre un archivo creado por el programa','Decide qué código esperas del DIR vacío y justifícalo'],
        hint:'dir /b de una carpeta vacía devuelve 1 en CMD: es la misma decisión que en el ejercicio de contar coincidencias.', lx:'rm informe/resultado.txt ; ls -1 informe (devuelve 0)',
        sol:String.raw`ejecutarEn(base, "del", "informe\\resultado.txt");
int c = ejecutarEn(base, "dir", "/b", "informe");`},
      {lvl:'Examen', t:'Demostrar la parada', task:'Prepara dos carpetas: una correcta y otra sin entrada con espacios.txt. Ejecuta el padre con ambas. En la segunda debe verse dir, mkdir, el error de copy y PADRE:codigo=1, sin TYPE posterior. Anota qué etapa falló y por qué.',
        out:'(carpeta sin entrada)\nsubcarpeta\nEl sistema no puede encontrar el archivo especificado.\nPADRE:codigo=1', check:['El fallo se ve en tu terminal','No se ejecuta ninguna etapa después del fallo','Sabes explicar IOException frente a código no cero'],
        hint:'Usa el escenario «Con error» de la escena para predecirlo antes de ejecutarlo.',
        sol:String.raw`// Sin cambios en el código: la prueba es el diseño.
// Cada ejecutarEn hace System.exit(codigo) al primer código no cero.`}
    ]
  }
};
