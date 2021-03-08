package com.example.app_titulacion.data

import com.example.app_titulacion.data.model.UserModel
import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult

interface IAuthDataSource {

    // region firebase auth
    suspend fun signInFb(email: String, password: String): Resource<AuthResult>
    suspend fun signUpFb(user: UserModel): Resource<AuthResult>

    // endregion

    // region firestore
    suspend fun createUser(user: UserModel): Resource<Any>
    // endregion
}