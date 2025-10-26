package com.grupo10.levelupgamer.model

data class SignupErrors(
    val name : String? = null,
    val lastName : String? = null,
    val rut : String? = null,
    val email : String? = null,
    val password : String? = null,
    val confirmPassword : String? = null,
    val address : String? = null
)