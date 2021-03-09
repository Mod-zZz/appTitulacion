package com.example.app_titulacion.ui.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentConfigurationBinding
import com.example.app_titulacion.utils.Constants.MENU_ALERTAR
import com.example.app_titulacion.utils.Constants.MENU_CONTACTOS_DE_CONFIANZA
import com.example.app_titulacion.utils.Constants.MENU_LOGOUT
import com.example.app_titulacion.utils.Constants.MENU_MIS_ALERTAS
import com.example.app_titulacion.utils.Constants.MENU_MI_PERFIL
import com.example.app_titulacion.utils.Constants.MENU_ZONA_DE_RIESGO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment : Fragment(), ConfigurationAdapter.ConfigurationListener {

    private val TAG = "ConfigurationFragment"

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    private lateinit var configurationAdapter: ConfigurationAdapter

    private val configurationList = listOf(
        "Alertar",
        "Mis alertas",
        "Zonas de riesgo",
        "Editar usuario",
        "Contactos de Confianza",
        "Cerrar Sesión"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        configurationAdapter = ConfigurationAdapter(configurationList, this)

        binding.rvConfiguration.apply {
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
            adapter = configurationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickSelected(pos: Int) {
        when (pos) {
            MENU_ALERTAR -> {
                findNavController().navigate(R.id.action_nav_configuration_fragment_to_alertarFragment)

            }
            MENU_MIS_ALERTAS -> {
                // Todo Ir a fragmento mis alertas
            }
            MENU_ZONA_DE_RIESGO -> {
                // Todo Ir a fragmento zona de riesgo
            }
            MENU_MI_PERFIL -> {
                // Todo Ir a fragmento mi perfil
            }
            MENU_CONTACTOS_DE_CONFIANZA -> {
                // Todo Ir a fragmento contactos de confianza
            }
            MENU_LOGOUT -> {
                // Todo Funcion para salir de app
            }
        }
    }
}