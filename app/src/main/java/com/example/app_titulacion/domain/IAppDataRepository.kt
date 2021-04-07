package com.example.app_titulacion.domain

import com.example.app_titulacion.data.http.response.*
import com.example.app_titulacion.utils.ResourceV2

interface IAppDataRepository {
    suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse>

    suspend fun sendNotification(email: String, latitud: String, longitud: String): ResourceV2<NotificationSosResponse>

    suspend fun sendNotificationAcosoSexual(email: String, latitud: String, longitud: String): ResourceV2<NotificationAcosoSexualResponse>
    suspend fun sendNotificationAgresionFisica(email: String, latitud: String, longitud: String): ResourceV2<NotificationAgresionFisicaResponse>
    suspend fun sendNotificationAgresionVerbal(email: String, latitud: String, longitud: String): ResourceV2<NotificationAgresionVerbalResponse>

    suspend fun getListaNotificaciones(email: String): ResourceV2<ListaNotificacionResponse>

}