package com.example.app_titulacion.data.http.response
import com.example.app_titulacion.data.model.Notificacion

open class ListaNotificacionResponse {
    val ok: Boolean? = false
    val data: List<Notificacion>? = listOf()
}