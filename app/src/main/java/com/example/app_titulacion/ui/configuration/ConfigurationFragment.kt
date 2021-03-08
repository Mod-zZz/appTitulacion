package com.example.app_titulacion.ui.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentConfigurationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {

    private val TAG = "ConfigurationFragment"

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    private lateinit var configurationAdapter: ConfigurationAdapter

    private val configurationList = listOf("Perfil", "Contactos", "Notificaciones", "Cerrar sesión")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        configurationAdapter = ConfigurationAdapter(configurationList)

        binding.rvConfiguration.apply {
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
            adapter = configurationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}