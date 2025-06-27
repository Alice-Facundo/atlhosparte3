package com.example.athlos.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.athlos.ui.models.FoodItem
import com.example.athlos.ui.theme.AthlosTheme
import com.example.athlos.ui.theme.DarkGreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import java.util.UUID

private val MealTitleBackgroundColor = Color(0xFFE0E0E0)
private val FoodCardBackgroundColor = Color(0xFFF5F5F5)

@Composable
fun DiaryScreen() {
    val totalKcal = remember { mutableStateOf(1200) }
    val breakfastFoods = remember { mutableStateListOf<FoodItem>() }
    val lunchFoods = remember { mutableStateListOf<FoodItem>() }
    val dinnerFoods = remember { mutableStateListOf<FoodItem>() }
    val snacksOtherFoods = remember { mutableStateListOf<FoodItem>() }

    val dummyFood = FoodItem(
        id = UUID.randomUUID().toString(),
        name = "Frango Grelhado",
        quantity = 100f,
        unit = "g",
        protein = 25f,
        carbohydrate = 0f,
        fiber = 0f,
        fat = 3f,
        calories = 165
    )

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
            MacroProgressItem("FIBRA", 60, Color(0xFF4CAF50))
        }

        Spacer(modifier = Modifier.height(16.dp))

        MealSection("CAFÉ DA MANHÃ", breakfastFoods) {
            breakfastFoods.add(dummyFood.copy(id = UUID.randomUUID().toString(), name = "Pão Integral"))
        }
        MealSection("ALMOÇO", lunchFoods) {
            lunchFoods.add(dummyFood.copy(id = UUID.randomUUID().toString(), name = "Arroz e Feijão"))
        }
        MealSection("JANTAR", dinnerFoods) {
            dinnerFoods.add(dummyFood.copy(id = UUID.randomUUID().toString(), name = "Salada Mista"))
        }
        MealSection("LANCHES/OUTROS", snacksOtherFoods) {
            snacksOtherFoods.add(dummyFood.copy(id = UUID.randomUUID().toString(), name = "Maçã"))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MealSection(title: String, foods: List<FoodItem>, onAddFood: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
        // Fundo colorido para o título da refeição
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MealTitleBackgroundColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
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
                IconButton(onClick = onAddFood) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar alimento", tint = MaterialTheme.colorScheme.primary)
                }
            }
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

@Composable
fun FoodItemCard(food: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FoodCardBackgroundColor), // cor personalizada aqui
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
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
    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }

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
