---
name: Autoestudio PSP
description: Sistema visual local para comprender y practicar procesos Java.
colors:
  ink: "#183d50"
  ink-deep: "#102b39"
  muted: "#536773"
  paper: "#fff"
  ground: "#f2f5f6"
  line: "#d6e0e4"
  accent: "#f4c85a"
  accent-light: "#fff6db"
  blue: "#2765a0"
  green: "#216247"
  red: "#a53635"
  code: "#132f3e"
  neutral-hover: "#e8eef1"
  field: "#fcfdfe"
typography:
  display:
    fontFamily: "Manrope, 'Segoe UI', Arial, sans-serif"
    fontSize: "clamp(2.1rem, 3.4vw, 3.6rem)"
    fontWeight: 800
    lineHeight: 1.2
    letterSpacing: "-.038em"
  headline:
    fontFamily: "Manrope, 'Segoe UI', Arial, sans-serif"
    fontSize: "1.65rem"
    fontWeight: 700
    lineHeight: 1.2
    letterSpacing: "-.025em"
  title:
    fontFamily: "Manrope, 'Segoe UI', Arial, sans-serif"
    fontSize: "1.12rem"
    fontWeight: 700
    lineHeight: 1.2
    letterSpacing: "-.015em"
  body:
    fontFamily: "'Segoe UI', Arial, sans-serif"
    fontSize: "16px"
    lineHeight: 1.65
  label:
    fontFamily: "'Segoe UI', Arial, sans-serif"
    fontSize: ".85rem"
    fontWeight: 600
    lineHeight: 1.5
  mono:
    fontFamily: "Consolas, 'Liberation Mono', monospace"
    fontSize: ".83rem"
    lineHeight: 1.7
rounded:
  inline: "3px"
  compact: "4px"
  control: "5px"
  note: "6px"
  code: "7px"
  visual: "8px"
  radius: "10px"
  scene: "12px"
spacing:
  gap-8: "8px"
  gap-10: "10px"
  gap-12: "12px"
  gap-16: "16px"
  inset-18: "18px"
  inset-20: "20px"
  inset-22: "22px"
  inset-24: "24px"
  inset-32: "32px"
  page: "42px"
components:
  button-primary:
    backgroundColor: "{colors.ink}"
    textColor: "{colors.paper}"
    typography: "{typography.label}"
    rounded: "{rounded.control}"
    padding: "11px 16px"
  button-primary-hover:
    backgroundColor: "{colors.ink-deep}"
  button-secondary:
    backgroundColor: "{colors.paper}"
    textColor: "{colors.ink}"
    rounded: "{rounded.control}"
    padding: "11px 16px"
  button-secondary-hover:
    backgroundColor: "{colors.neutral-hover}"
  button-accent:
    backgroundColor: "{colors.accent}"
    textColor: "{colors.ink-deep}"
    rounded: "{rounded.control}"
    padding: "11px 16px"
  answer-field:
    backgroundColor: "{colors.field}"
    textColor: "{colors.ink}"
    rounded: "{rounded.control}"
    padding: "16px"
    width: "100%"
  navigation-active:
    backgroundColor: "{colors.accent}"
    textColor: "{colors.ink-deep}"
    rounded: "{rounded.control}"
    padding: "10px 12px"
  article:
    backgroundColor: "{colors.paper}"
    textColor: "{colors.ink}"
    rounded: "{rounded.radius}"
    padding: "32px"
  scene:
    backgroundColor: "{colors.paper}"
    rounded: "{rounded.scene}"
  command-token:
    backgroundColor: "{colors.paper}"
    textColor: "{colors.ink}"
    rounded: "{rounded.compact}"
    padding: "8px 10px"
---

# Design System: Autoestudio PSP

## Overview

**Creative North Star: "Atlas secuencial"**

Metáfora y descripciones cualitativas: elección de implementación, no nueva declaración de marca del usuario. El sistema combina tinta azul, papel frío y azafrán con diagramas etiquetados. La jerarquía tipográfica distingue explicación, acción y código sin ornamentación ajena al aprendizaje.

Extracción del sistema construido en `autoestudio/UT01-procesos/panel/styles.css`; los tokens anteriores son normativos. El alcance observado es el panel local, no las prácticas Java ni otros proyectos.

**Key Characteristics:**
- Lectura amplia con controles compactos.
- Tinta estructural y azafrán para selección y actividad.
- Movimiento causal, detenible y compatible con movimiento reducido.

## Colors

Los nombres evocativos son elecciones descriptivas de implementación; las claves conservan el vocabulario CSS.
Las rampas OKLCH del sidecar son interpolaciones ilustrativas sintetizadas para el panel de diseño, no colores adicionales implementados ni sustitutos de los valores normativos.

