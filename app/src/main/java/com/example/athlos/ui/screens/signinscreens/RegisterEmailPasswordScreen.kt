package com.example.athlos.ui.screens.signinscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.athlos.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import android.widget.Toast
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation // Importe esta


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmailPasswordScreen(navController: NavHostController, viewModel: RegisterViewModel) {
    val context = LocalContext.current

    var emailInput by remember { mutableStateOf(viewModel.email) }
    var passwordInput by remember { mutableStateOf(viewModel.password) }
    var confirmPasswordInput by remember { mutableStateOf("") }

    var emailIsValid by remember { mutableStateOf(viewModel.isEmailValid()) }
    var passwordIsValid by remember { mutableStateOf(viewModel.isPasswordValid()) }
    var passwordsMatch by remember { mutableStateOf(false) }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.email)) // Use o nome do seu arquivo JSON
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )

    LaunchedEffect(passwordInput, confirmPasswordInput) {
        passwordsMatch = passwordInput == confirmPasswordInput && passwordInput.isNotBlank()
    }


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
            modifier = Modifier.size(150.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Para finalizar, seu email e senha",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "E-mail para login e recuperação de conta, senha para sua segurança!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = emailInput,
            onValueChange = {
                emailInput = it
                viewModel.email = it
                emailIsValid = viewModel.isEmailValid()
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            trailingIcon = {
                if (emailInput.isNotBlank()) {
                    Icon(
                        imageVector = if (emailIsValid) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = "Email Validação",
                        tint = if (emailIsValid) Color.Green else Color.Red
                    )
                }
            },
            colors = defaultTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = {
                passwordInput = it
                viewModel.password = it
                passwordIsValid = viewModel.isPasswordValid()
            },
            label = { Text("Senha") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisibility) "Ocultar senha" else "Mostrar senha"

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (passwordInput.isNotBlank()) {
                        Icon(
                            imageVector = if (passwordIsValid) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = "Senha Validação",
                            tint = if (passwordIsValid) Color.Green else Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            },
            colors = defaultTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPasswordInput,
            onValueChange = {
                confirmPasswordInput = it
            },
            label = { Text("Confirme a Senha") },
            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            trailingIcon = {
                val image = if (confirmPasswordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (confirmPasswordVisibility) "Ocultar senha" else "Mostrar senha"

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (confirmPasswordInput.isNotBlank()) {
                        Icon(
                            imageVector = if (passwordsMatch) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = "Confirmação Senha Validação",
                            tint = if (passwordsMatch) Color.Green else Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp)) 
                    }
                    IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            },
            colors = defaultTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (emailIsValid && passwordIsValid && passwordsMatch) {
                    // TODO: Navegar para a próxima tela (Prática de Exercícios ou Resumo)
                    navController.navigate("register_exercise")
                } else {
                    val message = when {
                        !emailIsValid -> "Por favor, insira um email válido."
                        !passwordIsValid -> "A senha deve ter pelo menos 6 caracteres."
                        !passwordsMatch -> "As senhas não coincidem."
                        else -> "Preencha todos os campos corretamente."
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            enabled = emailIsValid && passwordIsValid && passwordsMatch,
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