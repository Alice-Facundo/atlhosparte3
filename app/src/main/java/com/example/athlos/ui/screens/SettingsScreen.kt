package com.example.athlos.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.athlos.notifications.NotificationScheduler
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Message
import kotlinx.coroutines.launch
import java.util.Calendar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


val Context.dataStore by preferencesDataStore(name = "settings")
val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
val ANIMATIONS_ENABLED_KEY = booleanPreferencesKey("animations_enabled")
val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
val TRAINING_REMINDER_ENABLED_KEY = booleanPreferencesKey("training_reminder_enabled")
val WATER_REMINDER_ENABLED_KEY = booleanPreferencesKey("water_reminder_enabled")

val MEAL_BREAKFAST_REMINDER_ENABLED_KEY = booleanPreferencesKey("meal_breakfast_reminder_enabled")
val MEAL_LUNCH_REMINDER_ENABLED_KEY = booleanPreferencesKey("meal_lunch_reminder_enabled")
val MEAL_DINNER_REMINDER_ENABLED_KEY = booleanPreferencesKey("meal_dinner_reminder_enabled")
val MEAL_SNACKS_REMINDER_ENABLED_KEY = booleanPreferencesKey("meal_snacks_reminder_enabled")

val MEAL_BREAKFAST_HOUR_KEY = intPreferencesKey("meal_breakfast_hour")
val MEAL_BREAKFAST_MINUTE_KEY = intPreferencesKey("meal_breakfast_minute")
val MEAL_LUNCH_HOUR_KEY = intPreferencesKey("meal_lunch_hour")
val MEAL_LUNCH_MINUTE_KEY = intPreferencesKey("meal_lunch_minute")
val MEAL_DINNER_HOUR_KEY = intPreferencesKey("meal_dinner_hour")
val MEAL_DINNER_MINUTE_KEY = intPreferencesKey("meal_dinner_minute")
val MEAL_SNACKS_HOUR_KEY = intPreferencesKey("meal_snacks_hour")
val MEAL_SNACKS_MINUTE_KEY = intPreferencesKey("meal_snacks_minute")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val darkModeEnabledState = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }.collectAsState(initial = false)
    val darkModeEnabled = darkModeEnabledState.value


    val animationsEnabledState = context.dataStore.data.map { preferences ->
        preferences[ANIMATIONS_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val animationsEnabled = animationsEnabledState.value

    val notificationsEnabledState = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val notificationsEnabled = notificationsEnabledState.value

    val trainingReminderEnabledState = context.dataStore.data.map { preferences ->
        preferences[TRAINING_REMINDER_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val trainingReminderEnabled = trainingReminderEnabledState.value

    val waterReminderEnabledState = context.dataStore.data.map { preferences ->
        preferences[WATER_REMINDER_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val waterReminderEnabled = waterReminderEnabledState.value

    val mealBreakfastReminderEnabledState = context.dataStore.data.map { preferences ->
        preferences[MEAL_BREAKFAST_REMINDER_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val mealBreakfastReminderEnabled = mealBreakfastReminderEnabledState.value

    val mealLunchReminderEnabledState = context.dataStore.data.map { preferences ->
        preferences[MEAL_LUNCH_REMINDER_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val mealLunchReminderEnabled = mealLunchReminderEnabledState.value

    val mealDinnerReminderEnabledState = context.dataStore.data.map { preferences ->
        preferences[MEAL_DINNER_REMINDER_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val mealDinnerReminderEnabled = mealDinnerReminderEnabledState.value

    val mealSnacksReminderEnabledState = context.dataStore.data.map { preferences ->
        preferences[MEAL_SNACKS_REMINDER_ENABLED_KEY] ?: true
    }.collectAsState(initial = true)
    val mealSnacksReminderEnabled = mealSnacksReminderEnabledState.value

    val currentCalendar = remember { Calendar.getInstance() }
    val defaultHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val defaultMinute = currentCalendar.get(Calendar.MINUTE)

    val breakfastHourState = context.dataStore.data.map { it[MEAL_BREAKFAST_HOUR_KEY] ?: 8 }.collectAsState(initial = 8)
    val breakfastHour = breakfastHourState.value
    val breakfastMinuteState = context.dataStore.data.map { it[MEAL_BREAKFAST_MINUTE_KEY] ?: 0 }.collectAsState(initial = 0)
    val breakfastMinute = breakfastMinuteState.value

    val lunchHourState = context.dataStore.data.map { it[MEAL_LUNCH_HOUR_KEY] ?: 12 }.collectAsState(initial = 12)
    val lunchHour = lunchHourState.value
    val lunchMinuteState = context.dataStore.data.map { it[MEAL_LUNCH_MINUTE_KEY] ?: 0 }.collectAsState(initial = 0)
    val lunchMinute = lunchMinuteState.value

    val dinnerHourState = context.dataStore.data.map { it[MEAL_DINNER_HOUR_KEY] ?: 19 }.collectAsState(initial = 19)
    val dinnerHour = dinnerHourState.value
    val dinnerMinuteState = context.dataStore.data.map { it[MEAL_DINNER_MINUTE_KEY] ?: 0 }.collectAsState(initial = 0)
    val dinnerMinute = dinnerMinuteState.value

    val snacksHourState = context.dataStore.data.map { it[MEAL_SNACKS_HOUR_KEY] ?: 16 }.collectAsState(initial = 16)
    val snacksHour = snacksHourState.value
    val snacksMinuteState = context.dataStore.data.map { it[MEAL_SNACKS_MINUTE_KEY] ?: 0 }.collectAsState(initial = 0)
    val snacksMinute = snacksMinuteState.value


    var notificationMessage by remember { mutableStateOf("") }
    var notificationHour by remember { mutableStateOf(defaultHour) }
    var notificationMinute by remember { mutableStateOf(defaultMinute) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scheduleUserNotification(context, notificationMessage, notificationHour, notificationMinute)
        } else {
            Toast.makeText(context, "Permissão de notificação negada.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Configurações",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Preferências",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Divider(modifier = Modifier.padding(bottom = 16.dp))

        PreferenceSwitchRow(
            title = "Modo Escuro",
            checked = darkModeEnabled,
            onCheckedChange = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[DARK_MODE_KEY] = isChecked
                    }
                }
            }
        )

        PreferenceSwitchRow(
            title = "Animações",
            checked = animationsEnabled,
            onCheckedChange = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[ANIMATIONS_ENABLED_KEY] = isChecked
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Notificações",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Divider(modifier = Modifier.padding(bottom = 16.dp))

        PreferenceSwitchRow(
            title = "Notificações Gerais",
            checked = notificationsEnabled,
            onCheckedChange = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[NOTIFICATIONS_ENABLED_KEY] = isChecked
                    }
                }
            }
        )

        PreferenceSwitchRow(
            title = "Lembrar de Treinar",
            checked = trainingReminderEnabled,
            onCheckedChange = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[TRAINING_REMINDER_ENABLED_KEY] = isChecked
                    }
                }
                Toast.makeText(context, "Lembrete de Treino: ${if (isChecked) "Ativado" else "Desativado"}", Toast.LENGTH_SHORT).show()
            }
        )

        PreferenceSwitchRow(
            title = "Lembrar de Beber Água",
            checked = waterReminderEnabled,
            onCheckedChange = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[WATER_REMINDER_ENABLED_KEY] = isChecked
                    }
                }
                Toast.makeText(context, "Lembrete de Beber Água: ${if (isChecked) "Ativado" else "Desativado"}", Toast.LENGTH_SHORT).show()
            }
        )

        Text(
            text = "Notificações de Refeição",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        MealNotificationSetting(
            title = "Café da Manhã",
            enabled = mealBreakfastReminderEnabled,
            hour = breakfastHour,
            minute = breakfastMinute,
            onToggle = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_BREAKFAST_REMINDER_ENABLED_KEY] = isChecked
                    }
                }
            },
            onTimeSelected = { newHour, newMinute ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_BREAKFAST_HOUR_KEY] = newHour
                        preferences[MEAL_BREAKFAST_MINUTE_KEY] = newMinute
                    }
                }
            }
        )

        MealNotificationSetting(
            title = "Almoço",
            enabled = mealLunchReminderEnabled,
            hour = lunchHour,
            minute = lunchMinute,
            onToggle = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_LUNCH_REMINDER_ENABLED_KEY] = isChecked
                    }
                }
            },
            onTimeSelected = { newHour, newMinute ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_LUNCH_HOUR_KEY] = newHour
                        preferences[MEAL_LUNCH_MINUTE_KEY] = newMinute
                    }
                }
            }
        )

        MealNotificationSetting(
            title = "Jantar",
            enabled = mealDinnerReminderEnabled,
            hour = dinnerHour,
            minute = dinnerMinute,
            onToggle = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_DINNER_REMINDER_ENABLED_KEY] = isChecked
                    }
                }
            },
            onTimeSelected = { newHour, newMinute ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_DINNER_HOUR_KEY] = newHour
                        preferences[MEAL_DINNER_MINUTE_KEY] = newMinute
                    }
                }
            }
        )

        MealNotificationSetting(
            title = "Lanches/Outros",
            enabled = mealSnacksReminderEnabled,
            hour = snacksHour,
            minute = snacksMinute,
            onToggle = { isChecked ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_SNACKS_REMINDER_ENABLED_KEY] = isChecked
                    }
                }
            },
            onTimeSelected = { newHour, newMinute ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[MEAL_SNACKS_HOUR_KEY] = newHour
                        preferences[MEAL_SNACKS_MINUTE_KEY] = newMinute
                    }
                }
            }
        )


        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "Agendar Notificação Personalizada (Única)",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = notificationMessage,
            onValueChange = { notificationMessage = it },
            label = { Text("Mensagem da Notificação") },
            leadingIcon = { Icons.Default.Message.let { Icon(it, contentDescription = "Mensagem") } },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    context,
                    { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                        notificationHour = selectedHour
                        notificationMinute = selectedMinute
                        Toast.makeText(context, "Horário selecionado: %02d:%02d".format(selectedHour, selectedMinute), Toast.LENGTH_SHORT).show()
                    }, hour, minute, true
                )
                timePickerDialog.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Schedule, contentDescription = "Selecionar Horário")
            Spacer(Modifier.width(8.dp))
            Text("Selecionar Horário: %02d:%02d".format(notificationHour, notificationMinute))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (notificationMessage.isBlank()) {
                    Toast.makeText(context, "Por favor, insira uma mensagem para a notificação.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            scheduleUserNotification(context, notificationMessage, notificationHour, notificationMinute)
                        }
                        else -> {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    scheduleUserNotification(context, notificationMessage, notificationHour, notificationMinute)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Notifications, contentDescription = "Agendar Notificação")
            Spacer(Modifier.width(8.dp))
            Text("Agendar Notificação")
        }
    }
}

@Composable
fun PreferenceSwitchRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}


@Composable
fun MealNotificationSetting(
    title: String,
    enabled: Boolean,
    hour: Int,
    minute: Int,
    onToggle: (Boolean) -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title)
            Spacer(Modifier.height(4.dp))
            Text(
                text = String.format("Horário: %02d:%02d", hour, minute),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(Modifier.width(8.dp))
        Switch(checked = enabled, onCheckedChange = onToggle)
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                        onTimeSelected(selectedHour, selectedMinute)
                        Toast.makeText(context, "Horário ${title} selecionado: %02d:%02d".format(selectedHour, selectedMinute), Toast.LENGTH_SHORT).show()
                    }, hour, minute, true
                )
                timePickerDialog.show()
            },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Icon(Icons.Default.Schedule, contentDescription = "Definir Horário")
        }
    }
}


private fun scheduleUserNotification(context: Context, message: String, hour: Int, minute: Int) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    if (calendar.before(Calendar.getInstance())) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    NotificationScheduler.scheduleNotification(
        context,
        "Lembrete Athlos",
        message,
        calendar.timeInMillis
    )
    Toast.makeText(context, "Notificação agendada para %02d:%02d!".format(hour, minute), Toast.LENGTH_LONG).show()
}