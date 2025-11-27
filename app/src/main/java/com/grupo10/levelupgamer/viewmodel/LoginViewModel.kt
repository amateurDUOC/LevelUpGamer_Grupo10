package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.remote.RetrofitClient
import com.grupo10.levelupgamer.data.remote.dto.LoginRequest
import com.grupo10.levelupgamer.model.LoginErrors
import com.grupo10.levelupgamer.model.LoginUIState
import com.grupo10.levelupgamer.model.User
import com.grupo10.levelupgamer.util.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginUIState())
    val state: StateFlow<LoginUIState> = _state

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onEmailChange(value: String) {
        _state.update { it.copy(email = value, errors = LoginErrors(), loginError = null) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, errors = LoginErrors(), loginError = null) }
    }

    fun login() {
        _state.update { it.copy(loginError = null) }

        if (!validateForm()) return

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val currentState = _state.value
                val loginRequest = LoginRequest(
                    email = currentState.email,
                    password = currentState.password
                )

                val response = RetrofitClient.authApi.login(loginRequest)

                if (response.isSuccessful && response.body()?.success == true) {
                    val authData = response.body()!!.data!!

                    // Guardar token
                    RetrofitClient.setAuthToken(authData.token)

                    // Crear usuario
                    val user = User(
                        id = authData.user.id.toIntOrNull() ?: 1,
                        email = authData.user.email,
                        name = authData.user.name,
                        address = authData.user.address,
                        latitude = authData.user.latitude,
                        longitude = authData.user.longitude
                    )

                    _currentUser.value = user
                    _state.update { it.copy(loginSuccess = true, userId = user.id) }
                } else {
                    val errorMessage = response.body()?.message ?: "Credenciales inválidas"
                    _state.update { it.copy(loginError = errorMessage) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(loginError = "Error de conexión: ${e.message}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onBiometricLoginRequested() {
        _state.update { it.copy(showBiometricPrompt = true) }
    }

    // La vista llama a este método después de mostrar el diálogo biométrico
    fun onBiometricPromptHandled() {
        _state.update { it.copy(showBiometricPrompt = false) }
    }

    // La vista llama a este método si la autenticación biométrica es exitosa
    fun onBiometricAuthSuccess() {
        // Usar credenciales guardadas o predefinidas para login automático
        _state.update { it.copy(email = "admin@duoc.cl", password = "123456") }
        login()
    }

    // La vista llama a este método si la autenticación biométrica falla o hay un error
    fun onBiometricAuthError(error: String) {
        _state.update { it.copy(loginError = error) }
    }

    private fun validateForm(): Boolean {
        val currentState = _state.value
        val emailError = if (currentState.email.isBlank()) {
            "Debe ingresar un correo electrónico"
        } else if (!EmailValidator.isValidEmail(currentState.email)) {
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

