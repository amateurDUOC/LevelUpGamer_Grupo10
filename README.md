# LevelUpGamer - Grupo 10

Aplicación Android de e-commerce para productos gaming desarrollada con Kotlin y Jetpack Compose.

## Descripción

Plataforma móvil para la tienda Level-Up Gamer con:
- Autenticación de usuarios (JWT)
- Catálogo de productos con búsqueda y filtros
- Carrito de compras
- Geocodificación automática de direcciones
- Integración con backend Node.js + MongoDB

## Requisitos

- Android Studio Hedgehog 2023.1.1+
- JDK 17
- Android SDK API 24-34
- Node.js 18+ (backend)
- MongoDB (backend)

## Configuración

Correr el backend localmente y configurar la base url en el siguiente archivo:

### App Android

Editar `local.properties`:
```properties
# Emulador
BASE_URL=http://10.0.2.2:3000/api/
NOMINATIM_URL=https://nominatim.openstreetmap.org/

# Dispositivo físico (reemplazar con tu IP)
BASE_URL=http://192.168.1.X:3000/api/
```

## Compilación

1. Abrir proyecto en Android Studio
2. Sync Gradle
3. Build > Clean Project
4. Build > Rebuild Project
5. Run (Shift+F10)

## Solución de Problemas

### Error: "cleartext communication not permitted"

Ya está configurado `network_security_config.xml` para permitir HTTP en desarrollo.

Si persiste:
1. Build > Clean Project
2. Build > Rebuild Project
3. Desinstalar app del emulador
4. Run nuevamente

### Backend no responde

Verificar:
- URL correcta en `local.properties`
- Emulador: usar `10.0.2.2`
- Dispositivo físico: usar IP local

## Credenciales de Prueba

```
Email: admin@duoc.cl
Password: 123456
```

## Arquitectura

### Frontend
- Kotlin 1.9.10
- Jetpack Compose
- Material Design 3
- MVVM Pattern
- Room Database (caché local)
- Retrofit + OkHttp (networking)
- Coroutines + StateFlow

### Backend
- Express.js
- MongoDB + Mongoose
- JWT Authentication
- bcryptjs

### APIs Externas
- OpenStreetMap Nominatim (geocodificación)
- osmdroid (mapas)

## Estructura del Proyecto

```
app/src/main/java/com/grupo10/levelupgamer/
├── data/
│   ├── dao/              # Data Access Objects
│   ├── database/         # Room Database
│   ├── remote/           # API, DTOs, Services
│   ├── repository/       # Repositorios
│   └── mapper/           # Mappers DTO <-> Domain
├── model/                # Modelos de dominio
├── ui/
│   ├── screens/          # Pantallas
│   ├── components/       # Componentes reutilizables
│   └── navigation/       # Navegación
├── viewmodel/            # ViewModels
└── MainActivity.kt
```


