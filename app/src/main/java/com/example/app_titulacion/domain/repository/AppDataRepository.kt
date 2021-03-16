package com.example.app_titulacion.domain.repository

import com.example.app_titulacion.data.http.datasource.AppDataSource
import com.example.app_titulacion.data.http.response.ContactUpdateTokenResponse
import com.example.app_titulacion.domain.IAppDataRepository
import com.example.app_titulacion.utils.ResourceV2
import javax.inject.Inject

class AppDataRepository @Inject constructor(private val appDataSource: AppDataSource): IAppDataRepository {
    override suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse> {
        return appDataSource.contactUpdateToken(email)
    }
}