package com.walksafe.app_titulacion.data.http.datasource

import com.walksafe.app_titulacion.data.IAppDataSource
import com.walksafe.app_titulacion.data.http.ApiService
import com.walksafe.app_titulacion.data.http.dto.EmailDto
import com.walksafe.app_titulacion.data.http.response.*
import com.walksafe.app_titulacion.utils.ResourceV2
import javax.inject.Inject

class AppDataSource @Inject constructor(private val apiService: ApiService) : IAppDataSource,
    BaseDataSource() {

    override suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse> =
        getResult {
            apiService.contactUpdateToken(EmailDto(email))
        }

    override suspend fun sendNotificationSos(email: String, latitud: String, longitud: String): ResourceV2<NotificationSosResponse> =
        getResult {
            apiService.sendNotificationSos(email,latitud,longitud)
        }

    override suspend fun sendNotificationAcosoSexual(email: String,latitud: String, longitud: String): ResourceV2<NotificationAcosoSexualResponse> =
        getResult {
            apiService.sendNotificationAcosoSexual(email,latitud,longitud)
        }

    override suspend fun sendNotificationAgresionFisica(email: String,latitud: String, longitud: String): ResourceV2<NotificationAgresionFisicaResponse> =
        getResult {
            apiService.sendNotificationAgresionFisica(email,latitud,longitud)
        }

    override suspend fun sendNotificationAgresionVerbal(email: String,latitud: String, longitud: String): ResourceV2<NotificationAgresionVerbalResponse> =
        getResult {
            apiService.sendNotificationAgresionVerbal(email,latitud,longitud)
        }

    override suspend fun getListaNotificaciones(email: String): ResourceV2<ListaNotificacionResponse> =
        getResult {
            apiService.getListaNotificaciones(EmailDto(email))
        }



}
