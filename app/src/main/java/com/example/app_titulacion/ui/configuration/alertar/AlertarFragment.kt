package com.example.app_titulacion.ui.configuration.alertar

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.core.app.ActivityCompat
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentAlertarBinding


class AlertarFragment : Fragment() {

    private var _binding: FragmentAlertarBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE_ASK_PERMISSION = 111


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        with(binding) {


        }

    }


}