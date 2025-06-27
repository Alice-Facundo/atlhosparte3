package com.example.athlos.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.athlos.api.FatSecretApi
import com.example.athlos.ui.models.FoodItem
import com.example.athlos.ui.models.FoodItemApi
import com.example.athlos.ui.theme.AthlosTheme
import com.example.athlos.ui.theme.DarkGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val MealTitleBackgroundColor = Color(0xFFE0E0E0)
private val FoodCardBackgroundColor = Color(0xFFF5F5F5)

@Composable
fun DiaryScreen() {
    val totalKcal = remember { mutableStateOf(1200) }
    val breakfastFoods = remember { mutableStateListOf<FoodItem>() }
    val lunchFoods = remember { mutableStateListOf<FoodItem>() }
    val dinnerFoods = remember { mutableStateListOf<FoodItem>() }
    val snacksOtherFoods = remember { mutableStateListOf<FoodItem>() }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${totalKcal.value} kcal",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(DarkGreen)
                .wrapContentSize(Alignment.Center)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MacroProgressItem("CARBOIDRATO", 70, Color(0xFFFFC107))
            MacroProgressItem("PROTEÍNA", 85, Color(0xFFD32F2F))
            MacroProgressItem("GORDURA", 60, Color(0xFF4CAF50))
        }

        Spacer(modifier = Modifier.height(16.dp))

        MealSection("CAFÉ DA MANHÃ", breakfastFoods, rememberCoroutineScope(), context)
        MealSection("ALMOÇO", lunchFoods, rememberCoroutineScope(), context)
        MealSection("JANTAR", dinnerFoods, rememberCoroutineScope(), context)
        MealSection("LANCHES/OUTROS", snacksOtherFoods, rememberCoroutineScope(), context)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MealSection(title: String, foods: MutableList<FoodItem>, scope: CoroutineScope, context: Context) {
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<FoodItemApi>()) }
    var isLoadingSearch by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MealTitleBackgroundColor, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showSearchDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar alimento", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (showSearchDialog) {
            AlertDialog(
                onDismissRequest = { showSearchDialog = false },
                title = { Text("Buscar alimento") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Digite o alimento") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            if (searchQuery.isNotBlank()) {
                                scope.launch(Dispatchers.IO) {
                                    isLoadingSearch = true
                                    try {
                                        val result = FatSecretApi.service.searchFoods(searchQuery)
                                        searchResults = result.foods.food ?: emptyList()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        launch(Dispatchers.Main) {
                                            Toast.makeText(context, "Erro na busca: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                        searchResults = emptyList()
                                    } finally {
                                        isLoadingSearch = false
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Por favor, digite algo para buscar.", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Buscar")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isLoadingSearch) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else if (searchResults.isNotEmpty()) {
                            androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                                items(searchResults, key = { it.food_id }) { item ->
                                    TextButton(onClick = {
                                        scope.launch(Dispatchers.IO) {
                                            try {
                                                val macros = parseMacros(item.food_name + " " + item.food_type)

                                                val foodItem = FoodItem(
                                                    id = item.food_id,
                                                    name = item.food_name,
                                                    quantity = (macros["quantity"] as? Float) ?: 0f,
                                                    unit = macros["unitStr"]?.toString() ?: "g",
                                                    protein = (macros["protein"] as? Float) ?: 0f,
                                                    carbohydrate = (macros["carbs"] as? Float) ?: 0f,
                                                    fiber = (macros["fiber"] as? Float) ?: 0f,
                                                    fat = (macros["fat"] as? Float) ?: 0f,
                                                    calories = (macros["calories"] as? Float)?.toInt() ?: 0
                                                )
                                                foods.add(foodItem)
                                                launch(Dispatchers.Main) {
                                                    showSearchDialog = false
                                                    searchQuery = ""
                                                    searchResults = emptyList()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                launch(Dispatchers.Main) {
                                                    Toast.makeText(context, "Erro ao adicionar alimento: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }) {
                                        Text(item.food_name + (item.brand_name?.let { " ($it)" } ?: "") + "\n" + item.food_type)
                                    }
                                }
                            }
                        } else if (searchQuery.isNotBlank()) {
                            Text("Nenhum resultado encontrado para '${searchQuery}'.")
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showSearchDialog = false }) {
                        Text("Fechar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (foods.isEmpty()) {
            Text(
                text = "Nenhum alimento adicionado. Clique no '+' para adicionar.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            foods.forEach { food ->
                FoodItemCard(food)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun parseMacros(description: String): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    val regex = Regex("""([0-9.]+)\s*(g|kcal)?""")
    val matches = regex.findAll(description)
    val numbers = matches.map { it.groupValues[1].toFloat() to it.groupValues[2] }.toList()

    map["calories"] = numbers.getOrElse(0) { 0f to "" }.first
    map["carbs"] = numbers.getOrElse(1) { 0f to "" }.first
    map["protein"] = numbers.getOrElse(2) { 0f to "" }.first
    map["fat"] = numbers.getOrElse(3) { 0f to "" }.first
    map["fiber"] = numbers.getOrElse(4) { 0f to "" }.first

    map["quantity"] = 100f
    map["unitStr"] = "g"
    return map
}

@Composable
fun FoodItemCard(food: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FoodCardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = food.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${food.quantity} ${food.unit}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Prot: ${food.protein}g | Carb: ${food.carbohydrate}g | Fib: ${food.fiber}g | Gord: ${food.fat}g | ${food.calories}kcal",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun MacroProgressItem(label: String, progress: Int, progressColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val strokeWidth = 8.dp
        ProgressRing(progress, progressColor, strokeWidth)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun ProgressRing(progress: Int, progressColor: Color, strokeWidth: Dp) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress / 100f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    val backgroundRingColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Canvas(modifier = Modifier.size(80.dp)) {
        val radius = size.minDimension / 2f

        drawCircle(
            color = backgroundRingColor,
            radius = radius,
            style = Stroke(width = strokeWidth.toPx())
        )

        drawArc(
            color = progressColor,
            startAngle = 270f,
            sweepAngle = animatedProgress.value * 360f,
            useCenter = false,
            topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
            size = Size(size.width - strokeWidth.toPx(), size.height - strokeWidth.toPx()),
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDiaryScreen() {
    AthlosTheme(darkTheme = false) {
        DiaryScreen()
    }
}
