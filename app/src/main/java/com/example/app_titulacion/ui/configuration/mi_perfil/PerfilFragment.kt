package com.example.app_titulacion.ui.configuration.mi_perfil

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_titulacion.databinding.FragmentPerfilBinding
import com.example.app_titulacion.utils.Constants
import com.example.app_titulacion.utils.Constants.APP_EMAIL
import com.example.app_titulacion.utils.Constants.CAMPO_CELL
import com.example.app_titulacion.utils.Constants.CAMPO_CITY
import com.example.app_titulacion.utils.Constants.CAMPO_DISTRICT
import com.example.app_titulacion.utils.Constants.USER_COL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions


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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val email: String;
        //*********TRAER EL EMAIL GUARADO EN EL SHARE PREFERNECES*********//
        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(APP_EMAIL, "").toString()

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
            db.collection(USER_COL).document(email).get().addOnSuccessListener {
                celularEditText.setText(it.get(CAMPO_CELL) as String?)
                ciudadEditText.setText(it.get(CAMPO_CITY) as String?)
                distritoEditText.setText(it.get(CAMPO_DISTRICT) as String?)
            }

        }
    }

    private fun actualizarInformacionUsuario(email: String) {
        // TODO IMPLEMENTAR SPINNER EN CIUDAD / DISTRITO
        //https://www.youtube.com/watch?v=dp_ruQOP1sU

        with(binding) {

            actualizarButton.setOnClickListener {

                db.collection(USER_COL).document(email).set(
                    hashMapOf(
                        CAMPO_CELL to celularEditText.text.toString(),
                        CAMPO_CITY to ciudadEditText.text.toString(),
                        CAMPO_DISTRICT to distritoEditText.text.toString()
                    )
                )

            }


        }
    }




}