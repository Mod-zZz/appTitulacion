package com.example.app_titulacion.ui.configuration.alertar

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentAlertarBinding
import com.example.app_titulacion.utils.Constants
import com.example.app_titulacion.utils.Status
import com.example.app_titulacion.utils.showToast
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertarFragment : Fragment(R.layout.fragment_alertar) {

    private val TAG = "Aletar Fragment"

    private var _binding: FragmentAlertarBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val REQUEST_CODE_ASK_PERMISSION = 111

    private val notificacionViewModel: AlertarViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlertarBinding.inflate(inflater, container, false)

        return binding.root

        //TODO SE DEBE OBLIGAR AL USUARIO A ACTIVAR EL GPS
        //TODO OBTENER EL GPS DEL USUARIO PARA CALCULAR SU UBICACION Y TRAER LAS COMISARIAS CERCANAS

    }

    fun solicitarPermisos() {

        val gps = ActivityCompat.checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION
        )

        if (gps != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_CODE_ASK_PERMISSION)
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        solicitarPermisos()

        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

        val email: String = sharedPreferences.getString(Constants.APP_EMAIL, "").toString()

        with(binding) {

            acosoSexualButton.setOnClickListener() {
                notificacionViewModel.doSendNotificationAcosoSexual(email)
            }
            agrecionVerbalButton.setOnClickListener() {
                notificacionViewModel.doSendNotificationAgresionVerbal(email)
            }
            agrecionFisicaButton.setOnClickListener() {
                notificacionViewModel.doSendNotificationAgresionFisica(email)
            }
        }

        subscribe()

    }

    private fun subscribe() {

        notificacionViewModel.sendNotificationAcosoSexual.observe(viewLifecycleOwner) {
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

        notificacionViewModel.sendNotificationAgresionVerbal.observe(viewLifecycleOwner) {
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

        notificacionViewModel.sendNotificationAgresionFisica.observe(viewLifecycleOwner) {
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