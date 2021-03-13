package com.example.app_titulacion.data.model

data class UserModel(
    val email: String,
    val provider: String? = "",
    val password: String? = "",
    val token: String,
    val location: List<String>? = listOf(),
)