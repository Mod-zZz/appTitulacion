package com.example.app_titulacion.ui.configuration.alertar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentAlertarBinding
import com.example.app_titulacion.databinding.FragmentLoginBinding
import com.example.app_titulacion.databinding.FragmentRegisterBinding

class AlertarFragment : Fragment() {

    private var _binding: FragmentAlertarBinding? = null
    private val binding get() = _binding!!


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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


        }
    }



}