# 🎮 Splash Screen Animado - Level Up Gamer

## 📋 Descripción

Se ha implementado una splash screen animada profesional para la aplicación **Level Up Gamer** con múltiples efectos visuales y animaciones fluidas.

## ✨ Características de la Animación

### 1. **Logo Personalizado**
- Logo vectorial personalizado con temática gaming
- Incluye elementos de gamepad, estrella y flecha de "level up"
- Colores vibrantes: Púrpura (#6200EE), Teal (#03DAC5) y Dorado (#FFD700)

### 2. **Efectos de Animación**

#### **Animación del Logo**
- ✅ Animación de entrada con efecto "spring bounce"
- ✅ Escala desde 0 a 1 con rebote suave
- ✅ Duración de 3.5 segundos

#### **Anillos Rotativos**
- ✅ Dos anillos concéntricos con gradientes
- ✅ Rotación en direcciones opuestas
- ✅ Colores degradados: Púrpura → Teal → Dorado

#### **Brillo Pulsante**
- ✅ Efecto de pulso radial detrás del logo
- ✅ Escala de 1.0 a 1.3 repetidamente
- ✅ Gradiente radial con transparencia

#### **Partículas Flotantes**
- ✅ 20 partículas doradas flotantes en el fondo
- ✅ Movimiento vertical continuo
- ✅ Tamaños y velocidades aleatorias

#### **Indicador de Carga**
- ✅ 3 puntos animados en secuencia
- ✅ Efecto de escala con desfase temporal
- ✅ Color dorado brillante

#### **Texto Animado**
- ✅ Aparición gradual con fade-in
- ✅ Tipografía bold con espaciado de letras
- ✅ Texto principal en blanco, secundario en teal

### 3. **Paleta de Colores**

```kotlin
GamerPurple   = #6200EE  // Púrpura principal
GamerTeal     = #03DAC5  // Teal/Cian
GamerGold     = #FFD700  // Dorado
GamerDarkBlue = #1A1A2E  // Azul oscuro
GamerNavyBlue = #16213E  // Azul marino
GamerDeepBlue = #0F3460  // Azul profundo
GamerRed      = #E94560  // Rojo accent
GamerWhite    = #F5F5F5  // Blanco suave
```

### 4. **Fondo Degradado**
- ✅ Gradiente vertical de tres tonos de azul
- ✅ Transición suave: Oscuro → Marino → Profundo
- ✅ Temática nocturna gaming

## 📁 Archivos Creados

### **1. Logo**
```
app/src/main/res/drawable/logo_levelup_gamer.xml
```
Logo vectorial personalizado con elementos gaming.

### **2. Pantalla Simple**
```
app/src/main/java/com/grupo10/levelupgamer/ui/screens/SplashScreen.kt
```
Versión básica con animaciones esenciales.

### **3. Pantalla Avanzada** (Recomendada)
```
app/src/main/java/com/grupo10/levelupgamer/ui/screens/AnimatedSplashScreen.kt
```
Versión completa con todos los efectos visuales:
- Anillos rotativos
- Partículas flotantes
- Brillo pulsante
- Indicador de carga animado

### **4. Colores del Tema**
```
app/src/main/java/com/grupo10/levelupgamer/ui/theme/Color.kt
```
Paleta de colores personalizada para la app.

## 🚀 Uso

El Splash Screen se muestra automáticamente al iniciar la app en el `MainActivity`:

```kotlin
if (showSplash) {
    AnimatedSplashScreen(
        onSplashFinished = {
            showSplash = false
        }
    )
} else {
    // Contenido principal de la app
}
```

## ⚙️ Configuración

### Duración del Splash Screen
Por defecto: **3.5 segundos**

Para cambiar la duración, edita el delay en `AnimatedSplashScreen.kt`:

```kotlin
LaunchedEffect(key1 = true) {
    startAnimation = true
    delay(3500) // ← Cambiar aquí (en milisegundos)
    onSplashFinished()
}
```

### Personalización de Animaciones

#### Velocidad de Rotación
```kotlin
val rotation by infiniteTransition.animateFloat(
    animationSpec = infiniteRepeatable(
        animation = tween(4000), // ← Cambiar duración
        // ...
    )
)
```

#### Intensidad del Pulso
```kotlin
val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.3f, // ← Cambiar escala máxima
    // ...
)
```

## 🎨 Características Técnicas

- ✅ **Jetpack Compose** - UI moderna y declarativa
- ✅ **Animaciones nativas** - APIs de Animation de Compose
- ✅ **Canvas personalizado** - Dibujo de anillos y partículas
- ✅ **Coroutines** - Gestión del tiempo de visualización
- ✅ **Material Design 3** - Siguiendo las guías de diseño
- ✅ **Tema personalizado** - Colores gaming consistentes
- ✅ **Vector drawable** - Logo escalable sin pérdida de calidad

## 📱 Comportamiento

1. **Inicio** → Se muestra el Splash Screen
2. **0-0.5s** → Logo aparece con bounce
3. **0.5-1s** → Texto hace fade-in
4. **0-3.5s** → Animaciones continuas (rotación, pulso, partículas)
5. **3.5s** → Transición a la pantalla principal

## 🔄 Alternativas

Si prefieres la versión simple sin partículas ni anillos, cambia en `MainActivity.kt`:

```kotlin
SplashScreen(  // En lugar de AnimatedSplashScreen
    onSplashFinished = { showSplash = false }
)
```

## 🎯 Próximas Mejoras Posibles

- [ ] Integración con Lottie para animaciones JSON
- [ ] Sonido de inicio
- [ ] Animación de transición a pantalla principal
- [ ] Modo de "Skip" con botón
- [ ] Precarga de datos mientras se muestra el splash
- [ ] Variaciones según tema (claro/oscuro)

## 📝 Notas

- Las animaciones usan `remember` y `LaunchedEffect` para rendimiento óptimo
- El Splash Screen se ejecuta en el hilo principal sin bloqueos
- Todas las animaciones son cancelables automáticamente al cambiar de pantalla
- Compatible con Android API 24+ (Android 7.0)

---

**¡Disfruta del Splash Screen animado de Level Up Gamer! 🎮✨**

