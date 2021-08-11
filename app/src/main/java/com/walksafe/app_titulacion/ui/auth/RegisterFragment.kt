package com.walksafe.app_titulacion.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.walksafe.app_titulacion.R
import com.walksafe.app_titulacion.data.model.UserModel
import com.walksafe.app_titulacion.databinding.FragmentRegisterBinding
import com.walksafe.app_titulacion.utils.Constants
import com.walksafe.app_titulacion.utils.Resource
import com.walksafe.app_titulacion.utils.getNewToken
import dagger.hilt.android.AndroidEntryPoint
import com.walksafe.app_titulacion.utils.Constants.BASIC
import com.walksafe.app_titulacion.utils.showToast
import com.google.firebase.iid.FirebaseInstanceId
import kotlin.system.exitProcess


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val TAG = "RegisterFragment"

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val registerViewModel: RegisterViewModel by viewModels()

    var MyToken = ""

    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        session()

        with(binding) {
            btnSignUp.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val repitPassword = repitPasswordEditText.text.toString()

                if (password == repitPassword) {
                    user = UserModel(
                        email = email,
                        password = password,
                        token = getNewToken(requireContext())!!,
                        provider = BASIC
                    )
                    registerViewModel.doCreateUser(user!!)
                } else {
                    showToast(getString(R.string.msjPswIncorrecto))
                }
            }
        }
//        recuperaToken()
        subscribe()
    }

    private fun subscribe() {
        registerViewModel.createUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "createUser Loading")
                }
                is Resource.Success -> {
                    showToast(getString(R.string.msjCreacionInicio))
                    Log.d(TAG, "createUser Success")
                    registerViewModel.doSignUp(user!!)
                }
                is Resource.Failure -> {
                    showToast("Ocurrió un error durante la creación de su usuario, vuelva a intentarlo.")
                    Log.d(TAG, "createUser Failure ${it.throwable.message!!}")
                }
            }
        }

        registerViewModel.signUp.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "signUp Loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "signUp Success")
                    saveUserInSharedPreference(user!!.email)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is Resource.Failure -> {
                    Log.d(TAG, "signUp Failure ${it.throwable.message!!}")
                }
            }
        }
    }

    private fun saveUserInSharedPreference(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.APP_EMAIL, email)
        editor.putBoolean(Constants.APP_SESSION, true)
        editor.putString(Constants.APP_TOKEN, MyToken)
        editor.putString(Constants.APP_PROVIDER, BASIC)
        editor.apply()
    }

    private fun session() {

        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

        val isSessionActive = sharedPreferences.getBoolean(Constants.APP_SESSION, false)

        if (isSessionActive) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun recuperaToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            it.result?.token?.let { mToken ->
                MyToken = mToken
            }
        }
    }
}