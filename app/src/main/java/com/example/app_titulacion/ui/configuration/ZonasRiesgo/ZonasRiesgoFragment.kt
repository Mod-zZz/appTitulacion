package com.example.app_titulacion.ui.configuration.ZonasRiesgo

import android.Manifest
import android.content.pm.PackageManager
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.app_titulacion.databinding.FragmentZonasRiesgoBinding
import com.example.app_titulacion.utils.Constants.BASE_URL


class ZonasRiesgoFragment : Fragment() {

    val REQUEST_CODE_ASK_PERMISSION = 111

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

//        return inflater.inflate(R.layout.fragment_zonas_riesgo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        solicitarPermisos()

        with(binding) {

            webView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }

                override fun onReceivedSslError(
                    view: WebView,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
                    handler.proceed()
                }

            }


            val settings = webView.settings
            settings.javaScriptEnabled = true



            webView.loadUrl(BASE_URL)

        }
    }

    fun solicitarPermisos() {

        val gps = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (gps != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_ASK_PERMISSION
                )
            }
        }

    }


}