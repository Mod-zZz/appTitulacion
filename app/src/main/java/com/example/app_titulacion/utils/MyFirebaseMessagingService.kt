package com.example.app_titulacion.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.app_titulacion.R
import com.example.app_titulacion.utils.Constants.FIREBASE_PREF
import com.example.app_titulacion.utils.Constants.FIREBASE_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("newToken", token);
        getSharedPreferences(FIREBASE_PREF, MODE_PRIVATE).edit().putString(FIREBASE_TOKEN, token).apply();

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Looper.prepare()

        Handler().post(){
            Toast.makeText(baseContext, remoteMessage.notification?.body,Toast.LENGTH_LONG).show()
        }

        Looper.loop()
    }
}