package com.example.athlos.ui.screens.signinscreens

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.athlos.R
import java.util.Calendar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PersonOutline
import android.widget.Toast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDobGenderScreen(navController: NavHostController, viewModel: RegisterViewModel) {
    val context = LocalContext.current

    var dataNascimentoState by remember {
        mutableStateOf(
            TextFieldValue(
                if (viewModel.dobDay != null && viewModel.dobMonth != null && viewModel.dobYear != null)
                    String.format("%02d/%02d/%d", viewModel.dobDay, viewModel.dobMonth, viewModel.dobYear)
                else ""
            )
        )
    }
    var sexo by remember { mutableStateOf(viewModel.gender) }
    var sexoExpanded by remember { mutableStateOf(false) }
    val opcoesSexo = listOf("Masculino", "Feminino")

    var showYearError by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.walkingdude))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            viewModel.dobDay = selectedDayOfMonth
            viewModel.dobMonth = selectedMonth + 1
            viewModel.dobYear = selectedYear
            dataNascimentoState = TextFieldValue(
                String.format("%02d/%02d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear),
                selection = TextRange(10)
            )
            // ADICIONADO: Valida o ano imediatamente após a seleção
            showYearError = !viewModel.isDobValid() && (viewModel.dobYear != null && viewModel.dobYear!! >= Calendar.getInstance().get(Calendar.YEAR))
        }, year, month, day
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
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Qual sua data de nascimento e sexo?",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Isso nos ajuda a personalizar sua experiência!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = dataNascimentoState,
            onValueChange = {
                val filteredText = it.text.filter { char -> char.isDigit() }.take(8)
                var formatted = ""
                if (filteredText.length > 0) formatted += filteredText.substring(0, minOf(2, filteredText.length))
                if (filteredText.length > 2) formatted += "/" + filteredText.substring(2, minOf(4, filteredText.length))
                if (filteredText.length > 4) formatted += "/" + filteredText.substring(4, minOf(8, filteredText.length))

                dataNascimentoState = TextFieldValue(text = formatted, selection = TextRange(formatted.length))

                if (formatted.length == 10) {
                    val parts = formatted.split("/")
                    if (parts.size == 3) {
                        viewModel.dobDay = parts[0].toIntOrNull()
                        viewModel.dobMonth = parts[1].toIntOrNull()
                        viewModel.dobYear = parts[2].toIntOrNull()
                    }
                } else {
                    viewModel.dobDay = null
                    viewModel.dobMonth = null
                    viewModel.dobYear = null
                }
                // ADICIONADO: Atualiza o erro ao digitar também
                showYearError = !viewModel.isDobValid() && (viewModel.dobYear != null && viewModel.dobYear!! >= Calendar.getInstance().get(Calendar.YEAR))
            },
            label = { Text("Data de nascimento (DD/MM/AAAA)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            trailingIcon = {
                IconButton(onClick = {
                    datePickerDialog.show()
                    showYearError = false // Limpa o erro ao abrir o seletor
                }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar Data")
                }
            },
            colors = defaultTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        // ADICIONADO: Mensagem de erro para ano inválido
        if (showYearError) {
            Text(
                text = "Ano inválido. Por favor, selecione um ano anterior ao ano atual.",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = sexoExpanded,
            onExpandedChange = { sexoExpanded = !sexoExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = sexo,
                onValueChange = { },
                readOnly = true,
                label = { Text("Sexo") },
                placeholder = { Text("Selecione seu sexo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded) },
                leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = "Sexo") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = defaultTextFieldColors()
            )

            ExposedDropdownMenu(expanded = sexoExpanded, onDismissRequest = { sexoExpanded = false }) {
                opcoesSexo.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            sexo = option
                            viewModel.gender = option
                            sexoExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (viewModel.isDobValid() && viewModel.isGenderValid()) {
                    navController.navigate("register_weight_height")
                } else {
                    // Se o botão for clicado e a data ou sexo não forem válidos
                    // Verifique se o erro de ano precisa ser mostrado
                    if (!viewModel.isDobValid() && (viewModel.dobYear != null && viewModel.dobYear!! >= Calendar.getInstance().get(Calendar.YEAR))) {
                        showYearError = true
                    }
                    Toast.makeText(context, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = viewModel.isDobValid() && viewModel.isGenderValid(),
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