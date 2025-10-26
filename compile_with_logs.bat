@echo off
echo ========================================
echo Compilacion con Logs Detallados
echo ========================================
echo.

echo Limpiando proyecto...
call gradlew clean

echo.
echo Compilando con logs completos...
call gradlew assembleDebug --stacktrace 2>&1 | tee compile_output.txt

echo.
echo ========================================
echo Log guardado en: compile_output.txt
echo ========================================
echo.

if %ERRORLEVEL% EQU 0 (
    echo EXITO - Instalando en dispositivo...
    call gradlew installDebug

    echo.
    echo Para ver logs en tiempo real:
    echo adb logcat | findstr "AndroidRuntime"
) else (
    echo ERROR - Revisa compile_output.txt
    echo.
    echo Ultimas lineas del error:
    type compile_output.txt | findstr /C:"error" /C:"Error" /C:"FAILED"
)

pause

