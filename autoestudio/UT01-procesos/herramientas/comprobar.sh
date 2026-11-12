#!/bin/sh
# Equivalente Linux de comprobar.cmd: prueba la rama Linux de los talleres.
cd "$(dirname "$0")/.." || exit 1
command -v java >/dev/null 2>&1 || { echo "Falta el JDK en PATH. Instala JDK 17 o posterior."; exit 1; }
exec java herramientas/Verificar.java "$@"
