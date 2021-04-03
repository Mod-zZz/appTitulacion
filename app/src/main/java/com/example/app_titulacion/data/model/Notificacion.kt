package com.example.app_titulacion.data.model

data class Notificacion(
    val de: String? = "",
    val fecha: Long? = 0,
    val incidente: String? = "",
    val origen: Boolean? = false,
    val para: String? = "",
    val resultado: Boolean? = false,
)
