package com.example.app_titulacion.domain

import com.example.app_titulacion.data.http.response.ContactUpdateTokenResponse
import com.example.app_titulacion.utils.ResourceV2

interface IAppDataRepository {
    suspend fun contactUpdateToken(email: String): ResourceV2<ContactUpdateTokenResponse>
}