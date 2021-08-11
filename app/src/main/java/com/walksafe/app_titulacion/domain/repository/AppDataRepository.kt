package com.walksafe.app_titulacion.domain.repository

import com.walksafe.app_titulacion.data.http.datasource.AppDataSource
import com.walksafe.app_titulacion.data.http.response.*
import com.walksafe.app_titulacion.domain.IAppDataRepository
import com.walksafe.app_titulacion.utils.ResourceV2
import javax.inject.Inject

class AppDataRepository @Inject constructor(private val appDataSource: AppDataSource): IAppDataRepository {
    override suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse> {
        return appDataSource.contactUpdateToken(email)
    }

    override suspend fun sendNotification(email: String, latitud: String, longitud: String): ResourceV2<NotificationSosResponse> {
        return  appDataSource.sendNotificationSos(email,latitud,longitud)
    }

    override suspend fun sendNotificationAcosoSexual(email: String, latitud: String, longitud: String): ResourceV2<NotificationAcosoSexualResponse> {
        return  appDataSource.sendNotificationAcosoSexual(email,latitud,longitud)
    }

    override suspend fun sendNotificationAgresionFisica(email: String, latitud: String, longitud: String): ResourceV2<NotificationAgresionFisicaResponse> {
        return  appDataSource.sendNotificationAgresionFisica(email,latitud,longitud)
    }

    override suspend fun sendNotificationAgresionVerbal(email: String, latitud: String, longitud: String): ResourceV2<NotificationAgresionVerbalResponse> {
        return  appDataSource.sendNotificationAgresionVerbal(email,latitud,longitud)
    }

    override suspend fun getListaNotificaciones(email: String): ResourceV2<ListaNotificacionResponse> {
        return  appDataSource.getListaNotificaciones(email)
    }

}