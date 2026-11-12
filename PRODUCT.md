# Autoestudio PSP

<!-- impeccable:product-schema 1 -->

## Platform

web

## Stack

HTML, CSS y JavaScript estáticos para el dashboard local; Java 17 para los talleres.
La propuesta de web local sin servicios externos y trabajo real en VS Code fue
presentada al usuario, que autorizó su implementación. No hay despliegue solicitado.

## Users

Estudiante de segundo de DAM que prepara PSP en VS Code. El examen se realiza
100 % en Windows. Practica tanto en Windows como en Linux: el panel tiene un
selector de sistema y las soluciones llevan rama CMD y rama Linux.

## Product Purpose

Comprender, practicar y reproducir sin plantilla el patrón padre Java → hijo Java
→ comando CMD, y responder preguntas conceptuales en cuatro o cinco líneas.

## Operating Context

El dashboard se consulta junto a VS Code. El navegador simula, no ejecuta comandos
del equipo. Los programas reales se ejecutan en CMD. La preparación no depende de
red ni de instalar aplicaciones gráficas. Ping externo es una práctica manual.

## Capabilities and Constraints

- Cada taller tiene teoría, patrón, animación controlable, pistas y comprobaciones.
- Comandos: ping, dir, mkdir, copy, move, ren, type, javac, jar y java.
- Alcance práctico UT01; hilos solo como conceptos, diferencias y ventajas.
- Respuestas modelo diferenciadas de citas docentes, con fuentes localizables.
- No alterar Obsidian, prácticas existentes ni intentos del estudiante.
- Sin publicar ni hacer commits como efecto secundario.
- Los tests Linux no certifican ejecución en Windows.

## Evidence on Hand

Modelo: autoestudio de dam2-2026-27-ad-labs. Fuentes: bóveda DAM2, PSP UT01,
teoría conceptual de hilos UT02 y prácticas Ejercicio_1_* de este repositorio.
No existe en la bóveda una nota de examen PSP anterior a esta petición.

## Product Principles

1. Mostrar el mecanismo antes de pedir que se memorice.
2. Practicar al nivel del ejercicio de YouTube, con variantes desde cero.
3. Diferenciar simulación, resultado esperado y ejecución comprobada.
4. No esconder javac/jar/java detrás de herramientas de construcción.
