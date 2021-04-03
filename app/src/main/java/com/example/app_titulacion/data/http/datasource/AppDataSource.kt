package com.example.app_titulacion.data.http.datasource

import com.example.app_titulacion.data.IAppDataSource
import com.example.app_titulacion.data.http.ApiService
import com.example.app_titulacion.data.http.dto.EmailDto
import com.example.app_titulacion.data.http.response.*
import com.example.app_titulacion.utils.ResourceV2
import javax.inject.Inject

class AppDataSource @Inject constructor(private val apiService: ApiService) : IAppDataSource,
    BaseDataSource() {

    override suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse> =
        getResult {
            apiService.contactUpdateToken(EmailDto(email))
        }

    override suspend fun sendNotificationSos(email: String): ResourceV2<NotificationSosResponse> =
        getResult {
            apiService.sendNotificationSos(email)
        }

    override suspend fun sendNotificationAcosoSexual(email: String): ResourceV2<NotificationAcosoSexualResponse> =
        getResult {
            apiService.sendNotificationAcosoSexual(email)
        }

    override suspend fun sendNotificationAgresionFisica(email: String): ResourceV2<NotificationAgresionFisicaResponse> =
        getResult {
            apiService.sendNotificationAgresionFisica(email)
        }

    override suspend fun sendNotificationAgresionVerbal(email: String): ResourceV2<NotificationAgresionVerbalResponse> =
        getResult {
            apiService.sendNotificationAgresionVerbal(email)
        }

    override suspend fun getListaNotificaciones(email: String): ResourceV2<ListaNotificacionResponse> =
        getResult {
            apiService.getListaNotificaciones(EmailDto(email))
        }



}
