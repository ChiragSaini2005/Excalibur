package com.example.juicetracker.ui.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.juicetracker.R

@Composable
fun BatteryHealthGraph() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, size.height * 0.8f)
                lineTo(size.width * 0.25f, size.height * 0.4f)
                lineTo(size.width * 0.5f, size.height * 0.7f)
                lineTo(size.width * 0.75f, size.height * 0.3f)
                lineTo(size.width, size.height * 0.6f)
            }
            drawPath(path, color = Color.Black, style = Stroke(width = 5f))
        }
    }

}

@Composable
fun ChargingStatusGraph() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White)
    ) {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, size.height * 0.6f)
            lineTo(size.width * 0.2f, size.height * 0.3f)
            lineTo(size.width * 0.4f, size.height * 0.7f)
            lineTo(size.width * 0.6f, size.height * 0.2f)
            lineTo(size.width * 0.8f, size.height * 0.8f)
            lineTo(size.width, size.height * 0.5f)
        }
        drawPath(path, color = Color.Black, style = Stroke(width = 5f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryScreen(
    userName: String,
    modelYear: String,
    carBrand: String,
    carModel: String,
    navController: NavHostController
) {
    var expandedHealth by remember { mutableStateOf(true) }
    var expandedCharging by remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juice Tracker", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
                ) {
                    Text(
                        text = "Hello $userName, your $modelYear $carBrand $carModel's battery life is 69%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            item {
                ExpandableCard(
                    title = "State of Health Vs Depth of Discharge",
                    isExpanded = expandedHealth,
                    onToggle = { expandedHealth = !expandedHealth },
                    content = { BatteryHealthGraph() }
                )
            }

            item {
                ExpandableCard(
                    title = "Temperature Vs Current",
                    isExpanded = expandedCharging,
                    onToggle = { expandedCharging = !expandedCharging },
                    content = { ChargingStatusGraph() }
                )
            }

            item {
                Button(
                    onClick = {
                        val encodedUserName = java.net.URLEncoder.encode(userName, "UTF-8")
                        val encodedCarBrand = java.net.URLEncoder.encode(carBrand, "UTF-8")
                        val encodedCarModel = java.net.URLEncoder.encode(carModel, "UTF-8")

                        navController.navigate("serviceBooking/$encodedUserName/$encodedCarBrand/$encodedCarModel")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book Servicing")
                }
            }
        }
    }

}

@Composable
fun ExpandableCard(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(modifier = Modifier.animateContentSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onToggle() }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = "Drop-down arrow",
                        tint = Color.White
                    )
                }
            }


            if (isExpanded) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White)
                        .animateContentSize() // Smooth expansion animation
                ) {
                    content()
                }
            }

        }
    }
}
