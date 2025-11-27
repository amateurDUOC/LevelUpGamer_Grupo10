package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.remote.RetrofitClient
import com.grupo10.levelupgamer.data.remote.dto.RegisterRequest
import com.grupo10.levelupgamer.data.remote.service.AddressSuggestion
import com.grupo10.levelupgamer.data.remote.service.GeocodingService
import com.grupo10.levelupgamer.model.SignupErrors
import com.grupo10.levelupgamer.model.SignupUIState
import com.grupo10.levelupgamer.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignupUIState())
    val state: StateFlow<SignupUIState> = _state

    private val _registeredUser = MutableStateFlow<User?>(null)
    val registeredUser: StateFlow<User?> = _registeredUser

    private val geocodingService = GeocodingService()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estados para sugerencias de dirección
    private val _addressSuggestions = MutableStateFlow<List<AddressSuggestion>>(emptyList())
    val addressSuggestions: StateFlow<List<AddressSuggestion>> = _addressSuggestions

    private val _isLoadingSuggestions = MutableStateFlow(false)
    val isLoadingSuggestions: StateFlow<Boolean> = _isLoadingSuggestions

    private var searchJob: Job? = null

    fun onNameChange(value : String) {
        _state.update { it.copy(name = value, errors = it.errors.copy(name = null)) }
    }

    fun onLastNameChange(value : String) {
        _state.update { it.copy(lastName = value, errors = it.errors.copy(lastName = null)) }
    }

    fun onRutChange(value : String) {
        _state.update { it.copy(rut = value, errors = it.errors.copy(rut = null)) }
    }

    fun onEmailChange(value : String) {
        _state.update { it.copy(email = value, errors = it.errors.copy(email = null)) }
    }

    fun onPasswordChange(value : String) {
        _state.update { it.copy(password = value, errors = it.errors.copy(password = null)) }
    }

    fun onConfirmPasswordChange(value : String) {
        _state.update { it.copy(confirmPassword = value, errors = it.errors.copy(confirmPassword = null)) }
    }

    fun onAddressChange(value: String) {
        _state.update { it.copy(address = value, errors = it.errors.copy(address = null)) }

        // Buscar sugerencias con debounce (esperar 500ms después de que el usuario deje de escribir)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce de 500ms
            if (value.length >= 3) {
                searchAddressSuggestions(value)
            } else {
                _addressSuggestions.value = emptyList()
            }
        }
    }

    private fun searchAddressSuggestions(query: String) {
        viewModelScope.launch {
            _isLoadingSuggestions.value = true

            val result = geocodingService.getAddressSuggestions(query)

            if (result.isSuccess) {
                _addressSuggestions.value = result.getOrNull() ?: emptyList()
            } else {
                _addressSuggestions.value = emptyList()
            }

            _isLoadingSuggestions.value = false
        }
    }

    fun selectAddressSuggestion(suggestion: AddressSuggestion) {
        _state.update { it.copy(address = suggestion.displayName) }
        _addressSuggestions.value = emptyList() // Limpiar sugerencias
    }

    fun clearAddressSuggestions() {
        _addressSuggestions.value = emptyList()
    }

    private fun isValidEmailDomain(email: String): Boolean {
        val allowedDomains = listOf("@duoc.cl", "@gmail.com")
        return allowedDomains.any { domain -> email.trim().lowercase().endsWith(domain) }
    }

    fun validateSignupForm() : Boolean {
        val actualState = _state.value

        // Validar dominio del correo
        val emailError = when {
            actualState.email.isBlank() -> "Debe ingresar un correo electrónico"
            !isValidEmailDomain(actualState.email) -> "Solo se permiten correos @duoc.cl o @gmail.com"
            else -> null
        }

        val errors = SignupErrors(
            name = if (actualState.name.isBlank()) "Debe ingresar un nombre" else null,
            lastName = if (actualState.lastName.isBlank()) "Debe ingresar un apellido" else null,
            rut = if (actualState.rut.isBlank()) "Debe ingresar un RUT" else null,
            email = emailError,
            password = if (actualState.password.isBlank()) "Debe ingresar una contraseña" else null,
            confirmPassword = if (actualState.confirmPassword.isBlank()) "Debe confirmar la contraseña" else if (actualState.confirmPassword != actualState.password) "Las contraseñas no coinciden" else null,
            address = if (actualState.address.isBlank()) "Debe ingresar una dirección" else null
        )

        val foundErrors = listOfNotNull(
            errors.name,
            errors.lastName,
            errors.rut,
            errors.email,
            errors.password,
            errors.confirmPassword,
            errors.address
        )

        val hasErrors = foundErrors.isNotEmpty()

        if (hasErrors) {
            _state.update { it.copy(errors = errors) }
        }

        return !hasErrors
    }

    fun signup() {
        if (!validateSignupForm()) return

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val currentState = _state.value

                // 1. Geocodificar la dirección usando OpenStreetMap
                val locationResult = geocodingService.getCoordinatesFromAddress(currentState.address)

                if (locationResult.isFailure) {
                    _state.update {
                        it.copy(
                            signupError = "No se pudo verificar la dirección. Por favor, verifica que sea correcta."
                        )
                    }
                    _isLoading.value = false
                    return@launch
                }

                val location = locationResult.getOrNull()!!

                // 2. Registrar usuario en el backend
                val registerRequest = RegisterRequest(
                    name = "${currentState.name} ${currentState.lastName}",
                    email = currentState.email,
                    password = currentState.password,
                    address = currentState.address,
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                val response = RetrofitClient.authApi.register(registerRequest)

                if (response.isSuccessful && response.body()?.success == true) {
                    val authData = response.body()!!.data!!

                    // Guardar token
                    RetrofitClient.setAuthToken(authData.token)

                    // Crear usuario
                    val newUser = User(
                        id = authData.user.id.toIntOrNull() ?: 1,
                        email = authData.user.email,
                        name = authData.user.name,
                        address = authData.user.address,
                        latitude = authData.user.latitude,
                        longitude = authData.user.longitude
                    )

                    _registeredUser.value = newUser
                    _state.update { it.copy(signupSuccess = true, userId = newUser.id) }
                } else {
                    val errorMessage = response.body()?.message
                        ?: response.body()?.errors?.firstOrNull()?.msg
                        ?: "Error al registrar usuario"
                    _state.update { it.copy(signupError = errorMessage) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(signupError = "Error de conexión: ${e.message}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}