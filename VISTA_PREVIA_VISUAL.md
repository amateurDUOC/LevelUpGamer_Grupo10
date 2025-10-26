# 🎨 Vista Previa Visual - Level Up Gamer

## 📱 Pantalla de Inicio Completa

```
╔════════════════════════════════════════════════════╗
║                 🟣 HEADER PRINCIPAL 🟣             ║
╠════════════════════════════════════════════════════╣
║                                                    ║
║   🎮          Level Up                        🔔   ║
║   [logo]      Gamer                          [3]  ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │ 🔍  Buscar juegos, consolas...          │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
╠════════════════════════════════════════════════════╣
║                  CONTENIDO                         ║
╠════════════════════════════════════════════════════╣
║                                                    ║
║   Bienvenido a Level Up Gamer                     ║
║   Tu tienda de videojuegos favorita               ║
║                                                    ║
║   Categorías                                       ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │                                           │    ║
║   │  Juegos                                   │    ║
║   │                                           │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │                                           │    ║
║   │  Consolas                                 │    ║
║   │                                           │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │                                           │    ║
║   │  Accesorios                               │    ║
║   │                                           │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │                                           │    ║
║   │  Merchandising                            │    ║
║   │                                           │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │                                           │    ║
║   │  Ofertas                                  │    ║
║   │                                           │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
╚════════════════════════════════════════════════════╝
```

---

## 🔍 Vista con Búsqueda Activa

```
╔════════════════════════════════════════════════════╗
║   🎮          Level Up                        🔔   ║
║   [logo]      Gamer                          [3]  ║
║                                                    ║
║   ┌──────────────────────────────────────────┐    ║
║   │ 🔍  PlayStation 5                     ✖️  │    ║
║   └──────────────────────────────────────────┘    ║
║                                                    ║
╠════════════════════════════════════════════════════╣
║                                                    ║
║   Bienvenido a Level Up Gamer                     ║
║   Tu tienda de videojuegos favorita               ║
║                                                    ║
║   Categorías                                       ║
║   [...categorías...]                              ║
║                                                    ║
║   Resultados para: "PlayStation 5"                ║
║   Mostrando productos relacionados...             ║
║                                                    ║
╚════════════════════════════════════════════════════╝
```

---

## 🔔 Diálogo de Notificaciones

```
╔════════════════════════════════════════════════════╗
║                                                    ║
║          ┌──────────────────────────────┐         ║
║          │                              │         ║
║          │  Notificaciones              │         ║
║          │                              │         ║
║          │  ───────────────────────     │         ║
║          │                              │         ║
║          │  Nueva oferta disponible     │         ║
║          │  ¡50% de descuento en        │         ║
║          │  juegos seleccionados!       │         ║
║          │                              │         ║
║          │  ───────────────────────     │         ║
║          │                              │         ║
║          │  Producto añadido            │         ║
║          │  Se agregó nuevo stock       │         ║
║          │  de PlayStation 5            │         ║
║          │                              │         ║
║          │  ───────────────────────     │         ║
║          │                              │         ║
║          │  Pedido enviado              │         ║
║          │  Tu pedido #12345 está       │         ║
║          │  en camino                   │         ║
║          │                              │         ║
║          │              [ Cerrar ]      │         ║
║          │                              │         ║
║          └──────────────────────────────┘         ║
║                                                    ║
╚════════════════════════════════════════════════════╝
```

---

## 🎨 Paleta de Colores

### Header
- **Fondo**: Color primario del tema (Morado `#6200EE`)
- **Texto**: Blanco (`#FFFFFF`)
- **Logo**: Multicolor sobre fondo morado

### Barra de Búsqueda
- **Fondo**: Blanco (`#FFFFFF`)
- **Placeholder**: Gris (`#808080`)
- **Bordes**: Redondeados 28dp
- **Sombra**: Ninguna (transparente)

