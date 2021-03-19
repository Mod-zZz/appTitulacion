package com.example.app_titulacion.domain.repository

import com.example.app_titulacion.data.http.datasource.AppDataSource
import com.example.app_titulacion.data.http.response.*
import com.example.app_titulacion.domain.IAppDataRepository
import com.example.app_titulacion.utils.ResourceV2
import javax.inject.Inject

class AppDataRepository @Inject constructor(private val appDataSource: AppDataSource): IAppDataRepository {
    override suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse> {
        return appDataSource.contactUpdateToken(email)
    }

    override suspend fun sendNotification(email: String): ResourceV2<NotificationSosResponse> {
        return  appDataSource.sendNotificationSos(email)
    }

    override suspend fun sendNotificationAcosoSexual(email: String): ResourceV2<NotificationAcosoSexualResponse> {
        return  appDataSource.sendNotificationAcosoSexual(email)
    }

    override suspend fun sendNotificationAgresionFisica(email: String): ResourceV2<NotificationAgresionFisicaResponse> {
        return  appDataSource.sendNotificationAgresionFisica(email)
    }

    override suspend fun sendNotificationAgresionVerbal(email: String): ResourceV2<NotificationAgresionVerbalResponse> {
        return  appDataSource.sendNotificationAgresionVerbal(email)
    }

}