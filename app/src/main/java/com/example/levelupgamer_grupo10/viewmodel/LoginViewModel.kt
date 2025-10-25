package com.example.levelupgamer_grupo10.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelupgamer_grupo10.model.LoginErrors
import com.example.levelupgamer_grupo10.model.LoginUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginUIState())

    val state : StateFlow<LoginUIState> = _state

    fun onEmailChange(value : String) {
        _state.update { it.copy(email = value, errors = it.errors.copy(email = null)) }
    }

    fun onPasswordChange(value : String) {
        _state.update { it.copy(password = value, errors = it.errors.copy(password = null)) }
    }

    fun validateLoginForm() : Boolean {
        val actualState = _state.value
        val errors = LoginErrors(
            email = if (actualState.email.isBlank()) "Debe ingresar un correo electrónico" else null,
            password = if (actualState.password.isBlank()) "Debe ingresar una contraseña" else null
        )

        val foundErrors = listOfNotNull(
            errors.email,
            errors.password
        ).isNotEmpty()

        _state.update { it.copy(errors = errors) }

        return !foundErrors
    }
}