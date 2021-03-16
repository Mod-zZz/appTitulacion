package com.example.app_titulacion.data.http

import com.example.app_titulacion.data.http.dto.ContactUpdateTokenDto
import com.example.app_titulacion.data.http.response.ContactUpdateTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("contacto/actualizartoken")
    suspend fun contactUpdateToken(
        @Body dto: ContactUpdateTokenDto
    ): Response<ContactUpdateTokenResponse>
}