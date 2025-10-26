# LevelUpGamer_Grupo10 🎮
Aplicación Móvil Tienda Level-Up Gamer 

## 📝 Descripción
Aplicación Android desarrollada para la tienda Level-Up Gamer usando Kotlin y Jetpack Compose. Una plataforma moderna de e-commerce especializada en productos gaming con gestión de usuarios, carrito de compras, órdenes y notificaciones.

## ✅ Estado del Proyecto
**LISTO PARA COMPILAR** - Todos los errores críticos han sido resueltos.

## 🔧 Requisitos del Sistema
- **Android Studio:** Hedgehog 2023.1.1 o superior (recomendado: Ladybug 2024.2)
- **JDK:** 17 (requerido)
- **Android SDK:** API 24 (Android 7.0) mínimo, API 34 (Android 14) target
- **Gradle:** 8.13 (incluido en wrapper)
- **RAM:** Mínimo 8GB (recomendado 16GB)

## 🚀 Inicio Rápido

### 1. Clonar o Abrir el Proyecto
```bash
# Si clonaste desde Git
cd LevelUpGamer_Grupo10
```

### 2. Abrir en Android Studio
- File > Open
- Selecciona la carpeta del proyecto
- Espera a que Gradle sincronice

### 3. Compilar y Ejecutar
- **Opción A (Recomendada):** Build > Rebuild Project
- **Opción B:** Click en Run ▶️ (Shift+F10)

## 📁 Estructura del Proyecto
```
LevelUpGamer_Grupo10/
├── app/
│   ├── src/main/
│   │   ├── java/com/grupo10/levelupgamer/
│   │   │   ├── data/          # Capa de datos
│   │   │   │   ├── dao/       # Data Access Objects
│   │   │   │   ├── database/  # Room Database
│   │   │   │   └── repository/# Repositorios
│   │   │   ├── model/         # Modelos de datos
│   │   │   ├── ui/            # Composables UI
│   │   │   │   ├── screens/   # Pantallas
│   │   │   │   ├── components/# Componentes reutilizables
│   │   │   │   └── navigation/# Navegación
│   │   │   ├── viewmodel/     # ViewModels
│   │   │   └── MainActivity.kt
│   │   └── res/               # Recursos
│   │       ├── drawable/      # Iconos vectoriales
│   │       ├── mipmap-*/      # Iconos de launcher
│   │       └── values/        # Colores, strings, temas
│   └── build.gradle.kts       # Config del módulo
├── gradle/
│   ├── libs.versions.toml     # Catálogo de versiones
│   └── wrapper/
├── build.gradle.kts           # Config raíz
├── settings.gradle.kts        # Config de módulos
└── gradle.properties          # Propiedades de Gradle
```

## 🛠️ Tecnologías Utilizadas

### Core
- **Kotlin** 1.9.10
- **Android SDK** 34 (compileSdk y targetSdk)
- **Gradle** 8.13

### UI/UX
- **Jetpack Compose** (últimas versiones)
- **Material Design 3**
- **Lottie** 6.1.0 (animaciones)
- **Custom Splash Screen**

### Arquitectura
- **MVVM** (Model-View-ViewModel)
- **Room Database** 2.6.0 (con KSP)
- **LiveData** y **StateFlow**
- **Coroutines** para operaciones asíncronas
- **Repository Pattern**

### Dependencias Clave
```toml
[versions]
agp = "8.1.4"
kotlin = "1.9.10"
ksp = "1.9.10-1.0.13"
compose = "2023.10.01"
room = "2.6.0"
lifecycle = "2.6.2"
```

## 🎯 Características Principales

### 👤 Gestión de Usuarios
- Registro de nuevos usuarios
- Login con validación
- Perfil de usuario editable
- Roles y permisos

### 🛒 Tienda y Carrito
- Catálogo de productos gaming
- Filtrado por categorías
- Búsqueda de productos
- Carrito de compras persistente
- Gestión de cantidades

### 📦 Órdenes
- Historial de órdenes
- Estado de órdenes (Pendiente, Procesando, Enviado, Entregado)
- Detalles de cada orden

### 🔔 Notificaciones
- Sistema de notificaciones in-app
- Notificaciones de cambios de estado
- Contador de no leídas
- Marcar como leído/no leído

## 🗄️ Base de Datos (Room)

### Entidades
- **User:** Información de usuarios
- **Product:** Catálogo de productos
- **CartItem:** Items en el carrito
- **Order:** Órdenes realizadas
- **Notification:** Notificaciones del usuario

### DAOs
Cada entidad tiene su DAO con operaciones CRUD y queries personalizadas.

## 📱 Pantallas de la App

1. **Splash Screen** - Animación de carga con logo
2. **Login/Register** - Autenticación
3. **Home** - Pantalla principal con productos destacados
4. **Store** - Catálogo completo con filtros
5. **Product Detail** - Detalles del producto
6. **Cart** - Carrito de compras
7. **Checkout** - Proceso de compra
8. **Orders** - Historial de órdenes
9. **Profile** - Perfil del usuario
10. **Notifications** - Centro de notificaciones

## 🐛 Problemas Resueltos

### ✅ Error JDK Transform (CRÍTICO)
**Problema:** `Failed to transform core-for-system-modules.jar`
**Solución:** Cambiado `compileSdk` de 36 a 34

Ver: [SOLUCION_ERROR_JDK.md](SOLUCION_ERROR_JDK.md)

### ✅ Error KSP
**Problema:** `property 'classpathSnapshotProperties.useClasspathSnapshot' has no value`
**Solución:** Deshabilitado configuration-cache

Ver: [SOLUCION_ERROR_KSP.md](SOLUCION_ERROR_KSP.md)

## 📚 Documentación Adicional

- 📖 [GUIA_COMPILACION_COMPLETA.md](GUIA_COMPILACION_COMPLETA.md) - Guía detallada de compilación
- 🔧 [INSTRUCCIONES_COMPILACION.md](INSTRUCCIONES_COMPILACION.md) - Instrucciones paso a paso
- 📁 [ESTRUCTURA_ARCHIVOS.md](ESTRUCTURA_ARCHIVOS.md) - Estructura detallada
- 🎨 [SPLASH_SCREEN_README.md](SPLASH_SCREEN_README.md) - Configuración splash screen
- 🖼️ [ICONOS_README.md](ICONOS_README.md) - Información de iconos

## ⚠️ Solución de Problemas

### La app no compila
1. Build > Clean Project
2. File > Invalidate Caches / Restart
3. Build > Rebuild Project

### Errores de dependencias
```bash
gradlew clean
gradlew build --refresh-dependencies
```

### Errores de Room/KSP
- Verifica que las entidades estén correctamente anotadas
- Rebuild después de cambios en DAOs

Ver guía completa en: [GUIA_COMPILACION_COMPLETA.md](GUIA_COMPILACION_COMPLETA.md)

## 👥 Equipo - Grupo 10

Proyecto desarrollado para el curso de Mobile Development
DuocUC - 4to Semestre - Analista Programador

## 📄 Licencia

Este proyecto es parte de un trabajo académico.

## 🔄 Versión

**v1.0** - Octubre 2025
- ✅ Funcionalidades completas
- ✅ UI/UX implementado
- ✅ Base de datos configurada
- ✅ Listo para compilar y ejecutar

---

**⭐ NOTA:** Para compilar por primera vez, lee [GUIA_COMPILACION_COMPLETA.md](GUIA_COMPILACION_COMPLETA.md)
 

