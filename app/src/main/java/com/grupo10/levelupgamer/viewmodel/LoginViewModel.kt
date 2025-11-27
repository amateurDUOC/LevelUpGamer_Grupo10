package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo10.levelupgamer.model.LoginErrors
import com.grupo10.levelupgamer.model.LoginUIState
import com.grupo10.levelupgamer.util.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginUIState())

    val state : StateFlow<LoginUIState> = _state

    private companion object {
        private const val VALID_EMAIL = "admin@duoc.cl"
        private const val VALID_PASSWORD = "123456"
    }

    fun onEmailChange(value : String) {
        _state.update { it.copy(email = value, errors = LoginErrors(), loginError = null) }
    }

    fun onPasswordChange(value : String) {
        _state.update { it.copy(password = value, errors = LoginErrors(), loginError = null) }
    }

    fun login() {
        _state.update { it.copy(loginError = null) }

        if (!validateForm()) return

        val currentState = _state.value
        if (currentState.email == VALID_EMAIL && currentState.password == VALID_PASSWORD) {
            _state.update { it.copy(loginSuccess = true, userId = 1) } // TODO: Obtener ID real del usuario
        } else {
            _state.update { it.copy(loginError = "Correo o contraseña incorrectos") }
        }
    }

    // Solicita a la vista que inicie la autenticación biométrica
    fun onBiometricLoginRequested() {
        _state.update { it.copy(loginError = null, showBiometricPrompt = true) }
    }

    // La vista llama a este método después de mostrar el diálogo biométrico
    fun onBiometricPromptHandled() {
        _state.update { it.copy(showBiometricPrompt = false) }
    }

    // La vista llama a este método si la autenticación biométrica es exitosa
    fun onBiometricAuthSuccess() {
        _state.update { it.copy(loginSuccess = true, userId = 1) } // TODO: Obtener ID real del usuario
    }

    // La vista llama a este método si la autenticación biométrica falla o hay un error
    fun onBiometricAuthError(error: String) {
        _state.update { it.copy(loginError = error) }
    }

    private fun validateForm(): Boolean {
        val currentState = _state.value
        val emailError = if (currentState.email.isBlank()) {
            "Debe ingresar un correo electrónico"
        } else if (currentState.email != VALID_EMAIL && !EmailValidator.isValidEmail(currentState.email)) {
            "El formato del correo no es válido"
        } else {
            null
        }

        val passwordError = if (currentState.password.isBlank()) "Debe ingresar una contraseña" else null

        val hasErrors = emailError != null || passwordError != null

        if (hasErrors) {
            _state.update {
                it.copy(errors = LoginErrors(email = emailError, password = passwordError))
            }
        }
        return !hasErrors
    }
}

