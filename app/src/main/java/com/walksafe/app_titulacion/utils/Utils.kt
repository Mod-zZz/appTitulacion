package com.walksafe.app_titulacion.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.walksafe.app_titulacion.utils.Constants.FIREBASE_PREF
import com.walksafe.app_titulacion.utils.Constants.FIREBASE_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun Fragment.showAlert(title: String, message: String, positiveButtonText: String) {
    val builder = AlertDialog.Builder(requireContext())
    with(builder) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonText, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

fun getNewToken(context: Context): String? {
    return context.getSharedPreferences(FIREBASE_PREF, FirebaseMessagingService.MODE_PRIVATE)
        .getString(FIREBASE_TOKEN, "empty")
}