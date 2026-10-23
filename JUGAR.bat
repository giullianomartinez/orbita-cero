@echo off
setlocal
title Orbita Cero
cd /d "%~dp0"

if not defined ORBITA_JDK11_HOME set "ORBITA_JDK11_HOME=%USERPROFILE%\.jdks\temurin-11.0.32+101"
if not exist "%ORBITA_JDK11_HOME%\bin\java.exe" (
    echo No se encontro Java 11 en:
    echo %ORBITA_JDK11_HOME%
    echo Instala un JDK 11 o define ORBITA_JDK11_HOME y vuelve a intentarlo.
    pause
    exit /b 1
)
set "JAVA_HOME=%ORBITA_JDK11_HOME%"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Ejecutando el juego con Java 11:
java -version

if not exist "gradlew.bat" (
    echo No se encontro gradlew.bat. Ejecuta este archivo desde la carpeta del proyecto.
    pause
    exit /b 1
)

call gradlew.bat :lwjgl3:run
if errorlevel 1 (
    echo.
    echo El juego no pudo iniciarse. Revisa el mensaje anterior.
    pause
    exit /b 1
)
endlocal
