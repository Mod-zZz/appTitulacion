package com.walksafe.app_titulacion.domain.repository

import com.walksafe.app_titulacion.data.IAuthDataSource
import com.walksafe.app_titulacion.data.model.UserModel
import com.walksafe.app_titulacion.domain.IAuthDataRepository
import com.walksafe.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class AuthDataRepository @Inject constructor(private val authDataSource: IAuthDataSource) :
    IAuthDataRepository {
    override suspend fun signIn(email: String, password: String): Resource<AuthResult> =
        authDataSource.signInFb(email, password)

    override suspend fun signUp(user: UserModel): Resource<AuthResult> =
        authDataSource.signUpFb(user)

    override suspend fun createUser(user: UserModel): Resource<Any> =
        authDataSource.createUser(user)

}