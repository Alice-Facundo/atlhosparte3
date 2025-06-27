package com.example.athlos.ui.screens.signinscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.athlos.R
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterExerciseScreen(navController: NavHostController, viewModel: RegisterViewModel) {
    val context = LocalContext.current

    val activityLevels = listOf(
        "Sedentário (Pouco ou nenhum exercício)",
        "Levemente ativo (Exercício leve 1–3 dias/semana)",
        "Moderadamente ativo (Exercício moderado 3–5 dias/semana)",
        "Muito ativo (Exercício intenso 6–7 dias/semana)",
        "Extremamente ativo (Atleta/Exercício pesado + trabalho físico)"
    )

    var selectedLevel by remember { mutableStateOf(viewModel.exerciseDaysPerWeek) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.exercise))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(180.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "E pra finalizar, o quão ativo você é normalmente?",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Sua atividade física é um fator importante para os cálculos de calorias!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        // Opções de Nível de Atividade (Radio Buttons ou Dropdown)
        // Usando Dropdown (ExposedDropdownMenuBox) para manter a consistência com outras telas
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedLevel,
                onValueChange = { },
                readOnly = true,
                label = { Text("Nível de Atividade") },
                placeholder = { Text("Selecione seu nível de atividade") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Nível de Atividade") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = defaultTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                activityLevels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            selectedLevel = level
                            viewModel.practicesExercises = true // Assumindo que se ele escolhe um nível, pratica exercícios
                            viewModel.exerciseDaysPerWeek = level // Salva a string do nível de atividade
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedLevel.isNotBlank()) {
                    // TODO: Aqui você pode ter uma tela de resumo final ou navegar para a tela principal do app
                    // Por exemplo: mainNavController.navigate("main") { popUpTo("register") { inclusive = true } }
                    // Para este exemplo, vamos para a tela principal
                    navController.navigate("register_summary_or_main") // Próxima tela
                } else {
                    Toast.makeText(context, "Por favor, selecione seu nível de atividade.", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = selectedLevel.isNotBlank(),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Finalizar Cadastro")
        }
    }
}

// Reutilize esta função de cores
@Composable
private fun defaultTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
)