package com.example.app_titulacion.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.app_titulacion.R
import com.example.app_titulacion.data.model.Contact
import com.example.app_titulacion.databinding.FragmentHomeBinding
import com.example.app_titulacion.utils.*
import com.example.app_titulacion.utils.Constants.BASE_URL
import com.example.app_titulacion.utils.Constants.COLEC_CONTACT
import com.example.app_titulacion.utils.Constants.USER_COL
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.jvm.Throws


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val TAG = "Home Fragment"

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

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

        with(binding) {
            sosButton.setOnClickListener() {

                notificacionViewModel.doSendNotification(
                    email,
                    tvLatitud.text.toString(),
                    tvLongitud.text.toString()
                )

                if (checkPermissionsSms()) {
                    listadeContactosConfianza(
                        email,
                        tvLatitud.text.toString(),
                        tvLongitud.text.toString()
                    )
                }
            }
            btnZonasRiesgo.setOnClickListener() {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(BASE_URL))
                startActivity(intent)
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

        //SOLO GPS
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()//Permiso concedido
            }
        }

        //SOLO ENVIAR SMS

        if (requestCode == 777) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Todo_ bien SMS
            } else {
                Toast.makeText(this.requireContext(), "Permisos rechazados.", Toast.LENGTH_SHORT)
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

            if (mLastLocation.latitude.toString() == "") {

            } else {
                tvLatitud.text = "" + mLastLocation.latitude
                tvLongitud.text = "" + mLastLocation.longitude
            }

//        val date: Date = Calendar.getInstance().time
//        val sdf = SimpleDateFormat("hh:mm:ss a")
//        tvUbicacion.text = "Updated at : " + sdf.format(date) + " LATITUDE : " + mLastLocation.latitude + " LONGITUDE : " + mLastLocation.longitude

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

    // region Sms

    private fun listadeContactosConfianza(email: String, latitud: String, longitud: String) {

        db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e(TAG, "firebaseFirestoreException", firebaseFirestoreException)
                }

                val list = mutableListOf<Contact>()
                querySnapshot!!.forEach { queryDocumentSnapshot ->
                    list.add(queryDocumentSnapshot.toObject(Contact::class.java))
                }

                //solo se puede enviar con 160 caracteres como maximo
                val urlMaps =
                    "WalkSafe: $email https://www.google.es/maps?q=$latitud,$longitud"

                for (sendCell in list) {
                    sendSMS(urlMaps, sendCell.celular.toString())
                }
                showToast(getString(R.string.msjCorrectoSms))

            }


    }

    private fun sendSMS(
        smsMsj: String,
        cel: String
    ) {

        if (cel.count() > 0) {
            val sms: SmsManager = SmsManager.getDefault()
            sms.sendTextMessage(
                cel,
                null,
                smsMsj,
                null,
                null
            )
        }
    }

    private fun checkPermissionsSms(): Boolean {

        var r: Boolean = false

        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permiso no aceptado por el momento
            requestSmsPermission()

        } else {
            r = true
            return r
        }
        return r
    }

    private fun requestSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(),
                Manifest.permission.SEND_SMS
            )
        ) {
            //El usuaario ya ha rechazado los permisos
            Toast.makeText(this.requireContext(), "Permisos rechazados.", Toast.LENGTH_SHORT).show()
        } else {
            //pedir permiso
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.SEND_SMS),
                777
            )
        }
    }


    //endregion

    override fun onDestroyView() {
        stoplocationUpdates()
        _binding = null
        super.onDestroyView()
    }

}