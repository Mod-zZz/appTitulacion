package com.example.app_titulacion.data.http

import com.example.app_titulacion.data.http.response.ContactUpdateTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {

    @GET("contacto/actualizartoken")
    suspend fun contactUpdateToken(
        @Body email: String
    ): Response<ContactUpdateTokenResponse>
}