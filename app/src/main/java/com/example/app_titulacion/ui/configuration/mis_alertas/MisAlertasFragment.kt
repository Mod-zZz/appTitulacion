package com.example.app_titulacion.ui.configuration.mis_alertas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_titulacion.databinding.FragmentConfigurationAlertasBinding
import com.example.app_titulacion.ui.configuration.ConfigurationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MisAlertasFragment : Fragment(), ConfigurationAdapter.ConfigurationListener {

    private val TAG = "MisAlertasFragment"

    private var _binding: FragmentConfigurationAlertasBinding? = null
    private val binding get() = _binding!!

    private lateinit var configurationAdapter: ConfigurationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val configurationListAlertas = listOf(
        "Registro 1",
        "Registro 2",
        "Registro 3",
        "Registro 4",
        "Registro 5",
        "Registro 6"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurationAlertasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configurationAdapter = ConfigurationAdapter(configurationListAlertas, this)

        binding.rvConfigurationAlertas.apply {
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

        }
    }


}