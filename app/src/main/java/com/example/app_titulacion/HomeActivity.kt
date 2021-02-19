package com.example.app_titulacion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_titulacion.databinding.ActivityAuthBinding
import com.example.app_titulacion.databinding.ActivityHomeBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase

enum class ProviterType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

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

        //Setup
        setup(email ?: "", provider ?: "")

        //Guardado de datos
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

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