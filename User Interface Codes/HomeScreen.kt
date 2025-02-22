package com.example.juicetracker.ui.theme


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("battery/{name}/{year}/{brand}/{model}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""
            val brand = backStackEntry.arguments?.getString("brand") ?: ""
            val model = backStackEntry.arguments?.getString("model") ?: ""
            BatteryScreen(name, year, brand, model, navController)
        }
        composable("serviceBooking/{name}/{brand}/{model}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val brand = backStackEntry.arguments?.getString("brand") ?: ""
            val model = backStackEntry.arguments?.getString("model") ?: ""
            ServiceBookingScreen(navController, name, brand, model)
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var showTitle by remember { mutableStateOf(true) }
    var showContent by remember { mutableStateOf(false) }
    val typedText = remember { mutableStateOf("") }
    val fullText = "Juice Tracker"

    LaunchedEffect(Unit) {
        for (i in fullText.indices) {
            delay(150L)
            typedText.value = fullText.substring(0, i + 1)
        }
        delay(1000L)
        showTitle = false
        delay(500L)
        showContent = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = showTitle,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Text(
                text = typedText.value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Juice Tracker", fontWeight = FontWeight.Bold) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputField(label = "Name", value = name, onValueChange = { name = it })
                    InputField(label = "Car Brand", value = brand, onValueChange = { brand = it })
                    InputField(label = "Model", value = model, onValueChange = { model = it })
                    InputField(label = "Model Year", value = year, onValueChange = { year = it }, keyboardType = KeyboardType.Number)

                    Button(
                        onClick = {
                            navController.navigate("battery/$name/$year/$brand/$model")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Go")
                    }
                }
            }
        }
    }
}



@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
        )
    }
}

@Composable
fun JuiceTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFBB86FC),
            onPrimary = Color.Black,
            background = Color(0xFF121212),
            onBackground = Color.White,
            surfaceVariant = Color(0xFF1E1E1E)
        ),
        content = content
    )
}

