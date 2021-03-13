package com.example.app_titulacion.ui.configuration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentConfigurationBinding
import com.example.app_titulacion.utils.Constants
import com.example.app_titulacion.utils.Constants.APP_PREF
import com.example.app_titulacion.utils.Constants.FACEBOOK
import com.example.app_titulacion.utils.Constants.GMAIL
import com.example.app_titulacion.utils.Constants.GOOGLE_SIGN_IN
import com.example.app_titulacion.utils.Constants.MENU_ALERTAR
import com.example.app_titulacion.utils.Constants.MENU_CONTACTOS_DE_CONFIANZA
import com.example.app_titulacion.utils.Constants.MENU_LOGOUT
import com.example.app_titulacion.utils.Constants.MENU_MIS_ALERTAS
import com.example.app_titulacion.utils.Constants.MENU_MI_PERFIL
import com.example.app_titulacion.utils.Constants.MENU_ZONA_DE_RIESGO
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment : Fragment(), ConfigurationAdapter.ConfigurationListener {

    private val TAG = "ConfigurationFragment"

    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

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
                findNavController().navigate(R.id.action_nav_configuration_fragment_to_perfilFragment)
            }
            MENU_CONTACTOS_DE_CONFIANZA -> {
                findNavController().navigate(R.id.action_nav_configuration_fragment_to_contactosCFragment)
            }
            MENU_LOGOUT -> {

                //RECUPERANDO EL PROVEEDOR
                sharedPreferences =
                    this.requireActivity()
                        .getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

                val provider = sharedPreferences.getString(Constants.APP_PROVIDER, "").toString()

                //BORRAR DATOS DE SESION GUARDADOS
                val prefs =
                    this.activity?.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)?.edit()
                prefs?.clear()
                prefs?.apply()

                //CERRAR SESION FACEBOOK
                if (provider == FACEBOOK) {
                    LoginManager.getInstance().logOut()
                }

                //CERRAR SESSION GOOGLE
                if (provider == GMAIL) {
                    FirebaseAuth.getInstance().signOut()
                }

                findNavController().navigate(R.id.action_nav_configuration_fragment_to_nav_auth)
                //findNavController().navigate(ConfigurationFragmentDirections.actionNavConfigurationFragmentToNavAuth())

            }
        }
    }
}