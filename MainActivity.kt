package com.example.mypractica2

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

/**
 * Pantalla del juego "Adivina el Número"
 * 
 * Juego de memoria donde se muestra un número aleatorio brevemente
 * y el usuario debe recordarlo y escribirlo correctamente.
 * 
 * @param navController Controlador de navegación para volver atrás
 * @param modifier Modificador de composición para personalizar el layout
 */
@Composable
fun GuessTheNumberScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Número aleatorio generado una sola vez al montar el componente
    val randomNumber = remember { (0..10000).random() }
    
    // Estado para almacenar la entrada del usuario
    val userInput = remember { mutableStateOf("") }
    
    // Contexto de la aplicación para mostrar Toast messages
    val context = LocalContext.current

    /**
     * Efecto que se ejecuta una sola vez al montar el componente
     * Muestra el número que el usuario debe recordar
     */
    LaunchedEffect(Unit) {
        Toast.makeText(context, "Recuerda: $randomNumber", Toast.LENGTH_SHORT).show()
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Instrucciones del juego
                Text(
                    "Ingresa el número que se te mostró", 
                    textAlign = TextAlign.Center, 
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Campo de entrada para el número del usuario
                OutlinedTextField(
                    value = userInput.value,
                    onValueChange = { userInput.value = it },
                    label = { Text("Número") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón para verificar la respuesta
                Button(onClick = {
                    // Validación segura: convertir a Int o null
                    val message = if (userInput.value.toIntOrNull() == randomNumber) {
                        "¡Correcto! 🎉"
                    } else {
                        "Incorrecto. Intenta de nuevo."
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }) { 
                    Text("Verificar") 
                }
            }
        }
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