@echo off
title Orbita Cero
cd /d "%~dp0"

where java >nul 2>nul
if errorlevel 1 (
    echo Java no esta instalado o no esta agregado al PATH.
    echo Instala Java 11 y vuelve a intentarlo.
    pause
    exit /b 1
)

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
)
