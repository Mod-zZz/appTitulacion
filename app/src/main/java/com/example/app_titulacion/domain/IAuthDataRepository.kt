package com.example.app_titulacion.domain

import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult

interface IAuthDataRepository {
    suspend fun signIn(email: String, password: String): Resource<AuthResult>
    suspend fun signUp(email: String, password: String, name: String): Resource<Boolean>
}