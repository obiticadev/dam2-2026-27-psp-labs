/* Guiones de las escenas: qué ejecuta cada proceso, qué imprime y qué cambia en disco.
   Las salidas imitan CMD en español; son ilustrativas, no una ejecución real. */
(() => {
  const CMD_HELPER = [
    'static void ejecutarEn(File dir, String... orden) throws Exception {',
    '    List<String> cmd = new ArrayList<>(List.of("cmd", "/d", "/c"));',
    '    cmd.addAll(List.of(orden));',
    '    Process p = new ProcessBuilder(cmd).directory(dir)',
    '            .redirectErrorStream(true).start();',
    '    BufferedReader br = new BufferedReader(',
    '            new InputStreamReader(p.getInputStream()));',
    '    String linea;',
    '    while ((linea = br.readLine()) != null) System.out.println(linea);',
    '    int codigo = p.waitFor();',
    '    if (codigo != 0) System.exit(codigo);',
    '}'
  ];
  // Referencias de línea del helper según dónde empiece.
  const cmdRefs = h => ({start:h+3, relay:h+8, wait:h+9, fail:h+10});
  const base = ['entrada con espacios.txt', 'subcarpeta/', 'subcarpeta/otro.txt'];
  const NOFILE = 'El sistema no puede encontrar el archivo especificado.';

  window.PSP_SCENES = {
    '01-ping': {
      arg:'127.0.0.1', argName:'destino', relay:'full',
      hijo:['String destino = args[0];','ProcessBuilder pb = new ProcessBuilder(','        "ping", "-n", "2", destino);','pb.redirectErrorStream(true);','Process ping = pb.start();','BufferedReader br = new BufferedReader(','        new InputStreamReader(ping.getInputStream()));','String linea;','while ((linea = br.readLine()) != null) {','    System.out.println(linea);','}','int codigo = ping.waitFor();','System.exit(codigo);'],
      refs:{start:4, relay:9, wait:11, fail:12}, exit:12,
      stages:[{px:'ping.exe', tok:'ping -n 2 127.0.0.1',
        d:'ping es un ejecutable: cada opción viaja como un String separado. No hace falta cmd /c.',
        o:'ping escribe en su salida estándar. Todavía nadie la ve: está en la tubería hacia el hijo.',
        out:['Haciendo ping a 127.0.0.1 con 32 bytes de datos:','Respuesta desde 127.0.0.1: bytes=32 tiempo<1m TTL=128','Respuesta desde 127.0.0.1: bytes=32 tiempo<1m TTL=128','','Estadísticas de ping para 127.0.0.1:','    Paquetes: enviados = 2, recibidos = 2, perdidos = 0']}],
      fail:{label:'Host inexistente', arg:'noexiste', stage:0, code:1, tok:'ping -n 2 noexiste',
        out:['La solicitud de ping no pudo encontrar el host noexiste. Compruebe el nombre y vuelva a intentarlo.']}
    },
    '02-directorios': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', relay:'full', fs:base,
      hijo:['String carpeta = args[0];','ProcessBuilder pb = new ProcessBuilder(','        "cmd", "/d", "/c", "dir", "/b");','pb.directory(new File(carpeta));','pb.redirectErrorStream(true);','Process cmd = pb.start();','BufferedReader br = new BufferedReader(','        new InputStreamReader(cmd.getInputStream()));','String linea;','while ((linea = br.readLine()) != null) {','    System.out.println(linea);','}','int codigo = cmd.waitFor();','System.exit(codigo);'],
      refs:{start:5, relay:10, wait:12, fail:13}, exit:13,
      stages:[{px:'cmd.exe · dir', tok:'cmd /d /c dir /b',
        pre:[{t:'directory() fija la carpeta', d:'La carpeta de trabajo es del proceso nuevo. No se concatena la ruta al texto de CMD y el padre no cambia de carpeta.', ln:3}],
        d:'DIR es una orden interna: quien arranca es cmd.exe. /c ejecuta la orden y termina; /d ignora AutoRun.',
        o:'DIR lista la carpeta configurada. El lector del hijo recibe texto, no objetos File.',
        out:['entrada con espacios.txt','subcarpeta']}],
      fail:{label:'Carpeta inexistente', arg:'C:\\PSP\\no existe', stage:0, start:false, code:1,
        out:['No se pudo iniciar o leer el proceso: Cannot run program "cmd" (in directory "C:\\PSP\\no existe")']}
    },
    '03-buscar': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', fs:[...base,'ignorar.csv'],
      hijo:['String carpeta = args[0];','ProcessBuilder pb = new ProcessBuilder("cmd", "/d", "/c",','        "dir", "/s", "/b", "*.txt");','pb.directory(new File(carpeta));','pb.redirectErrorStream(true);','Process cmd = pb.start();','BufferedReader br = new BufferedReader(','        new InputStreamReader(cmd.getInputStream()));','String linea;','while ((linea = br.readLine()) != null) {','    System.out.println(linea);','}','int codigo = cmd.waitFor();','System.exit(codigo);'],
      refs:{start:5, relay:10, wait:12, fail:13}, exit:13,
      stages:[{px:'cmd.exe · dir /s', tok:'cmd /d /c dir /s /b *.txt',
        pre:[{t:'El comodín viaja intacto', d:'*.txt es un único argumento. Java no lo expande: lo interpreta DIR dentro de CMD.', ln:2}],
        d:'/s entra en subcarpetas y /b imprime rutas completas, sin cabecera ni resumen.',
        o:'Aparecen las dos coincidencias, también la de subcarpeta. ignorar.csv no cumple el patrón.',
        out:['C:\\PSP\\zona de pruebas\\entrada con espacios.txt','C:\\PSP\\zona de pruebas\\subcarpeta\\otro.txt']}],
      fail:{label:'Sin coincidencias', stage:0, code:1, tok:'cmd /d /c dir /s /b *.java', out:['No se encuentra el archivo']}
    },
    '04-copiar': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', fs:base,
      hijo:['File base = new File(args[0]);','if (!new File(base, "entrega").exists())','    ejecutarEn(base, "mkdir", "entrega");','ejecutarEn(base, "copy", "/Y",','        "entrada con espacios.txt", "entrega\\\\copia.txt");','ejecutarEn(base, "dir", "/b", "entrega");','System.exit(0); // todas las etapas han ido bien','',...CMD_HELPER],
      refs:cmdRefs(8), exit:6,
      stages:[
        {px:'cmd.exe · mkdir', tok:'mkdir entrega', ln:2, add:['entrega/'], d:'Primera etapa. Solo se crea entrega si no existe: repetir el taller no debe parecer un fallo.'},
        {px:'cmd.exe · copy', tok:'copy /Y "entrada con espacios.txt" entrega\\copia.txt', ln:3, add:['entrega/copia.txt'], d:'Solo se llega aquí si mkdir devolvió 0. El nombre con espacios es un único argumento.', out:['        1 archivo(s) copiado(s).']},
        {px:'cmd.exe · dir', tok:'dir /b entrega', ln:5, d:'DIR confirma el resultado. El origen sigue en su sitio: copiar no es mover.', out:['copia.txt']}],
      fail:{label:'Falta el archivo de entrada', stage:1, code:1, del:['entrada con espacios.txt'], out:[NOFILE]}
    },
    '05-mover': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', fs:base,
      hijo:['File base = new File(args[0]);','if (!new File(base, "archivo").exists())','    ejecutarEn(base, "mkdir", "archivo");','ejecutarEn(base, "move", "/Y",','        "entrada con espacios.txt", "archivo\\\\temporal.txt");','ejecutarEn(base, "ren", "archivo\\\\temporal.txt", "final.txt");','ejecutarEn(base, "dir", "/b", "archivo");','System.exit(0);','',...CMD_HELPER],
      refs:cmdRefs(9), exit:7,
      stages:[
        {px:'cmd.exe · mkdir', tok:'mkdir archivo', ln:2, add:['archivo/'], d:'Se prepara el destino. Si ya existe, no se vuelve a crear.'},
        {px:'cmd.exe · move', tok:'move /Y "entrada con espacios.txt" archivo\\temporal.txt', ln:3, add:['archivo/temporal.txt'], del:['entrada con espacios.txt'], d:'MOVE cambia la ubicación: el archivo desaparece del origen.', out:['        1 archivo(s) movido(s).']},
        {px:'cmd.exe · ren', tok:'ren archivo\\temporal.txt final.txt', ln:5, add:['archivo/final.txt'], del:['archivo/temporal.txt'], d:'REN recibe el nombre nuevo, no otra ruta. El archivo no cambia de carpeta.'},
        {px:'cmd.exe · dir', tok:'dir /b archivo', ln:6, d:'El listado confirma el estado final.', out:['final.txt']}],
      fail:{label:'Falta el archivo de origen', stage:1, code:1, del:['entrada con espacios.txt'], out:[NOFILE]}
    },
    '06-leer': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', fs:base,
      hijo:['File base = new File(args[0]);','ejecutarEn(base, "type", "entrada con espacios.txt");','System.exit(0);','',...CMD_HELPER],
      refs:cmdRefs(4), exit:2,
      stages:[{px:'cmd.exe · type', tok:'type "entrada con espacios.txt"', ln:1,
        d:'TYPE solo consulta el archivo: no lo modifica.',
        o:'La línea vacía es una cadena "", no el final. El bucle solo termina cuando readLine() devuelve null.',
        out:['CONTENIDO_PSP','','segunda linea']}],
      fail:{label:'Archivo inexistente', stage:0, code:1, del:['entrada con espacios.txt'], out:[NOFILE]}
    },
    '07-otro-java': {
      arg:'Ada Lovelace', argName:'nombre', relay:'full',
      hijo:['String nombre = args[0];','String cp = System.getProperty("java.class.path");','ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp,','        "estudio.t07.Eco", nombre);','pb.redirectErrorStream(true);','Process eco = pb.start();','BufferedReader br = new BufferedReader(','        new InputStreamReader(eco.getInputStream()));','String linea;','while ((linea = br.readLine()) != null) {','    System.out.println(linea);','}','int codigo = eco.waitFor();','System.exit(codigo);'],
      refs:{start:5, relay:10, wait:12, fail:13}, exit:13,
      stages:[{px:'Eco · tercera JVM', tok:'java -cp bin estudio.t07.Eco "Ada Lovelace"',
        d:'Otra JVM, no una llamada a Eco.main(). "Ada Lovelace" sigue siendo un solo argumento.',
        o:'Eco imprime args[0]. Si se hubiera partido por el espacio, saldría solo Ada.',
        out:['ECO:Ada Lovelace']}],
      fail:{label:'Classpath incorrecto', stage:0, code:1, tok:'java -cp bin\\estudio\\t07 estudio.t07.Eco',
        out:['Error: no se ha encontrado o cargado la clase principal estudio.t07.Eco']}
    },
    '08-crear-jar': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', fs:['Saludo.java'],
      hijo:['File base = new File(args[0]);','ejecutar(new ProcessBuilder("javac", "-encoding", "UTF-8",','        "-d", "clases", "Saludo.java").directory(base));','ejecutar(new ProcessBuilder("jar", "cfe", "Aplicacion.jar",','        "Saludo", "-C", "clases", ".").directory(base));','ejecutar(new ProcessBuilder("java", "-jar",','        "Aplicacion.jar", "Ada").directory(base));','System.exit(0); // tres etapas correctas','','static void ejecutar(ProcessBuilder pb) throws Exception {','    Process p = pb.redirectErrorStream(true).start();','    BufferedReader br = new BufferedReader(','            new InputStreamReader(p.getInputStream()));','    String linea;','    while ((linea = br.readLine()) != null) System.out.println(linea);','    int codigo = p.waitFor();','    if (codigo != 0) System.exit(codigo);','}'],
      refs:{start:10, relay:14, wait:15, fail:16}, exit:7,
      stages:[
        {px:'javac', tok:'javac -encoding UTF-8 -d clases Saludo.java', ln:1, add:['clases/','clases/Saludo.class'], d:'javac es un ejecutable del JDK. Genera bytecode en clases; si falla, la cadena se detiene.'},
        {px:'jar', tok:'jar cfe Aplicacion.jar Saludo -C clases .', ln:3, add:['Aplicacion.jar'], d:'cfe: crear, archivo y punto de entrada. -C clases . incluye las clases desde su raíz.'},
        {px:'java -jar', tok:'java -jar Aplicacion.jar Ada', ln:5, d:'El JAR no se ejecuta solo: java crea un proceso que lee el manifiesto.', out:['SALUDO:Ada']}],
      fail:{label:'Error de compilación', stage:0, code:1,
        out:['Saludo.java:1: error: <identifier> expected','public class Saludo { ERROR SINTAXIS }','                          ^','1 error']}
    },
    '09-integrador': {
      arg:'C:\\PSP\\zona de pruebas', argName:'carpeta', fs:base,
      hijo:['File base = new File(args[0]);','ejecutarEn(base, "dir", "/b");','if (!new File(base, "informe").exists())','    ejecutarEn(base, "mkdir", "informe");','ejecutarEn(base, "copy", "/Y",','        "entrada con espacios.txt", "informe\\\\resultado.txt");','ejecutarEn(base, "type", "informe\\\\resultado.txt");','System.exit(0);','',...CMD_HELPER],
      refs:cmdRefs(9), exit:7,
      stages:[
        {px:'cmd.exe · dir', tok:'dir /b', ln:1, d:'Primero se observa el estado inicial de la carpeta.', out:['entrada con espacios.txt','subcarpeta']},
        {px:'cmd.exe · mkdir', tok:'mkdir informe', ln:3, add:['informe/'], d:'Se crea el destino solo si hace falta.'},
        {px:'cmd.exe · copy', tok:'copy /Y "entrada con espacios.txt" informe\\resultado.txt', ln:4, add:['informe/resultado.txt'], d:'COPY conserva el origen y crea el informe.', out:['        1 archivo(s) copiado(s).']},
        {px:'cmd.exe · type', tok:'type informe\\resultado.txt', ln:6, d:'TYPE recupera el contenido copiado para comprobarlo.', out:['CONTENIDO_PSP','segunda linea']}],
      fail:{label:'Falla la copia', stage:2, code:1, del:['entrada con espacios.txt'], out:[NOFILE]}
    }
  };
})();
