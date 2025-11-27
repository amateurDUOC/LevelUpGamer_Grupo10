package com.grupo10.levelupgamer.model

data class LoginUIState(
    val email : String = "",
    val password : String = "",
    val errors : LoginErrors = LoginErrors(),
    val loginSuccess: Boolean = false,
    val loginError: String? = null,
    val showBiometricPrompt: Boolean = false,
    val userId: Int? = null
)

