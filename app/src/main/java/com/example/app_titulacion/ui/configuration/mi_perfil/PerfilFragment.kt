package com.example.app_titulacion.ui.configuration.mi_perfil

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_titulacion.R
import com.example.app_titulacion.data.model.UserModel
import com.example.app_titulacion.databinding.FragmentPerfilBinding
import com.example.app_titulacion.databinding.FragmentRegisterBinding
import com.example.app_titulacion.utils.Constants
import com.facebook.share.Share
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp

import com.google.firebase.functions.FirebaseFunctions
import com.google.firestore.v1.DocumentTransform
import com.google.type.Date

class PerfilFragment : Fragment() {

    //Traemos la funcion para traer la fecha del servidor
    private lateinit var functions: FirebaseFunctions

    private lateinit var sharedPreferences: SharedPreferences

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val email: String;
        //*********TRAER EL EMAIL GUARADO EN EL SHARE PREFERNECES*********//
        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        email = sharedPreferences.getString("APP_EMAIL", "").toString()

        //********* FIN TRAER EL EMAIL GUARADO EN EL SHARE PREFERNECES*********//
        traerEmailSession(email)
        recuperarInformacionUsaurio(email)
        actualizarInformacionUsuario(binding.correoEditText.text.toString())

    }

    private fun traerEmailSession(email: String) {
        binding.correoEditText.setText(email)
    }

    private fun recuperarInformacionUsaurio(email: String) {

        with(binding) {
            db.collection("users").document(email).get().addOnSuccessListener {
                celularEditText.setText(it.get("cell") as String?)
                ciudadEditText.setText(it.get("city") as String?)
                distritoEditText.setText(it.get("distric") as String?)
            }

        }
    }

    private fun actualizarInformacionUsuario(email: String) {
        // TODO IMPLEMENTAR SPINNER EN CIUDAD / DISTRITO
        //https://www.youtube.com/watch?v=dp_ruQOP1sU

        with(binding) {

            actualizarButton.setOnClickListener {

                db.collection("users").document(email).set(
                    hashMapOf(
                        "cell" to celularEditText.text.toString(),
                        "city" to ciudadEditText.text.toString(),
                        "distric" to distritoEditText.text.toString()
                    )
                )

            }


        }
    }




}