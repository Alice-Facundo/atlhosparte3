package com.example.athlos.ui.screens.signinscreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Calendar

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var dobDay by mutableStateOf<Int?>(null)
    var dobMonth by mutableStateOf<Int?>(null)
    var dobYear by mutableStateOf<Int?>(null)
    var gender by mutableStateOf("")
    var weight: Float? by mutableStateOf(null)
    var height: Float? by mutableStateOf(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var practicesExercises by mutableStateOf(false)
    var exerciseDaysPerWeek by mutableStateOf("")

    fun isNameValid(): Boolean = name.isNotBlank() && name.length >= 3
    fun isDobValid(): Boolean = dobDay != null && dobMonth != null && dobYear != null && dobYear!! < Calendar.getInstance().get(Calendar.YEAR)
    fun isGenderValid(): Boolean = gender.isNotBlank()
    fun isWeightValid(): Boolean = weight != null && weight!! > 0f
    fun isHeightValid(): Boolean = height != null && height!! > 0f
    fun isEmailValid(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isPasswordValid(): Boolean = password.length >= 6

    fun calculateAge(): String {
        return if (dobDay != null && dobMonth != null && dobYear != null) {
            try {
                val dob = Calendar.getInstance().apply { set(dobYear!!, dobMonth!! - 1, dobDay!!) }
                val today = Calendar.getInstance()
                var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }
                "$age anos"
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }
    }
}