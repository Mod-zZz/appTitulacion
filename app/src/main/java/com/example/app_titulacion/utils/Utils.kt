package com.example.app_titulacion.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

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