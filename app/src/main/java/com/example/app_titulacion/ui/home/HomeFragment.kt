package com.example.app_titulacion.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentHomeBinding
import com.example.app_titulacion.utils.Constants
import com.example.app_titulacion.utils.Status
import com.example.app_titulacion.utils.showToast
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val TAG = "Home Fragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val notificacionViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

        val email: String = sharedPreferences.getString(Constants.APP_EMAIL, "").toString()

        with(binding) {

            sosButton.setOnClickListener() {
                notificacionViewModel.doSendNotification(email)
            }

        }

        subscribe()
    }


    private fun subscribe() {
        notificacionViewModel.sendNotification.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "LOADING")
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "SUCCESS")
                    val gson = Gson()
                    Log.d(TAG, gson.toJson(it.data))
                    showToast(getString(R.string.msjCorrecto))
                }
                Status.ERROR -> {
                    Log.d(TAG, "ERROR ${it.message!!}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}