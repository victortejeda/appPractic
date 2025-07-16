package com.example.mypractica2

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mypractica2.ui.theme.Mypractica2Theme
import com.example.mypractica2.ui.theme.BackgroundEnd
import com.example.mypractica2.ui.theme.BackgroundStart
import com.example.mypractica2.ui.theme.DarkBackgroundEnd
import com.example.mypractica2.ui.theme.DarkBackgroundStart
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Actividad principal de la aplicación MyPractica2
 * 
 * Esta clase es el punto de entrada de la aplicación y se encarga de:
 * - Configurar el tema global de la aplicación
 * - Inicializar la navegación principal
 * - Gestionar el ciclo de vida de la actividad
 * 
 * @author Victor Tejeda
 * @version 1.0
 */
class MainActivity : ComponentActivity() {
    /**
     * Método llamado cuando la actividad se crea por primera vez
     * 
     * @param savedInstanceState Bundle que contiene el estado anterior de la actividad
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplicar el tema personalizado de la aplicación
            Mypractica2Theme {
                // Inicializar la navegación principal
                AppNavigation()
            }
        }
    }
}

// ============================================================================
// NAVEGACIÓN PRINCIPAL
// ============================================================================

/**
 * Configuración principal de navegación de la aplicación
 * 
 * Define todas las rutas disponibles y sus correspondientes pantallas:
 * - "home": Pantalla principal con lista de proyectos
 * - "calling_screen": Simulador de llamada telefónica
 * - "guess_the_number": Juego de memoria numérica
 * 
 * @return Composable que renderiza la navegación
 */
@Composable
fun AppNavigation() {
    // Controlador de navegación que persiste durante toda la vida de la app
    val navController = rememberNavController()
    
    // Configuración del grafo de navegación
    NavHost(navController = navController, startDestination = "home") {
        // Ruta: Pantalla principal
        composable("home") { 
            HomeScreen(navController = navController) 
        }
        // Ruta: Simulador de llamada
        composable("calling_screen") { 
            CallingScreen(navController = navController) 
        }
        // Ruta: Juego adivina el número
        composable("guess_the_number") { 
            GuessTheNumberScreen(navController = navController) 
        }
    }
}

// ============================================================================
// PANTALLAS PRINCIPALES
// ============================================================================

/**
 * Pantalla principal de la aplicación
 * 
 * Muestra una lista de proyectos disponibles en forma de tarjetas interactivas.
 * Cada tarjeta permite navegar a una funcionalidad específica de la aplicación.
 * 
 * @param navController Controlador de navegación para cambiar entre pantallas
 * @param modifier Modificador de composición para personalizar el layout
 */
@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Aplicar fondo con gradiente personalizado
    AppBackground {
        Scaffold(
            containerColor = Color.Transparent, 
            modifier = modifier
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Título principal de la aplicación
                Text(
                    "Mis Proyectos", 
                    style = MaterialTheme.typography.displayMedium, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Subtítulo descriptivo
                Text(
                    "Selecciona una de las aplicaciones", 
                    style = MaterialTheme.typography.titleMedium, 
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Tarjeta del proyecto "Simular Llamada"
                ProjectCard(
                    title = "Simular Llamada",
                    description = "Inicia un tono de llamada con control.",
                    icon = Icons.Default.Call,
                    onClick = { navController.navigate("calling_screen") }
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Tarjeta del proyecto "Adivinar Número"
                ProjectCard(
                    title = "Adivinar Número",
                    description = "Un juego para probar tu memoria.",
                    icon = Icons.Default.Quiz,
                    onClick = { navController.navigate("guess_the_number") }
                )
            }
        }
    }
}

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
    // Contexto de la aplicación para acceder a recursos del sistema
    val context = LocalContext.current
    
    // Estado que controla si la llamada está activa o no
    val isCalling = remember { mutableStateOf(false) }

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
        var ringtone: Ringtone? = null
        
        if (isCalling.value) {
            // Obtener el tono de llamada por defecto del sistema
            val ringtoneUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
            ringtone?.play()
        }
        
        // Limpieza automática cuando el efecto se destruye
        onDispose { 
            ringtone?.stop() 
        }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { 
                AppTopBar(
                    title = "Simular Llamada", 
                    canNavigateBack = true
                ) { 
                    navController.popBackStack() 
                } 
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isCalling.value) {
                    // Estado: Llamada activa
                    Text(
                        "Llamando...", 
                        style = MaterialTheme.typography.headlineMedium, 
                        fontWeight = FontWeight.Bold, 
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón para detener la llamada
                    Button(
                        onClick = { isCalling.value = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { 
                        Text("Parar Llamada") 
                    }
                } else {
                    // Estado: Llamada inactiva
                    Text(
                        "Presiona para llamar", 
                        style = MaterialTheme.typography.headlineSmall
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón para iniciar la llamada
                    Button(onClick = { isCalling.value = true }) { 
                        Text("Llamar") 
                    }
                }
            }
        }
    }
}

// ============================================================================
// JUEGO MEJORADO - ADIVINA EL NÚMERO
// ============================================================================

/**
 * Datos del juego para mantener el estado
 */
data class GameState(
    val targetNumber: Int = 0,
    val userGuess: String = "",
    val attempts: Int = 0,
    val maxAttempts: Int = 3,
    val score: Int = 0,
    val level: Int = 1,
    val gameStatus: GameStatus = GameStatus.PREPARING,
    val showHint: Boolean = false,
    val timeRemaining: Int = 5
)

/**
 * Estados posibles del juego
 */
enum class GameStatus {
    PREPARING,    // Preparando el juego
    SHOWING,      // Mostrando el número
    PLAYING,      // Jugando
    WON,          // Ganó
    LOST,         // Perdió
    PAUSED        // Pausado
}

/**
 * Niveles de dificultad del juego
 */
enum class DifficultyLevel(val displayName: String, val range: IntRange, val timeToShow: Int) {
    FACIL("Fácil", 0..100, 8),
    MEDIO("Medio", 0..1000, 6),
    DIFICIL("Difícil", 0..10000, 4),
    EXTREMO("Extremo", 0..100000, 3)
}

/**
 * Pantalla mejorada del juego "Adivina el Número"
 * 
 * Juego de memoria mejorado con:
 * - Múltiples niveles de dificultad
 * - Sistema de puntuación
 * - Animaciones y efectos visuales
 * - Pistas y ayudas
 * - Interfaz más atractiva
 * 
 * @param navController Controlador de navegación para volver atrás
 * @param modifier Modificador de composición para personalizar el layout
 */
@Composable
fun GuessTheNumberScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    // Estado del juego
    var gameState by remember { mutableStateOf(GameState()) }
    var selectedDifficulty by remember { mutableStateOf(DifficultyLevel.MEDIO) }
    
    // Animaciones
    val scale by animateFloatAsState(
        targetValue = if (gameState.gameStatus == GameStatus.SHOWING) 1.2f else 1f,
        animationSpec = tween(300)
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (gameState.gameStatus == GameStatus.SHOWING) 1f else 0.6f,
        animationSpec = tween(500)
    )

    /**
     * Iniciar nuevo juego
     */
    fun startNewGame() {
        val newTargetNumber = selectedDifficulty.range.random()
        gameState = gameState.copy(
            targetNumber = newTargetNumber,
            userGuess = "",
            attempts = 0,
            gameStatus = GameStatus.PREPARING
        )
    }

    /**
     * Mostrar el número al usuario
     */
    fun showNumber() {
        gameState = gameState.copy(gameStatus = GameStatus.SHOWING)
    }

    /**
     * Ocultar el número y comenzar el juego
     */
    fun hideNumber() {
        gameState = gameState.copy(gameStatus = GameStatus.PLAYING)
    }

    /**
     * Verificar la respuesta del usuario
     */
    fun checkGuess() {
        val guess = gameState.userGuess.toIntOrNull()
        if (guess != null) {
            val newAttempts = gameState.attempts + 1
            val isCorrect = guess == gameState.targetNumber
            
            if (isCorrect) {
                // Ganó
                val newScore = gameState.score + (selectedDifficulty.timeToShow * 10)
                gameState = gameState.copy(
                    gameStatus = GameStatus.WON,
                    score = newScore
                )
                Toast.makeText(context, "¡Correcto! 🎉 +${selectedDifficulty.timeToShow * 10} puntos", Toast.LENGTH_SHORT).show()
            } else {
                // Incorrecto
                if (newAttempts >= gameState.maxAttempts) {
                    // Perdió
                    gameState = gameState.copy(
                        gameStatus = GameStatus.LOST,
                        attempts = newAttempts
                    )
                    Toast.makeText(context, "¡Perdiste! El número era ${gameState.targetNumber}", Toast.LENGTH_LONG).show()
                } else {
                    // Más intentos disponibles
                    val hint = if (guess < gameState.targetNumber) "Mayor" else "Menor"
                    gameState = gameState.copy(
                        attempts = newAttempts,
                        userGuess = "",
                        showHint = true
                    )
                    Toast.makeText(context, "Incorrecto. El número es $hint", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Por favor ingresa un número válido", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Obtener pista basada en la diferencia
     */
    fun getHint(): String {
        val guess = gameState.userGuess.toIntOrNull() ?: return ""
        val difference = kotlin.math.abs(guess - gameState.targetNumber)
        return when {
            difference <= 10 -> "¡Muy cerca!"
            difference <= 50 -> "Cerca"
            difference <= 100 -> "Lejos"
            else -> "Muy lejos"
        }
    }

    // Efecto para mostrar el número
    LaunchedEffect(gameState.gameStatus) {
        when (gameState.gameStatus) {
            GameStatus.PREPARING -> {
                delay(1000)
                showNumber()
            }
            GameStatus.SHOWING -> {
                delay(selectedDifficulty.timeToShow * 1000L)
                hideNumber()
            }
            else -> {}
        }
    }

    // Efecto para ocultar pista después de 3 segundos
    LaunchedEffect(gameState.showHint) {
        if (gameState.showHint) {
            delay(3000)
            gameState = gameState.copy(showHint = false)
        }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { 
                AppTopBar(
                    title = "Adivina el Número", 
                    canNavigateBack = true
                ) { 
                    navController.popBackStack() 
                } 
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header con puntuación y nivel
                GameHeader(
                    score = gameState.score,
                    level = gameState.level,
                    attempts = gameState.attempts,
                    maxAttempts = gameState.maxAttempts
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Selector de dificultad (solo en preparación)
                if (gameState.gameStatus == GameStatus.PREPARING) {
                    DifficultySelector(
                        selectedDifficulty = selectedDifficulty,
                        onDifficultySelected = { selectedDifficulty = it }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                
                // Área principal del juego
                when (gameState.gameStatus) {
                    GameStatus.PREPARING -> {
                        PreparingGame()
                    }
                    GameStatus.SHOWING -> {
                        ShowingNumber(
                            number = gameState.targetNumber,
                            timeRemaining = selectedDifficulty.timeToShow,
                            scale = scale,
                            alpha = alpha
                        )
                    }
                    GameStatus.PLAYING -> {
                        PlayingGame(
                            userGuess = gameState.userGuess,
                            onGuessChange = { gameState = gameState.copy(userGuess = it) },
                            onCheckGuess = { checkGuess() },
                            hint = if (gameState.showHint) getHint() else "",
                            showHint = gameState.showHint
                        )
                    }
                    GameStatus.WON -> {
                        GameWon(
                            score = gameState.score,
                            onPlayAgain = { startNewGame() }
                        )
                    }
                    GameStatus.LOST -> {
                        GameLost(
                            targetNumber = gameState.targetNumber,
                            score = gameState.score,
                            onPlayAgain = { startNewGame() }
                        )
                    }
                    else -> {}
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Botón de nuevo juego
                if (gameState.gameStatus in listOf(GameStatus.WON, GameStatus.LOST)) {
                    Button(
                        onClick = { startNewGame() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jugar de Nuevo")
                    }
                }
            }
        }
    }
}

/**
 * Header del juego con puntuación y progreso
 */
@Composable
fun GameHeader(
    score: Int,
    level: Int,
    attempts: Int,
    maxAttempts: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Puntuación
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    "Puntuación",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    "$score",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Nivel
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Nivel",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    "$level",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            // Intentos
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "Intentos",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    "$attempts/$maxAttempts",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (attempts >= maxAttempts) Color.Red else MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

/**
 * Selector de dificultad
 */
@Composable
fun DifficultySelector(
    selectedDifficulty: DifficultyLevel,
    onDifficultySelected: (DifficultyLevel) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Selecciona la Dificultad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DifficultyLevel.values().forEach { difficulty ->
                DifficultyButton(
                    difficulty = difficulty,
                    isSelected = selectedDifficulty == difficulty,
                    onClick = { onDifficultySelected(difficulty) }
                )
            }
        }
    }
}

/**
 * Botón de dificultad individual
 */
@Composable
fun DifficultyButton(
    difficulty: DifficultyLevel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(200)
    )
    
    Card(
        modifier = Modifier
            .scale(scale)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) null else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                difficulty.displayName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
            Text(
                "0-${difficulty.range.last}",
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
            )
            Text(
                "${difficulty.timeToShow}s",
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
            )
        }
    }
}

/**
 * Pantalla de preparación del juego
 */
@Composable
fun PreparingGame() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Psychology,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .scale(scale),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Preparando el juego...",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "¡Prepara tu memoria!",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}

/**
 * Pantalla mostrando el número
 */
@Composable
fun ShowingNumber(
    number: Int,
    timeRemaining: Int,
    scale: Float,
    alpha: Float
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Número principal
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(scale * pulse)
                .alpha(alpha)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .border(
                    width = 4.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Contador regresivo
        Text(
            "¡Memoriza! Tiempo restante: $timeRemaining",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Iconos de memoria
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Pantalla de juego activo
 */
@Composable
fun PlayingGame(
    userGuess: String,
    onGuessChange: (String) -> Unit,
    onCheckGuess: () -> Unit,
    hint: String,
    showHint: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Instrucciones
        Text(
            "¿Cuál era el número?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "Escribe el número que viste",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Campo de entrada
        OutlinedTextField(
            value = userGuess,
            onValueChange = onGuessChange,
            label = { Text("Tu respuesta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Pista (si está disponible)
        AnimatedVisibility(
            visible = showHint,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        hint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Botón de verificar
        Button(
            onClick = onCheckGuess,
            modifier = Modifier.fillMaxWidth(),
            enabled = userGuess.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Verificar Respuesta")
        }
    }
}

/**
 * Pantalla de victoria
 */
@Composable
fun GameWon(
    score: Int,
    onPlayAgain: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de victoria
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .scale(1.2f),
            tint = Color(0xFFFFD700) // Dorado
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "¡Felicidades! 🎉",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "¡Has adivinado correctamente!",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Puntuación
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Puntuación Total",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "$score",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Confeti animado
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .scale(1f + (index * 0.1f)),
                    tint = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)[index]
                )
            }
        }
    }
}

/**
 * Pantalla de derrota
 */
@Composable
fun GameLost(
    targetNumber: Int,
    score: Int,
    onPlayAgain: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de derrota
        Icon(
            imageVector = Icons.Default.SentimentDissatisfied,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "¡Oh no! 😔",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "No pudiste adivinar el número",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Número correcto
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "El número era",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    "$targetNumber",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Puntuación final
        Text(
            "Puntuación final: $score",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Mensaje motivacional
        Text(
            "¡No te rindas! Inténtalo de nuevo",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

// ============================================================================
// COMPONENTES REUTILIZABLES
// ============================================================================

/**
 * Tarjeta interactiva para mostrar proyectos en la pantalla principal
 * 
 * Componente reutilizable que muestra información de un proyecto
 * con un icono, título, descripción y funcionalidad de click.
 * 
 * @param title Título del proyecto
 * @param description Descripción breve del proyecto
 * @param icon Icono representativo del proyecto
 * @param onClick Función que se ejecuta al hacer click en la tarjeta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    title: String, 
    description: String, 
    icon: ImageVector, 
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del proyecto
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del proyecto
            Column {
                Text(
                    text = title, 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description, 
                    style = MaterialTheme.typography.bodyMedium, 
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Fondo con gradiente adaptativo para toda la aplicación
 * 
 * Componente que proporciona un fondo con gradiente que se adapta
 * automáticamente al tema (claro/oscuro) del sistema.
 * 
 * @param content Contenido que se renderiza sobre el fondo
 */
@Composable
fun AppBackground(content: @Composable BoxScope.() -> Unit) {
    // Seleccionar colores de gradiente según el tema del sistema
    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(DarkBackgroundStart, DarkBackgroundEnd)
    } else {
        listOf(BackgroundStart, BackgroundEnd)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = gradientColors)),
        content = content
    )
}

/**
 * Barra superior de navegación personalizada
 * 
 * Componente que proporciona una barra superior consistente
 * con título, navegación hacia atrás y colores personalizados.
 * 
 * @param title Título que se muestra en la barra
 * @param canNavigateBack Si se debe mostrar el botón de navegación hacia atrás
 * @param onNavigateUp Función que se ejecuta al presionar el botón de navegación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String, 
    canNavigateBack: Boolean, 
    onNavigateUp: () -> Unit
) {
    TopAppBar(
        title = { 
            Text(title, fontWeight = FontWeight.Bold) 
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Text("⬅️", fontSize = 24.sp)
                }
            }
        }
    )
}