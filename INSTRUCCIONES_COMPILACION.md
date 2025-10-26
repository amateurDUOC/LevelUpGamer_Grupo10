# 🚀 Instrucciones de Compilación - Splash Screen

## ⚠️ IMPORTANTE: Pasos para Ejecutar

### 1️⃣ Sincronizar Gradle (OBLIGATORIO)

Antes de compilar, debes sincronizar el proyecto para que reconozca el archivo `libs.versions.toml`:

**En Android Studio / IntelliJ:**

```
File → Sync Project with Gradle Files
```

O usa el ícono de sincronización en la barra de herramientas: 🔄

**Espera a que termine la sincronización** (puede tardar 1-2 minutos la primera vez)

---

### 2️⃣ Verificar Errores

Después de sincronizar, verifica que no haya errores:

```
Build → Make Project
```

O usa: **Ctrl + F9** (Windows) / **Cmd + F9** (Mac)

---

### 3️⃣ Ejecutar la App

Una vez sincronizado sin errores:

```
Run → Run 'app'
```

O usa: **Shift + F10** (Windows) / **Ctrl + R** (Mac)

---

## 🔧 Solución de Problemas

### ❌ Error: "Unresolved reference 'libs'"

**Causa:** El proyecto no está sincronizado con Gradle.

**Solución:**
1. File → Sync Project with Gradle Files
2. Espera a que termine
3. Si persiste, cierra y reabre el proyecto

---

### ❌ Error: "Plugin not found"

**Causa:** Gradle no encontró los plugins en el repositorio.

**Solución:**
1. Verifica tu conexión a internet
2. File → Invalidate Caches → Invalidate and Restart
3. Espera a que se reindexe el proyecto
4. Sync Project with Gradle Files

---

### ❌ Error: Versiones incompatibles

**Causa:** Versión de Gradle o plugins desactualizada.

**Solución:**

Verifica en `gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
```

Si es menor a 8.0, actualízalo.

---

### ❌ Error: "Cannot resolve drawable/logo_levelup_gamer"

**Causa:** El archivo XML del logo no se generó correctamente.

**Solución:**
1. Verifica que existe: `app/src/main/res/drawable/logo_levelup_gamer.xml`
2. Clean Project: Build → Clean Project
3. Rebuild Project: Build → Rebuild Project

---

### ❌ La animación no se ve

**Causa posible 1:** Estás usando un emulador muy lento.

**Solución:** Prueba en un dispositivo físico o crea un AVD con mayor RAM.

**Causa posible 2:** La animación ya terminó (pasa muy rápido).

**Solución:** Aumenta el delay en `AnimatedSplashScreen.kt`:
```kotlin
delay(10000) // 10 segundos para probar
```

---

## 📱 Requisitos del Sistema

### Android Studio
- **Versión mínima:** Arctic Fox (2020.3.1) o superior
- **Recomendado:** Hedgehog (2023.1.1) o más nuevo

### SDK de Android
- **compileSdk:** 34
- **minSdk:** 24 (Android 7.0)
- **targetSdk:** 34

### Gradle
- **Versión:** 8.0 o superior
- **AGP:** 8.1.4

### Java/Kotlin
- **JDK:** 17
- **Kotlin:** 1.9.10

---

## 🎯 Checklist Pre-Compilación

Antes de ejecutar, verifica:

- [ ] ✅ Archivo `gradle/libs.versions.toml` existe
- [ ] ✅ Proyecto sincronizado (no hay errores en Gradle)
- [ ] ✅ Logo `logo_levelup_gamer.xml` en `res/drawable/`
- [ ] ✅ Archivos de Splash Screen creados
- [ ] ✅ MainActivity actualizado
- [ ] ✅ Color.kt con colores personalizados
- [ ] ✅ Build sin errores de compilación

---

## 🔄 Comandos de Terminal (Alternativo)

Si prefieres usar la terminal:

### Windows (CMD)
```cmd
cd "C:\Users\twoag\Desktop\duocAnalistaProgramador\4semestre\mobileDevelopment\exp2\LevelUpGamer_Grupo10"
gradlew clean
gradlew assembleDebug
gradlew installDebug
```

### Windows (PowerShell)
```powershell
cd "C:\Users\twoag\Desktop\duocAnalistaProgramador\4semestre\mobileDevelopment\exp2\LevelUpGamer_Grupo10"
.\gradlew clean
.\gradlew assembleDebug
.\gradlew installDebug
```

### Linux/Mac
```bash
cd "/path/to/LevelUpGamer_Grupo10"
./gradlew clean
./gradlew assembleDebug
./gradlew installDebug
```

---

## 📊 Tiempos Estimados

| Acción | Tiempo (Primera Vez) | Tiempo (Subsecuente) |
|--------|----------------------|---------------------|
| Sync Gradle | 2-3 minutos | 10-30 segundos |
| Build | 1-2 minutos | 20-40 segundos |
| Deploy | 30-60 segundos | 10-20 segundos |
| **TOTAL** | **4-6 minutos** | **40-90 segundos** |

---

## 🎨 Primera Ejecución

Al ejecutar por primera vez verás:

1. **0-3.5s:** Splash Screen animado
   - Logo con bounce
   - Anillos rotando
   - Partículas flotando
   - Texto fade-in
   - Puntos de carga

2. **3.5s+:** Pantalla principal
   - "Bienvenido a Level Up Gamer!"

---

## 📸 Captura de la Animación

Para capturar la animación en video:

**Android Studio:**
1. Run → Run 'app'
2. Cuando se abra el emulador
3. En la barra de emulador: ⚫ Record
4. Captura los primeros 5 segundos
5. ⏹️ Stop

**Dispositivo físico:**
1. Activa grabación de pantalla
2. Ejecuta la app
3. Captura el inicio

---

## ✅ Verificación de Éxito

Sabrás que todo funciona correctamente si:

- ✅ No hay errores en Build
- ✅ La app se instala sin problemas
- ✅ Ves el logo animado al iniciar
- ✅ Los anillos rotan suavemente
- ✅ El texto aparece gradualmente
- ✅ Después de 3.5s pasa a la pantalla principal
- ✅ No hay crashes ni freezes

---

## 🆘 Soporte

Si encuentras problemas:

1. **Revisa los logs:**
   - View → Tool Windows → Logcat
   - Busca errores en rojo

2. **Limpia el proyecto:**
   - Build → Clean Project
   - Build → Rebuild Project

3. **Reinicia Gradle daemon:**
   ```cmd
   gradlew --stop
   ```

4. **Invalida caché:**
   - File → Invalidate Caches → Invalidate and Restart

5. **Verifica JDK:**
   - File → Project Structure
   - SDK Location → JDK: debe ser 17

---

## 🎯 Resultado Esperado

```
📱 App Launch
    ↓
🎮 Splash Screen (3.5s)
    ↓
    ✨ Animaciones ejecutándose
    ↓
🏠 Main Screen
    ↓
✅ Funcionando correctamente!
```

---

## 🎉 ¡Listo para Ejecutar!

Ahora solo necesitas:

1. **Sync** (una vez) → 🔄
2. **Build** (cada cambio) → 🔨
3. **Run** (para ver) → ▶️

**¡Disfruta tu splash screen! 🚀✨**

