@echo off
echo ====================================
echo Limpiando proyecto Gradle...
echo ====================================
echo.

cd /d "%~dp0"

echo [1/6] Deteniendo daemons de Gradle...
call gradlew --stop

echo.
echo [2/6] Eliminando carpetas de build...
if exist "app\build\" (
    echo Eliminando app\build...
    rmdir /s /q "app\build"
)
if exist "build\" (
    echo Eliminando build...
    rmdir /s /q "build"
)

echo.
echo [3/6] Eliminando cache de Gradle local...
if exist ".gradle\" (
    echo Eliminando .gradle...
    rmdir /s /q ".gradle"
)

echo.
echo [4/6] Limpiando cache de KSP...
if exist "app\build\kspCaches\" (
    rmdir /s /q "app\build\kspCaches"
)

echo.
echo [5/6] Ejecutando Gradle clean...
call gradlew clean

echo.
echo [6/6] Haciendo build del proyecto...
call gradlew build --refresh-dependencies

echo.
echo ====================================
echo Limpieza completada!
echo ====================================
echo.
echo Si el build fue exitoso, ahora puedes ejecutar:
echo   gradlew assembleDebug
echo.
pause

