# Solución de Iconos de Launcher

## Problema
Los iconos de launcher actualmente son archivos XML en ubicaciones incorrectas. Android requiere archivos PNG en carpetas mipmap para iconos de launcher.

## Solución Recomendada

### Opción 1: Usar Android Studio Image Asset Studio (RECOMENDADO)
1. En Android Studio, click derecho en `res` → New → Image Asset
2. Selecciona "Launcher Icons (Adaptive and Legacy)"
3. Configura tu icono (puedes usar un color, imagen, o clipart)
4. Android Studio generará automáticamente todos los archivos necesarios en las densidades correctas

### Opción 2: Crear archivos PNG manualmente
Crea archivos PNG con estos tamaños en las siguientes carpetas:

```
app/src/main/res/
├── mipmap-mdpi/
│   ├── ic_launcher.png (48x48)
│   └── ic_launcher_round.png (48x48)
├── mipmap-hdpi/
│   ├── ic_launcher.png (72x72)
│   └── ic_launcher_round.png (72x72)
├── mipmap-xhdpi/
│   ├── ic_launcher.png (96x96)
│   └── ic_launcher_round.png (96x96)
├── mipmap-xxhdpi/
│   ├── ic_launcher.png (144x144)
│   └── ic_launcher_round.png (144x144)
└── mipmap-xxxhdpi/
    ├── ic_launcher.png (192x192)
    └── ic_launcher_round.png (192x192)
```

### Archivos a eliminar
Elimina los siguientes archivos que están en ubicaciones incorrectas:
- `app/src/main/res/drawable/ic_launcher.xml`
- `app/src/main/res/drawable/ic_launcher_round.xml`
- `app/src/main/res/mipmap-hdpi/ic_launcher_foreground.xml`

### Archivos que deben permanecer
Estos archivos están en las ubicaciones correctas:
- `app/src/main/res/drawable/ic_launcher_foreground.xml` ✓
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` ✓
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml` ✓
- `app/src/main/res/values/ic_launcher_background.xml` ✓

## Nota Temporal
Los archivos problemáticos han sido vaciados con comentarios para evitar errores de compilación. 
Una vez que generes los iconos correctamente, puedes eliminarlos completamente.

