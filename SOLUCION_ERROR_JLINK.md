# 🔧 SOLUCIÓN ERROR JLINK.EXE - JdkImageTransform

## ❌ Error Encontrado
```
Execution failed for task ':app:compileDebugJavaWithJavac'.
> Could not resolve all files for configuration ':app:androidJdkImage'.
   > Failed to transform core-for-system-modules.jar
     > Error while executing process jlink.exe
```

## 🎯 Cambios Aplicados

### 1. Downgrade de Gradle 8.13 → 8.7
**Archivo:** `gradle/wrapper/gradle-wrapper.properties`
**Motivo:** Gradle 8.13 tiene problemas con JdkImageTransform en Windows

```properties
# ANTES
distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip

# DESPUÉS
distributionUrl=https\://services.gradle.org/distributions/gradle-8.7-bin.zip
```

### 2. Propiedades Adicionales en gradle.properties
**Archivo:** `gradle.properties`
**Agregado:**
```properties
org.gradle.unsafe.isolated-projects=false
android.defaults.buildfeatures.buildconfig=true
android.enableJetifier=false
```

## ⚡ PASOS OBLIGATORIOS PARA RESOLVER

### Paso 1: Detener Daemons de Gradle
Abre una terminal (cmd) en el directorio del proyecto y ejecuta:
```cmd
gradlew --stop
```

### Paso 2: Limpiar Cache de Gradle
Elimina manualmente estas carpetas:

**Opción A - Desde Android Studio:**
```
File > Invalidate Caches / Restart...
Selecciona:
☑ Clear file system cache and Local History
☑ Clear downloaded shared indexes
☑ Clear VCS Log caches and indexes
Click: Invalidate and Restart
```

**Opción B - Manual (MÁS EFECTIVO):**
Elimina estas carpetas:
```
.gradle\
app\build\
build\
```

**Opción C - Script Automático:**
Crea un archivo `clean_cache.bat` con este contenido:
```bat
@echo off
echo Deteniendo Gradle daemons...
call gradlew --stop

echo Eliminando cache de Gradle local...
if exist ".gradle\" rmdir /s /q ".gradle"
if exist "app\build\" rmdir /s /q "app\build"
if exist "build\" rmdir /s /q "build"

echo Eliminando cache de usuario de Gradle...
if exist "%USERPROFILE%\.gradle\caches\8.13\" rmdir /s /q "%USERPROFILE%\.gradle\caches\8.13"
if exist "%USERPROFILE%\.gradle\caches\transforms*" (
    for /d %%i in ("%USERPROFILE%\.gradle\caches\transforms*") do rmdir /s /q "%%i"
)

echo Cache limpiada! Ahora sincroniza el proyecto en Android Studio.
pause
```

Luego ejecuta el script:
```cmd
clean_cache.bat
```

### Paso 3: Sincronizar Gradle (descargará Gradle 8.7)
En Android Studio:
```
File > Sync Project with Gradle Files
```
⏱️ Espera 1-2 minutos (descargará Gradle 8.7)

### Paso 4: Clean Project
```
Build > Clean Project
```

### Paso 5: Rebuild Project
```
Build > Rebuild Project
```

## 🔍 Verificar Versión de Gradle

Después de sincronizar, verifica que esté usando Gradle 8.7:
```cmd
gradlew --version
```

Deberías ver:
```
Gradle 8.7
```

## 💡 Causa del Problema

El error ocurre porque:
1. **Gradle 8.13** tiene un bug con `JdkImageTransform` en Windows
2. El proceso `jlink.exe` falla al crear el módulo JDK personalizado
3. Los argumentos pasados a jlink.exe no son compatibles con algunas configuraciones

**Solución:** Usar Gradle 8.7 que es más estable y compatible con AGP 8.1.4

## 📊 Compatibilidad de Versiones

| Componente | Versión | Estado |
|------------|---------|--------|
| Gradle | 8.7 | ✅ Cambiado |
| AGP | 8.1.4 | ✅ Compatible |
| Kotlin | 1.9.10 | ✅ Compatible |
| compileSdk | 34 | ✅ Correcto |
| JDK | 17 | ✅ Requerido |

## ⚠️ Si el Error Persiste

### Solución 1: Limpiar Cache de Usuario de Gradle
```cmd
# Cierra Android Studio
# Elimina la carpeta de cache de Gradle
rmdir /s /q %USERPROFILE%\.gradle\caches

# Reabre Android Studio y sincroniza
```

### Solución 2: Verificar JDK
1. File > Project Structure
2. SDK Location
3. JDK location debe apuntar a JDK 17
4. Si no, descarga JDK 17 desde: https://adoptium.net/

### Solución 3: Usar Gradle 8.5 (más conservador)
Si Gradle 8.7 aún da problemas, cambia a 8.5:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
```

### Solución 4: Actualizar Android Gradle Plugin
Si es necesario, actualiza AGP a 8.2.0:
```toml
# gradle/libs.versions.toml
agp = "8.2.0"
```

## 🎯 Orden de Acciones (RESUMEN)

```
1. gradlew --stop
2. Eliminar carpetas: .gradle\, app\build\, build\
3. Eliminar cache de usuario: %USERPROFILE%\.gradle\caches\8.13\
4. File > Sync Project with Gradle Files (descarga Gradle 8.7)
5. Build > Clean Project
6. Build > Rebuild Project
7. Run > Run 'app'
```

## 📝 Notas Importantes

- ✅ **Gradle 8.7** es la versión LTS (Long Term Support) más estable
- ✅ **No uses Gradle 8.13+** hasta que se solucione el bug de jlink.exe
- ✅ Siempre **detén los daemons** antes de limpiar cache
- ✅ **Elimina el cache de usuario** si el problema persiste

## 🔗 Referencias

- [Gradle Issue #26741](https://github.com/gradle/gradle/issues/26741) - JdkImageTransform error
- [Android Studio Issue Tracker](https://issuetracker.google.com/issues?q=JdkImageTransform)
- Gradle 8.7 Compatibility: AGP 8.0.0 - 8.2.x

---

**Fecha:** 25 de Octubre de 2025
**Cambio aplicado:** Gradle 8.13 → 8.7
**Estado:** Requiere limpieza de cache y resync

