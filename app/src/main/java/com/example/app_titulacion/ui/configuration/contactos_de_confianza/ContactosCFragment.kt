package com.example.app_titulacion.ui.configuration.contactos_de_confianza

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_titulacion.R
import com.example.app_titulacion.data.model.Contact
import com.example.app_titulacion.databinding.FragmentContactosCBinding
import com.example.app_titulacion.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.example.app_titulacion.utils.Constants.USER_COL
import com.example.app_titulacion.utils.Constants.COLEC_CONTACT
import com.example.app_titulacion.utils.Constants.APP_EMAIL
import com.example.app_titulacion.utils.Constants.APP_PREF
import com.example.app_titulacion.utils.Constants.EMAIL
import com.example.app_titulacion.utils.showToast


class ContactosCFragment : Fragment() {

    private val TAG = "ContactosCFragment"

    private var _binding: FragmentContactosCBinding? = null

    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactosCBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val email: String;
        sharedPreferences =
            this.requireActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(APP_EMAIL, "").toString()

        traerContactos(email)

        with(binding) {
            guardarButton.setOnClickListener {
                guardarContactos(email)
            }
        }
    }

    private fun guardarContactos(email: String) {


        // TODO GRABAR CONTACTOS DE CONFIANZA

        with(binding){
            db.collection(USER_COL).document(email)
        }

        with(binding) {

            val dataUpdate = hashMapOf<String, Any>(
                "" to correo1EditText.text.toString(),
                "" to correo2EditText.text.toString(),
                "" to correo3EditText.text.toString(),
                "" to correo4EditText.text.toString(),
                "" to correo5EditText.text.toString()
            )

            db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document()
                .update(dataUpdate)
                .addOnSuccessListener {
                    showToast(getString(R.string.mensajeCorrecto))
                }.addOnFailureListener { e ->
                    showToast(getString(R.string.mensajeError))
                }
        }
    }

    private fun traerContactos(email: String) {

        // TODO CARGAR CONTACTOS DE CONFIANZA

        with(binding) {
            db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    if (firebaseFirestoreException != null) {
                        Log.e(TAG, "firebaseFirestoreException", firebaseFirestoreException)
                    }

                    val list = mutableListOf<Contact>()
                    querySnapshot!!.forEach { queryDocumentSnapshot ->
                        list.add(queryDocumentSnapshot.toObject(Contact::class.java))
                    }

                    list.forEachIndexed { pos, item ->
                        when (pos) {
                            0 -> correo1EditText.setText(item.email)
                            1 -> correo2EditText.setText(item.email)
                            2 -> correo3EditText.setText(item.email)
                            3 -> correo4EditText.setText(item.email)
                            4 -> correo5EditText.setText(item.email)
                        }
                    }
                }
        }
    }


}