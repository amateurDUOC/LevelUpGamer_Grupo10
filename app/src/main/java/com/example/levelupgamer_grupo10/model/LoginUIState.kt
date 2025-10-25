package com.example.levelupgamer_grupo10.model

data class LoginUIState(
    val email : String = "",
    val password : String = "",
    val errors : LoginErrors = LoginErrors()
)