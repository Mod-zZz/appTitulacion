package com.example.app_titulacion.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentLoginBinding
import com.example.app_titulacion.databinding.FragmentRegisterBinding
import com.example.app_titulacion.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

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

        subscribe()

        with(binding) {
            loginButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                loginViewModel.executeSignIn(email, password)
            }

            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
                    Log.d(TAG, it.data.toString())
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