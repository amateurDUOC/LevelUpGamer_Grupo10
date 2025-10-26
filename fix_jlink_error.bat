@echo off
echo ========================================
echo Solucion Error JLINK - Gradle
echo ========================================
echo.

echo Paso 1: Deteniendo procesos de Gradle...
call gradlew --stop

echo.
echo Paso 2: Limpiando caches de Gradle...
rmdir /s /q .gradle 2>nul
rmdir /s /q build 2>nul
rmdir /s /q app\build 2>nul

echo.
echo Paso 3: Limpiando cache del usuario de Gradle (opcional)...
echo (Esto puede tardar un momento...)
rmdir /s /q %USERPROFILE%\.gradle\caches\transforms-4 2>nul
rmdir /s /q %USERPROFILE%\.gradle\caches\8.7 2>nul
rmdir /s /q %USERPROFILE%\.gradle\caches\8.13 2>nul

echo.
echo Paso 4: Sincronizando proyecto con nuevas versiones...
call gradlew --refresh-dependencies

echo.
echo Paso 5: Compilando proyecto...
call gradlew assembleDebug

echo.
echo ========================================
echo Proceso completado
echo ========================================
pause

