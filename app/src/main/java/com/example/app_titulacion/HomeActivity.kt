package com.example.app_titulacion

import android.content.Context
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_titulacion.databinding.ActivityAuthBinding
import com.example.app_titulacion.databinding.ActivityHomeBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.security.Timestamp

enum class ProviterType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

//Variable Base de Datos
private val db = FirebaseFirestore.getInstance()

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Vista
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        val token: String? = bundle?.getString("token")


        //Setup
        setup(email ?: "", provider ?: "")


        //Guardado de datos
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        //Guardado de datos FireBaseCloud
        guardarUsuarioLogeado(email ?: "",provider ?: "",token ?: "")

    }

    private fun guardarUsuarioLogeado(
        email: String,
        provider: String,
        token: String
    ) {
        db.collection("users").document()
            .set(
                hashMapOf(
                    "Token" to token,
                    "Email" to email,
                    "Provider" to provider
                ))
    }



    private fun setup(email: String, provider: String) {
        title = "Pantalla de inicio"

        with(binding) {
            emailTextView.text = email
            providerTextView.text = provider

            logOutButton.setOnClickListener {

                //Borrado de datos
                val prefs =
                    getSharedPreferences(
                        getString(R.string.prefs_file),
                        Context.MODE_PRIVATE
                    ).edit()
                prefs.clear()
                prefs.apply()

                //Cierra Sesión Facebook
                if (provider == ProviterType.FACEBOOK.name) {
                    LoginManager.getInstance().logOut()
                }

                //Cierra Sesión Google
                FirebaseAuth.getInstance().signOut()
                onBackPressed()
            }

            errorButton.setOnClickListener {

                //Envio de datos
                FirebaseCrashlytics.getInstance().setUserId(email)
                FirebaseCrashlytics.getInstance().setCustomKey("provider", provider)

                //Enviar log de contexto
                FirebaseCrashlytics.getInstance().log("Se ha pulsado el boton FORZAR ERROR.")

                //Forzado de error
                throw RuntimeException("Forzado de error")
            }

        }
    }
}