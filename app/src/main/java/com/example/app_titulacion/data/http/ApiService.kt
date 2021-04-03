package com.example.app_titulacion.data.http

import com.example.app_titulacion.data.http.dto.EmailDto
import com.example.app_titulacion.data.http.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

//    "https://poblacion-vulnerable.herokuapp.com/api/contacto/actualizartoken"

    @POST("contacto/actualizartoken")
    suspend fun contactUpdateToken(
        @Body dto: EmailDto
    ): Response<ContactUpdateTokenResponse>

    @GET("notificacion/sos")
    suspend fun sendNotificationSos(
        @Query("email") email : String
    ): Response<NotificationSosResponse>

    @GET("notificacion/AcosoSexual")
    suspend fun sendNotificationAcosoSexual(
        @Query("email") email : String
    ): Response<NotificationAcosoSexualResponse>

    @GET("notificacion/AgresionFisica")
    suspend fun sendNotificationAgresionFisica(
        @Query("email") email : String
    ): Response<NotificationAgresionFisicaResponse>

    @GET("notificacion/AgresionVerbal")
    suspend fun sendNotificationAgresionVerbal(
        @Query("email") email : String
    ): Response<NotificationAgresionVerbalResponse>

    @POST("notificacion/traerListaNotificaciones")
    suspend fun getListaNotificaciones(
        @Body dto: EmailDto
    ): Response<ListaNotificacionResponse>
}