package com.example.app_titulacion.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository constructor(private val firebaseAuth: FirebaseAuth) {

    suspend fun authenticate(
        email: String,
        password: String
    ): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).await()
        return firebaseAuth.currentUser ?: throw FirebaseAuthException("", "")
    }

//    suspend fun saveDataInFireStore(childName : String,
//                                    hashMap: HashMap<String,Any>) : Boolean{
//        return try{
//            val data = firestore
//                .collection("users")
//                .document(childName)
//                .set(hashMap)
//                .await()
//            return true
//        }catch (e : Exception){
//            return false
//        }
//    }
}