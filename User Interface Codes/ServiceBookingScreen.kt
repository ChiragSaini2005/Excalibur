package com.example.juicetracker.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import com.example.juicetracker.BuildConfig
import kotlinx.coroutines.launch


data class ServiceCenter(val name: String, val address: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceBookingScreen(navController: NavHostController, userName: String, carBrand: String, carModel: String) {
    val context = LocalContext.current
    var serviceCenters by remember { mutableStateOf<List<ServiceCenter>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val apiKey = BuildConfig.GOOGLE_PLACES_API_KEY

    suspend fun fetchServiceCenters(context: Context, carBrand: String, carModel: String) {
        serviceCenters = emptyList()
        isLoading = true

        val query = "nearest $carBrand $carModel service center"
        val urlString = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=${query.replace(" ", "%20")}&key=$apiKey"

        try {
            val response = withContext(Dispatchers.IO) {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.inputStream.bufferedReader().readText()
            }

            val jsonResponse = JSONObject(response)
            val results = jsonResponse.getJSONArray("results")

            val centers = mutableListOf<ServiceCenter>()
            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val name = item.getString("name")
                val address = item.getString("formatted_address")
                centers.add(ServiceCenter(name, address))
            }

            serviceCenters = centers
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find Service Centers", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Hello $userName, find the nearest service centers for your $carBrand $carModel",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (apiKey.isNotBlank()) {
                            fetchServiceCenters(
                                context = context,
                                carBrand = carBrand,
                                carModel = carModel
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Find Service Centers")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    items(serviceCenters) { center ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.elevatedCardElevation()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(center.name, style = MaterialTheme.typography.titleMedium)
                                Text(center.address, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
