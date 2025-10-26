# 🚀 GUÍA RÁPIDA - Compilar y Probar la App

## ✅ Prerequisitos Verificados

- ✅ Gradle actualizado a 8.6
- ✅ Android Gradle Plugin 8.4.2
- ✅ Kotlin 1.9.24
- ✅ Header creado con logo, búsqueda y notificaciones
- ✅ Sin errores de compilación

---

## 📋 Pasos para Compilar

### Opción 1: Usando el Script (RECOMENDADO) ⭐

```cmd
compilar_app.bat
```

Este script:
1. Compila la aplicación
2. Muestra la ubicación del APK
3. Da instrucciones para instalar

---

### Opción 2: Línea de Comandos

#### Paso 1: Limpiar (Opcional)
```cmd
gradlew clean
```

#### Paso 2: Compilar
```cmd
gradlew assembleDebug
```

#### Paso 3: Encontrar el APK
El APK estará en:
```
app\build\outputs\apk\debug\app-debug.apk
```

---

## 📱 Instalar en Dispositivo/Emulador

### Método 1: Automático con Gradle
```cmd
gradlew installDebug
```

### Método 2: Desde Android Studio
1. Conecta tu dispositivo o inicia el emulador
2. Haz clic en el botón ▶️ "Run"
3. Selecciona el dispositivo
4. Espera a que compile e instale

### Método 3: Instalación Manual del APK
1. Copia `app-debug.apk` a tu dispositivo
2. Habilita "Instalar apps desconocidas"
3. Abre el APK y toca "Instalar"

---

## 🧪 Qué Probar

### 1. Splash Screen ✨
- ✅ Logo animado aparece al iniciar
- ✅ Transición suave a pantalla principal
- ✅ Duración: 2-3 segundos

### 2. Header Principal 📱
- ✅ Logo circular visible en esquina superior izquierda
- ✅ Texto "Level Up Gamer" visible
- ✅ Campana de notificaciones en esquina superior derecha
- ✅ Badge rojo con número "3"

### 3. Barra de Búsqueda 🔍
- ✅ Toca la barra de búsqueda
- ✅ Aparece el teclado
- ✅ Escribe "PlayStation"
- ✅ Verifica que el texto aparece
- ✅ Aparece botón "X" a la derecha
- ✅ Toca "X" para limpiar
- ✅ Verifica que el texto se borra

### 4. Notificaciones 🔔
- ✅ Toca el icono de campana
- ✅ Aparece diálogo modal
- ✅ Verifica 3 notificaciones listadas:
  - Nueva oferta disponible
  - Producto añadido
  - Pedido enviado
- ✅ Toca "Cerrar"
- ✅ Verifica que el badge cambia a "0"

### 5. Categorías 📦
- ✅ Scroll hacia abajo
- ✅ Verifica 5 categorías:
  - Juegos
  - Consolas
  - Accesorios
  - Merchandising
  - Ofertas
- ✅ Todas las cards visibles y con buen espaciado

### 6. Resultados de Búsqueda 📊
- ✅ Escribe algo en la barra de búsqueda
- ✅ Scroll hacia abajo
- ✅ Verifica texto "Resultados para: '...'"
- ✅ Mensaje "Mostrando productos relacionados..."

---

## 🎥 Flujo Completo de Prueba

```
1. Abrir app
   ↓
2. Ver splash screen (2-3 seg)
   ↓
3. Llegar a pantalla principal
   ↓
4. Ver header con logo y campana
   ↓
5. Tocar barra de búsqueda
   ↓
6. Escribir "Nintendo Switch"
   ↓
7. Ver resultados al final
   ↓
8. Tocar X para limpiar
   ↓
9. Tocar campana de notificaciones
   ↓
10. Leer las 3 notificaciones
    ↓
11. Cerrar diálogo
    ↓
12. Verificar que badge desapareció
    ↓
13. Scroll por las categorías
    ↓
✅ PRUEBA COMPLETA
```

---

## 🐛 Solución de Problemas

### Error: "Unresolved reference HomeScreen"
**Causa**: Caché del IDE  
**Solución**:
```
En Android Studio:
File → Invalidate Caches → Restart
```

### Error: jlink.exe
**Causa**: Caché corrupto de Gradle  
**Solución**:
```cmd
limpiar_cache_jlink.bat
```

### APK no se genera
**Causa**: Errores de compilación  
**Solución**:
```cmd
gradlew clean
gradlew assembleDebug --info
```
Revisa los logs para ver el error específico.

### Logo no aparece
**Causa**: Recursos no sincronizados  
**Solución**:
```
En Android Studio:
File → Sync Project with Gradle Files
```

### Búsqueda no funciona
**Causa**: Estado no se actualiza  
**Solución**: 
- Verifica que estás en modo Debug
- Hot reload puede no funcionar, reinstala la app

---

## 📊 Checklist de Funcionalidades

```
✅ Splash screen animado
✅ Logo en header
✅ Nombre de app en header
✅ Barra de búsqueda funcional
✅ Icono de búsqueda
✅ Placeholder en búsqueda
✅ Botón X para limpiar
✅ Campana de notificaciones
✅ Badge con contador
✅ Diálogo de notificaciones
✅ 3 notificaciones de ejemplo
✅ Botón cerrar diálogo
✅ Contador se reinicia al cerrar
✅ 5 categorías listadas
✅ Scroll suave
✅ Diseño responsive
✅ Colores del tema aplicados
✅ Material Design 3
✅ Sin errores de compilación
```

---

## 📸 Capturas Esperadas

### Vista Principal
- Header morado en la parte superior
- Logo circular a la izquierda
- Campana con badge "3" a la derecha
- Barra de búsqueda blanca centrada
- Lista de categorías debajo

### Con Búsqueda Activa
- Texto en la barra de búsqueda
- Botón X visible
- Resultados mostrados al final

### Diálogo de Notificaciones
- Ventana modal semi-transparente
- Lista de 3 notificaciones
- Dividers entre notificaciones
- Botón "Cerrar" en la parte inferior

---

## ⚡ Compilación Rápida

Para desarrolladores:

```cmd
REM Compilar y ejecutar en un comando
gradlew installDebug && adb shell am start -n com.grupo10.levelupgamer/.MainActivity
```

---

## 📝 Notas Importantes

1. **Primera compilación**: Puede tardar 2-5 minutos
2. **Hot Reload**: Funciona para cambios pequeños de UI
3. **Clean Build**: Necesario después de cambios en Gradle
4. **Emulador**: Recomendado API 30+ para mejor rendimiento
5. **Dispositivo Real**: Habilita depuración USB

---

## 🎯 Métricas de Éxito

- ✅ Compilación exitosa sin errores
- ✅ APK generado correctamente
- ✅ App se instala sin problemas
- ✅ Splash screen se muestra
- ✅ Transición suave a home
- ✅ Todos los elementos del header visibles
- ✅ Búsqueda funciona correctamente
- ✅ Notificaciones interactivas funcionan
- ✅ Sin crashes o ANR

---

## 📞 Próximos Desarrollos

Después de verificar que todo funciona:

1. Conectar búsqueda con base de datos Room
2. Implementar navegación entre pantallas
3. Agregar pantallas de detalle de productos
4. Implementar carrito de compras
5. Sistema de notificaciones real
6. Perfil de usuario
7. Historial de pedidos

---

**¡Listo para compilar y probar!** 🚀

Ejecuta: `compilar_app.bat` o `gradlew assembleDebug`

