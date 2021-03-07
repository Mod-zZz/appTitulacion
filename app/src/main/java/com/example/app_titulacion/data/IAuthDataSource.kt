package com.example.app_titulacion.data

import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult

interface IAuthDataSource {

    // region firebase
    suspend fun signInFb(email: String, password: String): Resource<AuthResult>
    suspend fun signUpFb(email: String, password: String): Resource<AuthResult>

    // endregion
}