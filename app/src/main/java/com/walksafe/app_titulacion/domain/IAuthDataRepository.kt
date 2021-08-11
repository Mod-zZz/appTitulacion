package com.walksafe.app_titulacion.domain

import com.walksafe.app_titulacion.data.model.UserModel
import com.walksafe.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult

interface IAuthDataRepository {
    suspend fun signIn(email: String, password: String): Resource<AuthResult>
    suspend fun signUp(user: UserModel): Resource<AuthResult>
    suspend fun createUser(user: UserModel): Resource<Any>
}