package com.example.app_titulacion.ui.configuration.contactos_de_confianza

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.app_titulacion.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactosCFragment : Fragment() {

    private val contactosViewModel: ContactosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contactos_c, container, false)
    }


}