package com.example.app_titulacion.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.app_titulacion.R
import com.example.app_titulacion.databinding.FragmentHomeBinding
import com.example.app_titulacion.utils.Constants
import com.example.app_titulacion.utils.Status
import com.example.app_titulacion.utils.showToast
import com.google.android.gms.location.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val TAG = "Home Fragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    val REQUEST_PERMISSION_LOCATION = 111

    private lateinit var sharedPreferences: SharedPreferences

    private val notificacionViewModel: HomeViewModel by viewModels()


    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

        val email: String = sharedPreferences.getString(Constants.APP_EMAIL, "").toString()


        mLocationRequest = LocationRequest()

        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        if (checkPermissionForLocation(this.requireContext())) {
            startLocationUpdates()
        }

        var latitud: String = ""
        var longitud: String = ""


        with(binding) {
            sosButton.setOnClickListener() {
                latitud = tvLatitud.text.toString()
                longitud = tvLatitud.text.toString()
                notificacionViewModel.doSendNotification(email,latitud,longitud)
            }
        }

        subscribe()
    }

    private fun subscribe() {
        notificacionViewModel.sendNotification.observe(viewLifecycleOwner) {
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

    // region Gps
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Agregar el método startlocationUpdate () más tarde en lugar de Toast
                Toast.makeText(this.requireContext(), "Permiso concedido.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Mostrar la solicitud de permiso
                ActivityCompat.requestPermissions(
                    this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this.requireContext())
        builder.setMessage("Tu GPS parece estar desactivado, ¿Quieres activarlo?")
            .setCancelable(false)
            .setPositiveButton("Sí") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11
                )
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
//                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()

    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        //Se ha determinado la nueva ubicación
        with(binding) {

            mLastLocation = location

            if (mLastLocation == null){

            }else {
                tvLatitud.text = "" + mLastLocation.latitude
                tvLongitud.text = "" + mLastLocation.longitude
            }





//        val date: Date = Calendar.getInstance().time
//        val sdf = SimpleDateFormat("hh:mm:ss a")
//        tvUbicacion.text = "Updated at : " + sdf.format(date) + " LATITUDE : " + mLastLocation.latitude + " LONGITUDE : " + mLastLocation.longitude
//            tvUbicacion.text =
//                "Latitud : " + mLastLocation.latitude + " Longitud : " + mLastLocation.longitude

        }
    }

    private fun startLocationUpdates() {

        //Cree la solicitud de ubicación para comenzar a recibir actualizaciones

//        mLocationRequest = LocationRequest()
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Crear objeto LocationSettingsRequest usando la solicitud de ubicación

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this.requireActivity())
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        // El nuevo SDK de API de Google v11 usa getFusedLocationProviderClient (this)
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

//endregion

    override fun onDestroyView() {
        stoplocationUpdates()
        super.onDestroyView()
        _binding = null
    }

}