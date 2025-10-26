package com.example.levelupgamer_grupo10.model

data class LoginUIState(
    val email : String = "",
    val password : String = "",
    val errors : LoginErrors = LoginErrors(),
    val loginSuccess: Boolean = false,
    val loginError: String? = null,
    val showBiometricPrompt: Boolean = false
)