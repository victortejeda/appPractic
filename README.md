# 📱 MyPractica2 - Aplicación Android Multi-Proyecto

## 📋 Descripción
MyPractica2 es una aplicación Android desarrollada en Kotlin que contiene múltiples mini-proyectos educativos. La aplicación demuestra el uso de Jetpack Compose, navegación, y diferentes funcionalidades de Android.

## 🚀 Características Principales

### 🏠 Pantalla Principal
- **Navegación centralizada** entre proyectos
- **Diseño moderno** con tarjetas interactivas
- **Tema adaptativo** (modo claro/oscuro)
- **Gradientes personalizados** de fondo

### 📞 Simulador de Llamada
- **Reproducción de tonos** del sistema
- **Control de estado** (llamando/parado)
- **Interfaz intuitiva** con botones de control
- **Gestión automática** de recursos de audio

### 🎲 Juego "Adivina el Número"
- **Generación aleatoria** de números (0-10000)
- **Memoria visual** - muestra el número brevemente
- **Validación de entrada** del usuario
- **Feedback inmediato** con Toast messages

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Kotlin** | 1.9+ | Lenguaje de programación principal |
| **Jetpack Compose** | 1.5+ | Framework de UI declarativa |
| **Navigation Compose** | 2.7+ | Navegación entre pantallas |
| **Material Design 3** | 1.1+ | Sistema de diseño |
| **Android SDK** | 34+ | APIs nativas de Android |

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/mypractica2/
├── MainActivity.kt              # Actividad principal
├── ui/theme/                    # Temas y estilos
│   ├── Color.kt                # Definición de colores
│   ├── Theme.kt                # Configuración de tema
│   └── Type.kt                 # Tipografías
└── res/                        # Recursos de la aplicación
    ├── values/
    │   ├── colors.xml          # Colores XML
    │   ├── strings.xml         # Cadenas de texto
    │   └── themes.xml          # Temas XML
    └── drawable/               # Imágenes y vectores
```

## 🔧 Configuración del Proyecto

### Requisitos Previos
- Android Studio Hedgehog | 2023.1.1 o superior
- Android SDK 34+
- Kotlin 1.9+
- Gradle 8.0+

### Instalación
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un emulador o dispositivo

## 📖 Documentación del Código

### Clases Principales

#### MainActivity
```kotlin
class MainActivity : ComponentActivity()
```
- **Propósito**: Punto de entrada principal de la aplicación
- **Responsabilidades**: 
  - Configuración del tema
  - Inicialización de la navegación
  - Gestión del ciclo de vida

#### AppNavigation
```kotlin
@Composable
fun AppNavigation()
```
- **Propósito**: Configuración de navegación principal
- **Rutas disponibles**:
  - `"home"` → Pantalla principal
  - `"calling_screen"` → Simulador de llamada
  - `"guess_the_number"` → Juego adivina el número

### Pantallas (Screens)

#### HomeScreen
- **Funcionalidad**: Pantalla principal con lista de proyectos
- **Componentes**: Tarjetas de proyecto interactivas
- **Navegación**: Permite acceso a otros módulos

#### CallingScreen
- **Funcionalidad**: Simulación de llamada telefónica
- **Características**:
  - Reproducción de tono de llamada
  - Control de estado (llamando/parado)
  - Gestión automática de recursos de audio
- **APIs utilizadas**: `RingtoneManager`, `DisposableEffect`

#### GuessTheNumberScreen
- **Funcionalidad**: Juego de memoria numérica
- **Características**:
  - Generación de números aleatorios
  - Validación de entrada del usuario
  - Feedback visual con Toast messages
- **APIs utilizadas**: `LaunchedEffect`, `Toast`

### Componentes Reutilizables

#### ProjectCard
```kotlin
@Composable
fun ProjectCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
)
```
- **Propósito**: Tarjeta interactiva para mostrar proyectos
- **Características**: Diseño Material Design 3 con elevación

#### AppBackground
```kotlin
@Composable
fun AppBackground(content: @Composable BoxScope.() -> Unit)
```
- **Propósito**: Fondo con gradiente adaptativo
- **Características**: Cambia automáticamente según el tema

#### AppTopBar
```kotlin
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateUp: () -> Unit
)
```
- **Propósito**: Barra superior de navegación
- **Características**: Navegación hacia atrás y título personalizable

## 🎨 Sistema de Diseño

### Colores
- **Primary**: Color principal de la aplicación
- **Background**: Gradientes adaptativos
- **Surface**: Colores de superficie para tarjetas
- **OnPrimary/OnSurface**: Colores de texto

### Tipografía
- **Display Medium**: Títulos principales
- **Headline Medium**: Encabezados de sección
- **Title Large**: Títulos de tarjetas
- **Body Medium**: Texto de contenido

### Espaciado
- **16dp**: Padding estándar
- **20dp**: Espaciado entre elementos
- **24dp**: Espaciado de sección
- **40dp**: Espaciado grande

## 🔄 Flujo de Navegación

```
HomeScreen
├── CallingScreen (Simulador de Llamada)
│   └── Volver a HomeScreen
└── GuessTheNumberScreen (Juego)
    └── Volver a HomeScreen
```

## 🧪 Testing

### Pruebas Unitarias
- `ExampleUnitTest.kt`: Pruebas básicas de ejemplo
- Ubicación: `app/src/test/java/com/example/mypractica2/`

### Pruebas de Instrumentación
- `ExampleInstrumentedTest.kt`: Pruebas de UI
- Ubicación: `app/src/androidTest/java/com/example/mypractica2/`

## 📱 Permisos Requeridos

La aplicación requiere los siguientes permisos:
- **AUDIO**: Para reproducir tonos de llamada
- **INTERNET**: Para funcionalidades futuras (opcional)

## 🚀 Próximas Mejoras

- [ ] Agregar más mini-proyectos
- [ ] Implementar persistencia de datos
- [ ] Agregar animaciones de transición
- [ ] Soporte para tablets
- [ ] Tests de integración
- [ ] Documentación de API

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Victor Tejeda**
- GitHub: [@victortejeda](https://github.com/victortejeda)

## 📞 Contacto

- Email: victor.tejeda@example.com
- LinkedIn: [Victor Tejeda](https://linkedin.com/in/victortejeda)

---

**Nota**: Esta aplicación es un proyecto educativo para practicar desarrollo Android con Kotlin y Jetpack Compose. 