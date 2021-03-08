package com.example.app_titulacion.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentLoginBinding
import com.example.app_titulacion.utils.Constants.GOOGLE_SIGN_IN
import com.example.app_titulacion.utils.Resource
import com.example.app_titulacion.utils.showAlert
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var callbackManager: CallbackManager

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

        callbackManager = CallbackManager.Factory.create()

        subscribe()

        with(binding) {

            emailEditText.setText(R.string.dev_email)
            passwordEditText.setText(R.string.dev_password)

            loginButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                loginViewModel.doSignIn(email, password)
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
//                                            showHome(
//                                                it.result?.user?.email ?: "",
//                                                ProviterType.FACEBOOK
//                                            )
                                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribe() {
        loginViewModel.signIn.observe(viewLifecycleOwner, {
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
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is Resource.Failure -> {
                    Log.d(TAG, "Failure")
                    Log.d(TAG, it.throwable.message!!)
                }
            }
        })
    }
}