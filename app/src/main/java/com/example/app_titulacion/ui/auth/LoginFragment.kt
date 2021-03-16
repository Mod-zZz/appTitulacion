package com.example.app_titulacion.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.app_titulacion.R
import com.example.app_titulacion.data.model.UserModel
import com.example.app_titulacion.databinding.FragmentLoginBinding
import com.example.app_titulacion.utils.Constants.APP_EMAIL
import com.example.app_titulacion.utils.Constants.APP_PREF
import com.example.app_titulacion.utils.Constants.APP_PROVIDER
import com.example.app_titulacion.utils.Constants.APP_SESSION
import com.example.app_titulacion.utils.Constants.APP_TOKEN
import com.example.app_titulacion.utils.Constants.BASIC
import com.example.app_titulacion.utils.Constants.FACEBOOK
import com.example.app_titulacion.utils.Constants.GMAIL
import com.example.app_titulacion.utils.Constants.GOOGLE_SIGN_IN
import com.example.app_titulacion.utils.Constants.TOKEN_FIELD
import com.example.app_titulacion.utils.Resource
import com.example.app_titulacion.utils.showAlert
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var callbackManager: CallbackManager

    private lateinit var sharedPreferences: SharedPreferences

    private var email: String? = null

    var MyToken = ""
    var provider = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session()

        sharedPreferences =
            this.requireActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)

        callbackManager = CallbackManager.Factory.create()

        recuperaToken()
        subscribe()

        with(binding) {

            emailEditText.setText(R.string.dev_email)
            passwordEditText.setText(R.string.dev_password)

            loginButton.setOnClickListener {
                email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                loginViewModel.doSignIn(email!!, password)
                provider = BASIC
            }

            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnGoogle.setOnClickListener {
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                //Cliente de autenticacion
                val googleClient = GoogleSignIn.getClient(requireContext(), googleConf)
                googleClient.signOut()
                startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
            }

            btnFacebook.setOnClickListener {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this@LoginFragment, listOf("email"))


                LoginManager.getInstance()
                    .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                        override fun onSuccess(result: LoginResult?) {
                            result?.let {
                                val token = it.accessToken

                                val credential =
                                    FacebookAuthProvider.getCredential(token.token)

                                FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnCompleteListener { authResult ->

                                        if (authResult.isSuccessful) {
                                            provider = FACEBOOK
                                            val userFb = authResult.result?.user?.email!!
                                            val user = UserModel(userFb, FACEBOOK, "", MyToken)
                                            registerViewModel.doCreateUser(user)
                                        } else {
                                            showAlert(
                                                getString(R.string.error_title),
                                                getString(R.string.error_message),
                                                getString(R.string.ok)
                                            )
                                        }
                                    }
                            }
                        }

                        override fun onCancel() {

                        }

                        override fun onError(error: FacebookException?) {
                            showAlert(
                                getString(R.string.error_title),
                                getString(R.string.error_message),
                                getString(R.string.ok)
                            )
                        }

                    })
            }

        }

    }

    private fun session() {

        sharedPreferences =
            this.requireActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)

        val isSessionActive = sharedPreferences.getBoolean(APP_SESSION, false)

        if (isSessionActive) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

    }

    //************************************* GOOGLE INICIO DE SESION *************************************
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //**************** AGREGADO PARA FACEBOOK ****************
        callbackManager.onActivityResult(requestCode, resultCode, data)
        //**************** FIN AGREGADO PARA FACEBOOK ****************

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                //LA CUENTA VALOR PUEDE TRAER UN ERROR - TRY
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { authResult ->

                            if (authResult.isSuccessful) {

                                val userGM = authResult.result?.user?.email!!
                                val user = UserModel(userGM, GMAIL, "", MyToken)
                                registerViewModel.doCreateUser(user)
                                provider = GMAIL

                            } else {
                                showAlert(
                                    getString(R.string.error_title),
                                    getString(R.string.error_message),
                                    getString(
                                        R.string.ok
                                    )
                                )
                            }
                        }
                }
            } catch (e: ApiException) {
                showAlert(
                    getString(R.string.error_title),
                    getString(R.string.error_message),
                    getString(
                        R.string.ok
                    )
                )
            }
        }
    }

    private fun recuperaToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let { mToken ->
                MyToken = mToken
            }
        }
    }


    private fun saveUserInSharedPreference(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString(APP_EMAIL, email)
        editor.putBoolean(APP_SESSION, true)
        editor.putString(APP_TOKEN, MyToken)
        editor.putString(APP_PROVIDER, provider)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribe() {
        loginViewModel.signIn.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "Loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "Success")
                    //                    val userInfo = it.data.additionalUserInfo
                    //                    val user = it.data.user
                    //                    val userCredential = it.data.credential
                    //                    val gson = Gson()
                    //                    val json = gson.toJson(it.data)
                    //                    Log.d(TAG, json)
                    saveUserInSharedPreference(email!!)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is Resource.Failure -> {
                    Log.d(TAG, "Failure")
                    Log.d(TAG, it.throwable.message!!)
                }
            }
        }

        registerViewModel.createUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "createUser Loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "createUser Success")
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is Resource.Failure -> {
                    Log.d(TAG, "createUser Failure")
                    Log.d(TAG, it.throwable.message!!)
                }
            }
        }
    }
}