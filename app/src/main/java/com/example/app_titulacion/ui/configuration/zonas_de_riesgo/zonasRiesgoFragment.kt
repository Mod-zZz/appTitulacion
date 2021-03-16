package com.example.app_titulacion.ui.configuration.zonas_de_riesgo

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.app_titulacion.databinding.FragmentZonasRiesgoBinding
import com.example.app_titulacion.utils.Constants.BASE_URL


class zonasRiesgoFragment : Fragment() {

    private var _binding: FragmentZonasRiesgoBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentZonasRiesgoBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        swipeRefresh.setOnRefreshListener{

//        }


        with(binding)
        {


            webView.webChromeClient = object : WebChromeClient() {

            }

            webView.webViewClient = object : WebViewClient() {

            }

            //Activar JavaScrip
            val settings = webView.settings
            settings.javaScriptEnabled = true

            webView.loadUrl(BASE_URL)
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
           // onBackPressed()

        }

    }

    fun onBackPressed(){
        with(binding) {

            if (webView.canGoBack()){
                webView.goBack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}