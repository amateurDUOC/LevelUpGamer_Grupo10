package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo10.levelupgamer.model.SignupErrors
import com.grupo10.levelupgamer.model.SignupUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SignupViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignupUIState())

    val state : StateFlow<SignupUIState> = _state

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

    fun onAddressChange(value : String) {
        _state.update { it.copy(address = value, errors = it.errors.copy(address = null)) }
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
        ).isNotEmpty()

        _state.update { it.copy(errors = errors) }

        return !foundErrors
    }

    fun signup() {
        if (validateSignupForm()) {
            // TODO: Implementar lógica de registro (e.g., llamada a API)
            _state.update { it.copy(signupSuccess = true) }
        }
    }
}