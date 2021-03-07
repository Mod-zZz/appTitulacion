package com.example.app_titulacion.data.firebase

import com.example.app_titulacion.data.IAuthDataSource
import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthDataSource @Inject constructor() : IAuthDataSource {
    override suspend fun signInFb(email: String, password: String): Resource<AuthResult> {
        return try {
            val request: AuthResult =
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            Resource.Success(request)
        } catch (e: Exception) {
            Resource.Failure(e)
        }

    }


    override suspend fun signUpFb(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val request =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            Resource.Success(request)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}