package com.example.app_titulacion.utils

import android.util.Log
import com.example.app_titulacion.utils.Constants.FIREBASE_PREF
import com.example.app_titulacion.utils.Constants.FIREBASE_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("newToken", token);
        getSharedPreferences(FIREBASE_PREF, MODE_PRIVATE).edit().putString(FIREBASE_TOKEN, token).apply();
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }
}