package com.example.app_titulacion.data.http.datasource

import com.example.app_titulacion.data.IAppDataSource
import com.example.app_titulacion.data.http.ApiService
import com.example.app_titulacion.data.http.response.ContactUpdateTokenResponse
import com.example.app_titulacion.utils.ResourceV2
import javax.inject.Inject

class AppDataSource @Inject constructor(private val apiService: ApiService) : IAppDataSource,
    BaseDataSource() {
    override suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse> =
        getResult {
            apiService.contactUpdateToken(email)
        }
}