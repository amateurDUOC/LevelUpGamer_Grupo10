package com.grupo10.levelupgamer.model

data class User(
    val id: Int,
    val email: String,
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

