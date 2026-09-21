# Ejercicio Extra 1: Llamada a Navegador

Proyecto que demuestra la invocación de un navegador web externo pasando una URL como argumento mediante `Runtime.getRuntime().exec()`.

---

## Estructura

- `src/psp/Psp.java`: Clase principal con el método `main(String[] args)`.
- `bin/`: Salida de compilación.
- `lib/`: Librerías auxiliares.
- `dist/Psp.jar`: Archivo JAR empaquetado del proyecto.

---

## Compilación y Ejecución

### Compilar:
```bash
javac -d bin src/psp/Psp.java
```

### Ejecutar:
```bash
java -cp bin psp.Psp "https://www.youtube.com"
```

### Generar el JAR:
```bash
jar cfe dist/Psp.jar psp.Psp -C bin .
```
