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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mypractica2Theme {
                AppNavigation()
            }
        }
    }
}

// --- NAVEGACIÓN PRINCIPAL ---
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("calling_screen") { CallingScreen(navController = navController) }
        composable("guess_the_number") { GuessTheNumberScreen(navController = navController) }
    }
}

// --- PANTALLAS ---

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    AppBackground {
        Scaffold(containerColor = Color.Transparent, modifier = modifier) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Mis Proyectos", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Selecciona una de las aplicaciones", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(40.dp))
                ProjectCard(
                    title = "Simular Llamada",
                    description = "Inicia un tono de llamada con control.",
                    icon = Icons.Default.Call,
                    onClick = { navController.navigate("calling_screen") }
                )
                Spacer(modifier = Modifier.height(20.dp))
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

@Composable
fun CallingScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isCalling = remember { mutableStateOf(false) }

    DisposableEffect(isCalling.value) {
        var ringtone: Ringtone? = null
        if (isCalling.value) {
            val ringtoneUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
            ringtone?.play()
        }
        onDispose { ringtone?.stop() }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { AppTopBar("Simular Llamada", true) { navController.popBackStack() } }
        ) { innerPadding ->
            Column(
                modifier = modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isCalling.value) {
                    Text("Llamando...", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { isCalling.value = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Parar Llamada") }
                } else {
                    Text("Presiona para llamar", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { isCalling.value = true }) { Text("Llamar") }
                }
            }
        }
    }
}

@Composable
fun GuessTheNumberScreen(navController: NavController, modifier: Modifier = Modifier) {
    val randomNumber = remember { (0..10000).random() }
    val userInput = remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Toast.makeText(context, "Recuerda: $randomNumber", Toast.LENGTH_SHORT).show()
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = { AppTopBar("Adivina el Número", true) { navController.popBackStack() } }
        ) { innerPadding ->
            Column(
                modifier = modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ingresa el número que se te mostró", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = userInput.value,
                    onValueChange = { userInput.value = it },
                    label = { Text("Número") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    val message = if (userInput.value.toIntOrNull() == randomNumber) "¡Correcto! 🎉" else "Incorrecto. Intenta de nuevo."
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }) { Text("Verificar") }
            }
        }
    }
}

// --- COMPONENTES REUTILIZABLES ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
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
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}

@Composable
fun AppBackground(content: @Composable BoxScope.() -> Unit) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, canNavigateBack: Boolean, onNavigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
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