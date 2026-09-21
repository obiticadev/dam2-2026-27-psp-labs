# Ejercicio Extra 2: Comandos y Gestión de Procesos

Proyecto que ilustra la ejecución de comandos del sistema operativo, captura de flujos estándar y llamadas entre procesos mediante `Runtime` y `ProcessBuilder`.

---

## Clases disponibles

1. **`psp2.Psp2` (`src/psp2/Psp2.java`)**:
   - Lanza el ejecutable `Psp.jar` pasándole la URL `www.youtube.com` mediante `Runtime.getRuntime().exec("java -jar Psp.jar " + cadena)`.

2. **`psp2.PspDir` (`src/psp2/PspDir.java`)**:
   - Ejecuta el comando de listado del sistema con `Runtime.getRuntime().exec` y captura su salida estándar mediante `BufferedReader`.

3. **`psp2.ProcBuild` (`src/psp2/ProcBuild.java`)**:
   - Ejecuta un comando del sistema mediante `ProcessBuilder`, lee su salida por consola y muestra variables de entorno (`System.getenv()`) y el sistema operativo (`os.name`).

---

## Compilación y Ejecución

### Compilar todas las clases:
```bash
javac -d bin src/psp2/*.java
```

### Ejecutar las clases individuales:
```bash
# Ejecutar PspDir
java -cp bin psp2.PspDir

# Ejecutar ProcBuild
java -cp bin psp2.ProcBuild

# Ejecutar Psp2 (requiere Psp.jar en el directorio de trabajo)
java -cp bin psp2.Psp2
```
