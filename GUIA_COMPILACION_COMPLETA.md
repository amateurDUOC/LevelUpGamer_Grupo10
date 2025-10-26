# Guía Completa de Compilación - LevelUpGamer

## ✅ Problemas Resueltos

### 1. Error JDK Transform (CRÍTICO - RESUELTO)
**Problema:** Error al ejecutar jlink.exe durante la compilación
**Causa:** Uso de API Level 36 (experimental)
**Solución:** Cambiado `compileSdk` de 36 a 34 en `app/build.gradle.kts`

### 2. Configuración de Gradle
**Estado:** ✅ Correcta
- Gradle wrapper: 8.13
- Android Gradle Plugin: 8.1.4
- Kotlin: 1.9.10
- KSP: 1.9.10-1.0.13

### 3. Archivos de Recursos
**Estado:** ✅ Todos los recursos necesarios existen
- Iconos de launcher: ✅
- Iconos de store: ✅
- Colores y temas: ✅

### 4. Dependencias y Repositorios
**Estado:** ✅ Configurados correctamente
- `libs.versions.toml` existe y está bien configurado
- `settings.gradle.kts` tiene los repositorios correctos
- Todas las dependencias están definidas

## 📋 Archivos Clave del Proyecto

### Configuración Principal
```
├── build.gradle.kts          # Configuración raíz del proyecto
├── settings.gradle.kts       # Configuración de módulos y repositorios
├── gradle.properties         # Propiedades de Gradle
└── gradle/
    ├── libs.versions.toml    # Catálogo de versiones centralizado
    └── wrapper/
        └── gradle-wrapper.properties
```

### Módulo App
```
app/
├── build.gradle.kts          # Configuración del módulo
└── src/main/
    ├── AndroidManifest.xml
    ├── java/com/grupo10/levelupgamer/
    │   ├── data/             # Capa de datos (DAO, Database, Repository)
    │   ├── model/            # Modelos de datos
    │   ├── ui/               # Composables de UI
    │   └── viewmodel/        # ViewModels
    └── res/                  # Recursos (iconos, colores, strings)
```

## 🚀 Cómo Compilar el Proyecto

### Opción 1: Usando Android Studio (RECOMENDADO)

1. **Abrir el proyecto:**
   - File > Open
   - Selecciona la carpeta `LevelUpGamer_Grupo10`

2. **Sincronizar Gradle:**
   - File > Sync Project with Gradle Files
   - Espera a que termine la sincronización

3. **Limpiar el proyecto:**
   - Build > Clean Project

4. **Reconstruir el proyecto:**
   - Build > Rebuild Project

5. **Ejecutar la app:**
   - Run > Run 'app'
   - O presiona Shift+F10

### Opción 2: Usando Línea de Comandos

⚠️ **NOTA:** El wrapper de Gradle (`gradlew.bat`) debe existir en el directorio raíz.

Si no existe, genera el wrapper desde Android Studio:
- Terminal en Android Studio
- Ejecuta: `gradle wrapper`

Luego puedes usar:

```cmd
# Limpiar el proyecto
gradlew clean

# Compilar (build)
gradlew build

# Crear APK de debug
gradlew assembleDebug

# Instalar en dispositivo conectado
gradlew installDebug
```

### Opción 3: Script de Limpieza Automática

Si tienes el wrapper instalado, usa el script incluido:

```cmd
clean_and_build.bat
```

Este script:
1. Detiene los daemons de Gradle
2. Elimina carpetas de build
3. Limpia cache de Gradle
4. Limpia cache de KSP
5. Ejecuta gradle clean
6. Ejecuta gradle build

## 🔧 Configuración Actual del Proyecto

