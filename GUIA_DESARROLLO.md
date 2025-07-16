# 🛠️ Guía de Desarrollo - MyPractica2

## 📋 Tabla de Contenidos
1. [Configuración del Entorno](#configuración-del-entorno)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Convenciones de Código](#convenciones-de-código)
4. [Flujo de Trabajo](#flujo-de-trabajo)
5. [Testing](#testing)
6. [Debugging](#debugging)
7. [Deployment](#deployment)
8. [Troubleshooting](#troubleshooting)

---

## 🚀 Configuración del Entorno

### Requisitos del Sistema
- **Android Studio**: Hedgehog | 2023.1.1 o superior
- **JDK**: 17 o superior
- **Android SDK**: API 34 (Android 14)
- **Kotlin**: 1.9+
- **Gradle**: 8.0+

### Instalación Paso a Paso

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/victortejeda/MyPractica2.git
cd MyPractica2
```

#### 2. Abrir en Android Studio
1. Abrir Android Studio
2. Seleccionar "Open an existing project"
3. Navegar a la carpeta del proyecto
4. Esperar a que se sincronice Gradle

#### 3. Configurar el Emulador
1. Ir a Tools → AVD Manager
2. Crear un nuevo dispositivo virtual
3. Recomendado: Pixel 7 con API 34
4. Iniciar el emulador

#### 4. Ejecutar la Aplicación
```bash
# Desde la terminal
./gradlew assembleDebug
./gradlew installDebug

# Desde Android Studio
Run → Run 'app'
```

---

## 📁 Estructura del Proyecto

### Organización de Archivos
```
appPractic/
├── app/                          # Módulo principal de la aplicación
│   ├── build.gradle.kts         # Configuración de dependencias
│   └── src/
│       ├── main/                # Código fuente principal
│       │   ├── java/
│       │   │   └── com/example/mypractica2/
│       │   │       ├── MainActivity.kt      # Actividad principal
│       │   │       └── ui/theme/            # Temas y estilos
│       │   │           ├── Color.kt         # Definición de colores
│       │   │           ├── Theme.kt         # Configuración de tema
│       │   │           └── Type.kt          # Tipografías
│       │   ├── res/             # Recursos de la aplicación
│       │   │   ├── values/
│       │   │   │   ├── colors.xml
│       │   │   │   ├── strings.xml
│       │   │   │   └── themes.xml
│       │   │   └── drawable/    # Imágenes y vectores
│       │   └── AndroidManifest.xml
│       ├── test/                # Pruebas unitarias
│       └── androidTest/         # Pruebas de instrumentación
├── build.gradle.kts             # Configuración del proyecto
├── gradle.properties            # Propiedades de Gradle
└── settings.gradle.kts          # Configuración de módulos
```

### Convenciones de Nomenclatura

#### Archivos Kotlin
- **Clases**: `PascalCase` (ej: `MainActivity`)
- **Funciones**: `camelCase` (ej: `onCreate`)
- **Variables**: `camelCase` (ej: `isCalling`)
- **Constantes**: `UPPER_SNAKE_CASE` (ej: `MAX_RETRY_COUNT`)

#### Archivos de Recursos
- **Layouts**: `snake_case.xml` (ej: `activity_main.xml`)
- **Drawables**: `snake_case.xml` (ej: `ic_launcher.xml`)
- **Strings**: `strings.xml`
- **Colors**: `colors.xml`

---

## 📝 Convenciones de Código

### 1. Documentación de Funciones

#### Formato KDoc
```kotlin
/**
 * Descripción breve de la función
 * 
 * Descripción detallada que puede incluir múltiples líneas
 * y explicar el comportamiento, parámetros y valor de retorno.
 * 
 * @param param1 Descripción del primer parámetro
 * @param param2 Descripción del segundo parámetro
 * @return Descripción del valor de retorno
 * @throws ExceptionType Descripción de la excepción
 * 
 * @example
 * ```kotlin
 * val result = miFuncion("valor1", 42)
 * println(result) // Output: "Resultado esperado"
 * ```
 */
fun miFuncion(param1: String, param2: Int): String {
    // Implementación
}
```

#### Ejemplo Real
```kotlin
/**
 * Pantalla del simulador de llamada telefónica
 * 
 * Permite al usuario simular una llamada telefónica reproduciendo
 * el tono de llamada del sistema. Incluye controles para iniciar
 * y detener la simulación.
 * 
 * @param navController Controlador de navegación para volver atrás
 * @param modifier Modificador de composición para personalizar el layout
 */
@Composable
fun CallingScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Implementación
}
```

### 2. Comentarios en el Código

#### Comentarios de Sección
```kotlin
// ============================================================================
// NAVEGACIÓN PRINCIPAL
// ============================================================================
```

#### Comentarios de Línea
```kotlin
// Estado que controla si la llamada está activa o no
val isCalling = remember { mutableStateOf(false) }

// Obtener el tono de llamada por defecto del sistema
val ringtoneUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
```

#### Comentarios de Bloque
```kotlin
/**
 * Efecto que gestiona la reproducción del tono de llamada
 * 
 * Se ejecuta cada vez que cambia el valor de isCalling.value
 * - Si isCalling es true: reproduce el tono
 * - Si isCalling es false: detiene el tono
 * 
 * IMPORTANTE: onDispose asegura que el tono se detenga cuando
 * el componente se destruye, evitando memory leaks
 */
DisposableEffect(isCalling.value) {
    // Implementación
}
```

### 3. Estructura de Composable

#### Orden de Elementos
```kotlin
@Composable
fun MiComposable(
    // 1. Parámetros de función
    param1: String,
    param2: Int,
    modifier: Modifier = Modifier
) {
    // 2. Estados y efectos
    val state = remember { mutableStateOf("") }
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        // Efectos secundarios
    }
    
    // 3. Lógica de negocio
    val processedData = state.value.uppercase()
    
    // 4. UI
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = processedData)
        Button(onClick = { /* acción */ }) {
            Text("Botón")
        }
    }
}
```

### 4. Gestión de Estados

#### Estados Locales
```kotlin
// ✅ Correcto - Estado local con remember
val isCalling = remember { mutableStateOf(false) }
val userInput = remember { mutableStateOf("") }

// ❌ Incorrecto - Estado que se recrea en cada recomposición
val isCalling = mutableStateOf(false)
```

#### Estados Derivados
```kotlin
// ✅ Correcto - Estado derivado optimizado
val isFormValid = derivedStateOf {
    userInput.value.isNotEmpty() && userInput.value.length >= 3
}

// ❌ Incorrecto - Cálculo en cada recomposición
val isFormValid = userInput.value.isNotEmpty() && userInput.value.length >= 3
```

---

## 🔄 Flujo de Trabajo

### 1. Desarrollo de Nuevas Funcionalidades

#### Paso 1: Crear Rama
```bash
git checkout -b feature/nueva-funcionalidad
```

#### Paso 2: Implementar Código
1. Crear/editar archivos necesarios
2. Seguir convenciones de código
3. Agregar documentación KDoc
4. Implementar tests

#### Paso 3: Testing
```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests de instrumentación
./gradlew connectedAndroidTest

# Ejecutar todos los tests
./gradlew check
```

#### Paso 4: Commit
```bash
git add .
git commit -m "feat: agregar nueva funcionalidad

- Descripción detallada de los cambios
- Lista de características agregadas
- Cambios técnicos importantes"
```

#### Paso 5: Push y Pull Request
```bash
git push origin feature/nueva-funcionalidad
# Crear Pull Request en GitHub
```

### 2. Convenciones de Commits

#### Formato
```
tipo(alcance): descripción breve

Descripción detallada opcional

- Lista de cambios
- Características agregadas
- Bugs corregidos
```

#### Tipos de Commit
- `feat`: Nueva funcionalidad
- `fix`: Corrección de bug
- `docs`: Documentación
- `style`: Formato de código
- `refactor`: Refactorización
- `test`: Tests
- `chore`: Tareas de mantenimiento

#### Ejemplos
```bash
git commit -m "feat(calling): agregar control de volumen

- Implementar slider de volumen en simulador de llamada
- Agregar persistencia de configuración de volumen
- Mejorar UX con feedback visual"

git commit -m "fix(game): corregir validación de números negativos

- Agregar validación para números negativos en juego
- Mejorar mensajes de error para el usuario"
```

---

## 🧪 Testing

### 1. Tests Unitarios

#### Ubicación
```
app/src/test/java/com/example/mypractica2/
```

#### Ejemplo de Test
```kotlin
import org.junit.Test
import org.junit.Assert.*

class MainActivityTest {
    
    @Test
    fun `should generate random number between 0 and 10000`() {
        // Given
        val minValue = 0
        val maxValue = 10000
        
        // When
        val randomNumber = (minValue..maxValue).random()
        
        // Then
        assertTrue("El número debe estar en el rango válido", 
                  randomNumber in minValue..maxValue)
    }
    
    @Test
    fun `should validate user input correctly`() {
        // Given
        val validInput = "1234"
        val invalidInput = "abc"
        
        // When & Then
        assertNotNull("Entrada válida debe convertirse a número", 
                     validInput.toIntOrNull())
        assertNull("Entrada inválida debe retornar null", 
                  invalidInput.toIntOrNull())
    }
}
```

### 2. Tests de UI

#### Ubicación
```
app/src/androidTest/java/com/example/mypractica2/
```

#### Ejemplo de Test de UI
```kotlin
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class MainActivityUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun shouldNavigateToCallingScreen() {
        // Given - Configurar la UI
        composeTestRule.setContent {
            HomeScreen(navController = mockNavController)
        }
        
        // When - Interactuar con la UI
        composeTestRule.onNodeWithText("Simular Llamada").performClick()
        
        // Then - Verificar el resultado
        composeTestRule.onNodeWithText("Presiona para llamar").assertIsDisplayed()
    }
    
    @Test
    fun shouldShowCorrectMessageWhenCalling() {
        // Given
        composeTestRule.setContent {
            CallingScreen(navController = mockNavController)
        }
        
        // When
        composeTestRule.onNodeWithText("Llamar").performClick()
        
        // Then
        composeTestRule.onNodeWithText("Llamando...").assertIsDisplayed()
        composeTestRule.onNodeWithText("Parar Llamada").assertIsDisplayed()
    }
}
```

### 3. Tests de Integración

#### Ejemplo
```kotlin
@Test
fun shouldPlayRingtoneWhenCalling() {
    // Given
    val context = ApplicationProvider.getApplicationContext<Context>()
    val ringtoneManager = RingtoneManager(context)
    
    // When
    val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
    
    // Then
    assertNotNull("El tono debe estar disponible", ringtone)
    assertTrue("El tono debe poder reproducirse", ringtone.isPlaying.not())
}
```

---

## 🐛 Debugging

### 1. Logs de Debug

#### Configuración
```kotlin
private const val TAG = "MyPractica2"

// En funciones
Log.d(TAG, "Iniciando simulación de llamada")
Log.e(TAG, "Error al reproducir tono", exception)
```

#### Filtros en Android Studio
```
// Filtrar por tag
tag:MyPractica2

// Filtrar por nivel
level:ERROR

// Filtrar por texto
text:"Llamando"
```

### 2. Breakpoints

#### Tipos de Breakpoints
- **Line Breakpoint**: Pausa en línea específica
- **Conditional Breakpoint**: Pausa solo si se cumple condición
- **Logpoint**: Registra información sin pausar
- **Exception Breakpoint**: Pausa en excepciones

#### Configuración de Breakpoint Condicional
```
// En CallingScreen, línea donde se reproduce el tono
isCalling.value == true
```

### 3. Layout Inspector

#### Uso
1. Ejecutar la app en modo debug
2. Ir a View → Tool Windows → Layout Inspector
3. Seleccionar el proceso de la app
4. Inspeccionar la jerarquía de vistas

### 4. Performance Profiler

#### Métricas a Monitorear
- **CPU**: Uso de procesador
- **Memory**: Uso de memoria
- **Network**: Actividad de red
- **Energy**: Consumo de batería

---

## 📱 Deployment

### 1. Build de Release

#### Configuración de Firmado
```kotlin
// app/build.gradle.kts
android {
    signingConfigs {
        create("release") {
            storeFile = file("keystore/release.keystore")
            storePassword = "password"
            keyAlias = "release"
            keyPassword = "password"
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

#### Generar APK
```bash
# APK de release
./gradlew assembleRelease

# Bundle de release (recomendado para Play Store)
./gradlew bundleRelease
```

### 2. Play Store

#### Preparación
1. Crear cuenta de desarrollador
2. Configurar información de la app
3. Generar bundle de release
4. Subir a Play Console

#### Checklist de Release
- [ ] Versión actualizada en `build.gradle.kts`
- [ ] Tests pasando
- [ ] APK/Bundle firmado
- [ ] Screenshots actualizadas
- [ ] Descripción de la app
- [ ] Política de privacidad

### 3. Internal Testing

#### Configuración
1. Crear track de internal testing
2. Subir APK/Bundle
3. Agregar testers
4. Enviar invitaciones

---

## 🔧 Troubleshooting

### Problemas Comunes

#### 1. Gradle Sync Fails
```bash
# Limpiar cache de Gradle
./gradlew clean
./gradlew --stop

# Invalidar cache de Android Studio
File → Invalidate Caches and Restart
```

#### 2. Build Errors
```bash
# Ver logs detallados
./gradlew assembleDebug --info

# Limpiar proyecto
./gradlew clean
```

#### 3. Emulator Issues
```bash
# Reiniciar emulador
adb kill-server
adb start-server

# Ver dispositivos conectados
adb devices
```

#### 4. Memory Leaks
```kotlin
// Verificar DisposableEffect
DisposableEffect(key) {
    // Setup
    onDispose {
        // Cleanup - SIEMPRE incluir
    }
}
```

#### 5. Navigation Issues
```kotlin
// Verificar rutas definidas
NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    // Asegurar que todas las rutas estén definidas
}
```

### Logs Útiles

#### Verificar Permisos
```bash
adb shell dumpsys package com.example.mypractica2 | grep permission
```

#### Verificar Actividad
```bash
adb shell dumpsys activity activities | grep MyPractica2
```

#### Verificar Audio
```bash
adb shell dumpsys audio | grep -A 10 "Audio Focus"
```

---

## 📚 Recursos Adicionales

### Documentación Oficial
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Material Design 3](https://m3.material.io/)

### Herramientas
- [Android Studio](https://developer.android.com/studio)
- [Layout Inspector](https://developer.android.com/studio/debug/layout-inspector)
- [Performance Profiler](https://developer.android.com/studio/profile)

### Comunidad
- [Android Developers Blog](https://android-developers.googleblog.com/)
- [Kotlin Blog](https://blog.kotlin.team/)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android+jetpack-compose)

---

**Nota**: Esta guía debe actualizarse regularmente con las mejores prácticas y cambios en el proyecto. 