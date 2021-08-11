package com.walksafe.app_titulacion.data.firebase

import com.walksafe.app_titulacion.data.IAuthDataSource
import com.walksafe.app_titulacion.data.model.UserModel
import com.walksafe.app_titulacion.utils.Constants.PROVIDER_FIELD
import com.walksafe.app_titulacion.utils.Constants.TOKEN_FIELD
import com.walksafe.app_titulacion.utils.Constants.USER_COL
import com.walksafe.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun signUpFb(user: UserModel): Resource<AuthResult> {
        return try {
            val request =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password?:"")
                    .await()
            Resource.Success(request)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun createUser(user: UserModel): Resource<Any> {
        return try {
            val request = FirebaseFirestore.getInstance().collection(USER_COL).document(user.email)
                .set(
                    hashMapOf(
                        TOKEN_FIELD to user.token,
                        PROVIDER_FIELD to user.provider
                    )
                ).await()
            Resource.Success(request)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}