### Badge de Notificaciones
- **Fondo**: Rojo (`#FF0000`)
- **Texto**: Blanco (`#FFFFFF`)
- **Forma**: Círculo

### Categorías
- **Fondo**: `primaryContainer` del tema
- **Texto**: `onPrimaryContainer` del tema
- **Elevación**: 2dp

---

## 📐 Dimensiones

### Header
- **Altura total**: ~140dp
  - Logo: 48dp
  - Barra de búsqueda: 56dp
  - Padding: 16dp arriba/abajo, 16dp laterales

### Logo
- **Tamaño**: 48x48dp
- **Forma**: Circular (clip)

### Campana de Notificaciones
- **Área clickeable**: 48x48dp
- **Icono**: 24x24dp
- **Badge**: 20x20dp
- **Posición badge**: Esquina superior derecha

### Barra de Búsqueda
- **Altura**: 56dp
- **Ancho**: Completo (fill)
- **Bordes**: 28dp (redondeados)

### Categorías
- **Altura**: 80dp
- **Ancho**: Completo (fill)
- **Espaciado**: 16dp entre items

---

## 🔄 Estados Interactivos

### Búsqueda Vacía
```
┌──────────────────────────────────────────┐
│ 🔍  Buscar juegos, consolas...          │
└──────────────────────────────────────────┘
```

### Búsqueda con Texto
```
┌──────────────────────────────────────────┐
│ 🔍  PlayStation 5                     ✖️  │
└──────────────────────────────────────────┘
```

### Notificaciones Sin Leer (3)
```
┌───────┐
│  🔔   │
│   3   │
└───────┘
```

### Notificaciones Leídas (0)
```
┌───────┐
│  🔔   │
│       │
└───────┘
```

---

## 📱 Flujo de Usuario

### 1. Inicio de App
```
[Splash Screen] 
    ↓ (2-3 segundos)
[Home Screen con Header]
```

### 2. Interacción con Búsqueda
```
Usuario toca barra de búsqueda
    ↓
Aparece teclado
    ↓
Usuario escribe "PS5"
    ↓
Resultados aparecen en tiempo real
    ↓
Usuario toca "X" para limpiar
    ↓
Vuelve al estado inicial
```

### 3. Interacción con Notificaciones
```
Usuario ve badge con "3"
    ↓
Usuario toca campana 🔔
    ↓
Aparece diálogo modal
    ↓
Usuario lee notificaciones
    ↓
Usuario toca "Cerrar"
    ↓
Badge cambia a "0"
    ↓
Diálogo se cierra
```

---

## 🎬 Animaciones Futuras Sugeridas

1. **Entrada del Header**
   - Slide down desde arriba
   - Duración: 300ms

2. **Badge de Notificaciones**
   - Pulse animation cuando llega nueva notificación
   - Scale up/down suave

3. **Búsqueda**
   - Fade in de resultados
   - Slide up de teclado

4. **Categorías**
   - Fade in secuencial
   - Stagger de 50ms entre items

5. **Diálogo de Notificaciones**
   - Fade in con backdrop
   - Slide up desde centro

---

## 📊 Jerarquía Visual

```
Nivel 1 (Más Importante)
├── Logo de la app
└── Campana de notificaciones con badge

Nivel 2 (Secundario)
└── Barra de búsqueda

Nivel 3 (Contenido)
├── Título de bienvenida
└── Subtítulo

Nivel 4 (Lista)
└── Categorías
```

---

## 🎯 Puntos de Interacción

1. **Logo** → (Futuro) Volver a inicio
2. **Barra de Búsqueda** → Activar teclado, filtrar productos
3. **Botón X en búsqueda** → Limpiar texto
4. **Campana** → Mostrar diálogo de notificaciones
5. **Cards de Categorías** → (Futuro) Navegar a categoría
6. **Botón Cerrar diálogo** → Cerrar y marcar leídas

---

**Diseñado con Material Design 3**  
**Optimizado para Android 7.0+ (API 24+)**