### Primary
- **Tinta azul** (`ink`, `ink-deep`): texto principal, navegación y acciones; el tono profundo refuerza el hover primario.
- **Azafrán** (`accent`, `accent-light`): navegación seleccionada, actor activo y fondos de explicación.

### Secondary
- **Azul enlace** (`blue`): enlaces, caret y foco de teclado.
- **Verde comprobación** (`green`): finalización y estados positivos.
- **Rojo reservado** (`red`): variable declarada; las alertas actuales usan tintes propios, no esta variable.

### Neutral
- **Papel y suelo frío** (`paper`, `ground`): superficies y fondo exterior.
- **Texto auxiliar y divisores** (`muted`, `line`): jerarquía secundaria y separación.
- **Pizarra de código** (`code`): paneles de código oscuros.
- **Gris interactivo y campo** (`neutral-hover`, `field`): hover secundario, código inline y respuesta escrita.

## Typography

Manrope local para títulos; Segoe UI con Arial como alternativa para lectura y controles; Consolas con Liberation Mono para código, rutas, numeración técnica y reloj. No hay escala proporcional única: los tamaños responden a su función.

La jerarquía base está en el frontmatter; los titulares secundarios usan el peso negrita nativo del navegador. La portada amplía el título a `clamp(2.8rem,4.4vw,4.9rem)` con interlineado (1.08). Los párrafos se limitan a (72ch), o (68ch) en cabecera. Las etiquetas secundarias seleccionadas terminan en (.75rem) y las líneas de código en (.78rem): las reglas finales prevalecen sobre los tamaños anteriores y sus media queries.

## Layout

Rejilla de navegación persistente (256px) más contenido flexible; contenedor principal hasta (1560px), padding base del token `page`. La escena divide diagrama y código en proporción (1.1:1); artículos con anotaciones usan (1.7:1), separación (32px).

A (1200px) o menos, navegación (220px) y padding principal (30px). A (1000px) o menos, escena, artículo y simulacro pasan a una columna. A (700px) o menos, menú lateral desplazable (268px), barra superior sticky (63px) y padding principal (28px 18px). Desde (1600px), padding (48px 56px). Los controles se redistribuyen sin quitar pasos. Impresión elimina navegación y controles, aplana columnas y revela respuestas modelo.

## Elevation & Depth

Sistema principalmente plano: bordes finos y contraste tonal organizan el contenido. Escena y toast comparten sombra ambiental; el menú móvil añade sombra lateral. Los valores exactos están en el sidecar. Los artículos no llevan sombra.

## Shapes

Controles discretamente redondeados; notas y bloques usan radios intermedios; artículos y escenas, las esquinas más suaves. Divisores de (1px), pestañas con indicador inferior de (3px). Solo señales circulares y geometría de diagrama donde expresan un mecanismo.

## Components

- **Buttons:** primario tinta, secundario papel y acento azafrán; borde (1px), padding y radios en tokens. El hover del acento usa `accent-light` y texto `ink-deep` para conservar contraste. Deshabilitados con opacidad (.42) y cursor de bloqueo.
- **Inputs / Fields:** respuesta multilínea redimensionable, mínimo (175px), borde gris y caret azul. Foco global de (3px) separado (4px). No se define un estado de error de campo.
- **Navigation:** enlaces compactos sobre tinta; hover azul apagado, selección azafrán con peso (650). En móvil se convierte en panel desplazable. Pestañas mantienen indicador inferior.
- **Cards / Containers:** artículo blanco con borde, sin elevación; anotaciones en fondo azafrán claro. Radios y padding según tokens.
- **Chips:** fragmentos de comando en mono, con borde; primer fragmento tinta con texto blanco. Son explicativos, no filtros.
- **Scene:** actores, señal y línea de código actual coordinados. Actor activo se eleva (-5px) y escala (1.07); señal recorre posiciones (16%, 49%, 82%). Controles temporales y fullscreen conservan etiquetas. Movimiento reducido elimina transiciones y señal, manteniendo el estado activo cromático.

## Do's and Don'ts

### Do:
- **Do** reutilizar los tokens de tinta, papel y azafrán según su función.
- **Do** mantener foco visible y etiquetas textuales junto al estado visual.
- **Do** respetar el orden de lectura al apilar columnas.

### Don't:
- **Don't** convertir los artículos planos en tarjetas elevadas.
- **Don't** usar movimiento decorativo para representar progreso técnico.
- **Don't** tratar una simulación visual como ejecución comprobada.
