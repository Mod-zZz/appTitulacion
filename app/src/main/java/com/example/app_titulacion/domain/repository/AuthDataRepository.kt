package com.example.app_titulacion.domain.repository

import com.example.app_titulacion.data.IAuthDataSource
import com.example.app_titulacion.domain.IAuthDataRepository
import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class AuthDataRepository @Inject constructor(private val authDataSource: IAuthDataSource) :
    IAuthDataRepository {
    override suspend fun signIn(email: String, password: String): Resource<AuthResult> =
        authDataSource.signInFb(email, password)

    override suspend fun signUp(email: String, password: String, name: String): Resource<Boolean> =
        authDataSource.signUpFb(email, password, name)
}