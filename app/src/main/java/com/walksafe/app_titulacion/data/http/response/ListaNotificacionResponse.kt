package com.walksafe.app_titulacion.data.http.response
import com.walksafe.app_titulacion.data.model.Notificacion

open class ListaNotificacionResponse {
    val ok: Boolean? = false
    val data: List<Notificacion>? = listOf()
}