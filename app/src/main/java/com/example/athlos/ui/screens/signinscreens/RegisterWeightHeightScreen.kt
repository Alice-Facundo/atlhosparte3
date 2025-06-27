package com.example.athlos.ui.screens.signinscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.athlos.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Scale
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterWeightHeightScreen(navController: NavHostController, viewModel: RegisterViewModel) {
    val context = LocalContext.current

    var weightInput by remember { mutableStateOf(viewModel.weight?.toString() ?: "") }
    var heightInput by remember { mutableStateOf(viewModel.height?.toString() ?: "") }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.scale))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Agora você pode nos informar seu peso e altura?",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Tudo isso entra nos nossos cálculos mágicos pra te colocar no shape!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = weightInput,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() || (char == '.' && !it.contains('.')) }
                weightInput = filtered
                viewModel.weight = filtered.toFloatOrNull()
            },
            label = { Text("Seu peso (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            trailingIcon = {
                if (weightInput.isNotBlank()) {
                    if (viewModel.isWeightValid()) {
                        Icon(Icons.Default.Scale, contentDescription = "Peso Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Scale, contentDescription = "Peso Inválido", tint = Color.Red)
                    }
                }
            },
            colors = defaultTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = heightInput,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() || (char == '.' && !it.contains('.')) }
                heightInput = filtered
                viewModel.height = filtered.toFloatOrNull()
            },
            label = { Text("Sua altura (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            trailingIcon = {
                if (heightInput.isNotBlank()) {
                    if (viewModel.isHeightValid()) {
                        Icon(Icons.Default.Height, contentDescription = "Altura Válida", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Height, contentDescription = "Altura Inválida", tint = Color.Red)
                    }
                }
            },
            colors = defaultTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.isWeightValid() && viewModel.isHeightValid()) {
                    navController.navigate("register_email_password")
                } else {
                    Toast.makeText(context, "Por favor, insira valores válidos para peso e altura.", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = viewModel.isWeightValid() && viewModel.isHeightValid(),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Próximo")
        }
    }
}

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