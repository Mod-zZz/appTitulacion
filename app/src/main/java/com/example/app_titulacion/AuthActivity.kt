package com.example.app_titulacion

import android.accounts.AccountAuthenticatorActivity
import android.content.Context
import android.content.Intent
import android.media.Session2Token
import android.media.VolumeProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.app_titulacion.databinding.ActivityAuthBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.type.Date
import kotlinx.coroutines.awaitAll
import java.security.ProviderException
import java.security.Timestamp
import kotlin.math.sign

class AuthActivity : AppCompatActivity() {
    //Vista
    private lateinit var binding: ActivityAuthBinding

    //Variable Google
    private val GOOGLE_SIGN_IN = 100

    //Variable Facebook
    private val callbackManager = CallbackManager.Factory.create()

    //Variable Token
    var tk = ""

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
//        Splash
        Thread.sleep(2000)
        setTheme(R.style.Theme_App_Titulacion)

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_auth)

        //Vista
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        //Lanzando eventos personalizados para google analytics

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "¡¡¡Integracion de FireBase completa!!!")
        analytics.logEvent("InitScreen", bundle)

        //Setup
        notification()
        setup()
        session()

    }

    override fun onStart() {
        super.onStart()
        binding.authLayout.visibility = View.VISIBLE
    }

    private fun notification() {

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let {
                tk = it
                println("Este es el token del dispositivo: ${it}")
            }
        }

        //Temas (Topics)
//        FirebaseMessaging.getInstance().subscribeToTopic("Prueba")

        //Recuperar informacion de una notifiacion Push
//        val url = intent.getStringExtra("url")
//        url?.let {
//            println("Ha llegado información en una push: ${it}")
//        }
    }

    private fun setup() {
        title = "Autenticación"
        //addOnCompleteListener →→ Para comprobar si se completo correctamente

        with(binding) {

            //Registrar usuario con correo y contraseña
            signUpButton.setOnClickListener {
                if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                    val resp = crearUsuario(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                    if (resp) {
                        showHome(emailEditText.text.toString() ?: "", ProviterType.BASIC)
                    } else {
                        showAlert()
                    }

                }
            }

            //Iniciar sesion con google
            googleButton.setOnClickListener {
                //Configuracion
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                //Cliente de autenticacion
                val googleClient = GoogleSignIn.getClient(this@AuthActivity, googleConf)
                googleClient.signOut()
                startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
            }

            //Inicio de sesión por usuario y contraseña
            loginButton.setOnClickListener {
                if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {

                    val resp = loginUsuario(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                    if (resp) showHome(
                        emailEditText.text.toString() ?: "",
                        ProviterType.BASIC
                    ) else showAlert()

                }
            }

            //Iniciar sesión con Facebook
            facebookButton.setOnClickListener {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this@AuthActivity, listOf("email"))


                LoginManager.getInstance()
                    .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                        override fun onSuccess(result: LoginResult?) {
                            result?.let {
                                val token = it.accessToken

                                val credential =
                                    FacebookAuthProvider.getCredential(token.token)

                                FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            showHome(
                                                it.result?.user?.email ?: "",
                                                ProviterType.FACEBOOK
                                            )
                                        } else {
                                            showAlert()
                                        }
                                    }
                            }
                        }

                        override fun onCancel() {

                        }

                        override fun onError(error: FacebookException?) {
                            showAlert()
                        }

                    })
            }
        }

    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            binding.authLayout.visibility = View.INVISIBLE
            showHome(email, ProviterType.valueOf(provider))
        }
    }

    //Alerta
    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Error")
            setMessage("Se ha producido un error autenticando al usuario")
            setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun showHome(email: String, provider: ProviterType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            putExtra("token", tk)
        }
        startActivity(homeIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(account.email ?: "", ProviterType.GOOGLE)
                            } else {
                                showAlert()
                            }

                        }
                }
            } catch (e: ApiException) {
                showAlert()
            }


        }
    }

    //Captura de errores


    // Firebase Auth

    private fun crearUsuario(email: String, password: String): Boolean {
        val TAG = "crearUsuario"

        var isSuccessful = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                isSuccessful = task.isSuccessful

            }
        return isSuccessful
    }

    private fun loginUsuario(email: String, password: String): Boolean {
        val TAG = "loginUsuario"

        var isSuccessful = false

        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener(this) { task ->
            isSuccessful = task.isSuccessful
            if (!task.isSuccessful) {
                Log.w(TAG, "signInWithEmail:failure", task.exception)
            }
        }
        return isSuccessful
    }
}