package com.example.app_titulacion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.app_titulacion.databinding.ActivityHomeBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore

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
        if (validarUsuario(email!!, provider!!, token!!) == false) {
            guardarUsuarioLogeado(email ?: "", provider ?: "", token ?: "")

        } else {
            // Mostrar que ya existe

        }


    }

    private fun validarUsuario(
        email: String,
        provider: String,
        token: String
    ): Boolean {

        val TAG = "validarUsuario"

        var isExist = false
        db.collection("users").whereEqualTo("Email", email)
            .get().addOnSuccessListener { documents ->
                isExist = documents.count() > 0

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        return isExist

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
                )
            )
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