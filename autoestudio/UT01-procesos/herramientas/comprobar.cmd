@echo off
setlocal
pushd "%~dp0.."
where java >nul 2>nul
if errorlevel 1 (
  echo Falta el JDK en PATH. Instala JDK 17 o posterior y abre otra terminal CMD.
  popd
  exit /b 1
)
java herramientas\Verificar.java %*
set "resultado=%errorlevel%"
popd
exit /b %resultado%
