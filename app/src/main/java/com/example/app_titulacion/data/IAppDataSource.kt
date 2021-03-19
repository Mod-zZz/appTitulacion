package com.example.app_titulacion.data

import com.example.app_titulacion.data.http.response.*
import com.example.app_titulacion.utils.ResourceV2

interface IAppDataSource {
    suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse>

    suspend fun sendNotificationSos(email: String): ResourceV2<NotificationSosResponse>

    suspend fun sendNotificationAcosoSexual(email: String): ResourceV2<NotificationAcosoSexualResponse>

    suspend fun sendNotificationAgresionFisica(email: String): ResourceV2<NotificationAgresionFisicaResponse>

    suspend fun sendNotificationAgresionVerbal(email: String): ResourceV2<NotificationAgresionVerbalResponse>
}