### Versiones (app/build.gradle.kts)
```kotlin
android {
    compileSdk = 34        // ✅ Versión estable
    
    defaultConfig {
        minSdk = 24        // Android 7.0 Nougat
        targetSdk = 34     // Android 14
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

### Características Habilitadas
- ✅ Jetpack Compose
- ✅ Room Database con KSP
- ✅ LiveData y ViewModel
- ✅ Material 3
- ✅ AndroidX

## ⚠️ Problemas Conocidos y Soluciones

### Error: "Plugin was not found"
**Solución:** Verifica que `gradle/libs.versions.toml` exista y esté correcto

### Error: "Unresolved reference"
**Solución:** 
1. Build > Clean Project
2. File > Invalidate Caches / Restart
3. Rebuild Project

### Error: "SDK processing version 4"
**Solución:** Este es solo un warning, no afecta la compilación

### Error: "KSP property has no value available"
**Solución:** Ya resuelto con `org.gradle.configuration-cache=false`

### Error: Resource not found (ic_launcher_foreground)
**Solución:** Ya verificado - todos los recursos existen correctamente

## 📱 Ejecutar en Emulador o Dispositivo

### Requisitos del Dispositivo
- **Versión mínima de Android:** 7.0 (API 24)
- **Versión objetivo:** Android 14 (API 34)

### Configurar Emulador (Android Studio)
1. Tools > Device Manager
2. Create Device
3. Selecciona un dispositivo (ej: Pixel 5)
4. Selecciona una System Image: **API 34** (Android 14)
5. Finish

### Ejecutar en Dispositivo Físico
1. Habilita "Opciones de desarrollador" en tu dispositivo
2. Habilita "Depuración USB"
3. Conecta el dispositivo por USB
4. Acepta la autorización de depuración
5. Run > Run 'app'

## 📊 Estructura de la Base de Datos

El proyecto usa **Room Database** con las siguientes entidades:
- `User` - Usuarios de la aplicación
- `Product` - Productos en la tienda
- `CartItem` - Items en el carrito
- `Order` - Órdenes realizadas
- `Notification` - Notificaciones del usuario

## 🎨 Características de UI

- **Jetpack Compose** para UI declarativa
- **Material 3** Design System
- **Navigation Component** para navegación
- **Lottie** para animaciones
- **Splash Screen** personalizado

## 📝 Notas Importantes

1. **Java 17 es requerido** - Verifica que Android Studio use JDK 17
2. **No uses API Level 35 o 36** hasta que sean estables
3. **El cache de Gradle puede causar problemas** - Usa clean cuando sea necesario
4. **KSP es sensible a cambios** - Rebuild después de cambios en entidades Room

## 🆘 Solución de Problemas

Si encuentras errores al compilar:

1. **Limpia todo:**
   ```cmd
   gradlew clean
   ```

2. **Invalida caches (Android Studio):**
   - File > Invalidate Caches / Restart
   - Selecciona "Invalidate and Restart"

3. **Elimina carpetas manualmente:**
   - Elimina `.gradle/` en el directorio raíz
   - Elimina `app/build/`
   - Elimina `build/`

4. **Reconstruye:**
   - File > Sync Project with Gradle Files
   - Build > Rebuild Project

5. **Verifica la versión de JDK:**
   - File > Project Structure
   - SDK Location
   - JDK Location debe ser JDK 17

## 📚 Documentación Adicional

- `SOLUCION_ERROR_JDK.md` - Detalles del error JDK resuelto
- `SOLUCION_ERROR_KSP.md` - Configuración de KSP
- `INSTRUCCIONES_COMPILACION.md` - Instrucciones generales
- `ESTRUCTURA_ARCHIVOS.md` - Estructura del proyecto
- `SPLASH_SCREEN_README.md` - Configuración splash screen
- `ICONOS_README.md` - Iconos de la aplicación

## ✅ Checklist de Compilación

Antes de compilar, verifica:

- [ ] Android Studio actualizado (versión 2023.1 o superior)
- [ ] JDK 17 configurado
- [ ] Android SDK 34 instalado
- [ ] Gradle sync completado sin errores
- [ ] No hay errores rojos en el código
- [ ] Internet disponible (primera compilación descarga dependencias)

## 🎯 Resultado Esperado

Después de una compilación exitosa:
- APK generado en: `app/build/outputs/apk/debug/`
- Tamaño aproximado: 15-25 MB
- La app debería iniciar con el splash screen
- Login/Register screen visible

---

**Última actualización:** 25 de octubre de 2025
**Estado del proyecto:** ✅ Listo para compilar
**Versión:** 1.0

