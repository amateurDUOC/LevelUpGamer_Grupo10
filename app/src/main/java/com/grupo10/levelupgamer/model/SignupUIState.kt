package com.grupo10.levelupgamer.model

data class SignupUIState(
    val name : String = "",
    val lastName : String = "",
    val rut : String = "",
    val email : String = "",
    val password : String = "",
    val confirmPassword : String = "",
    val address: String = "",
    val errors : SignupErrors = SignupErrors(),
    val signupSuccess: Boolean = false,
    val signupError: String? = null,
    val userId: Int? = null
)