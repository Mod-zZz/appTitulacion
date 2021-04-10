package com.example.app_titulacion.ui.configuration.contactos_de_confianza

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.app_titulacion.R
import com.example.app_titulacion.data.model.Contact
import com.example.app_titulacion.databinding.FragmentContactosCBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.example.app_titulacion.utils.Constants.USER_COL
import com.example.app_titulacion.utils.Constants.COLEC_CONTACT
import com.example.app_titulacion.utils.Constants.APP_EMAIL
import com.example.app_titulacion.utils.Constants.APP_PREF
import com.example.app_titulacion.utils.Constants.EMAIL1_FIELD
import com.example.app_titulacion.utils.Constants.EMAIL2_FIELD
import com.example.app_titulacion.utils.Constants.EMAIL3_FIELD
import com.example.app_titulacion.utils.Constants.EMAIL4_FIELD
import com.example.app_titulacion.utils.Constants.EMAIL5_FIELD
import com.example.app_titulacion.utils.Status
import com.example.app_titulacion.utils.showToast
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactosCFragment : Fragment() {

    private val TAG = "ContactosCFragment"

    private var _binding: FragmentContactosCBinding? = null

    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    private lateinit var sharedPreferences: SharedPreferences

    private val contactosViewModel: ContactosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactosCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences =
            this.requireActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
        val email: String = sharedPreferences.getString(APP_EMAIL, "").toString()

        traerContactos(email)

        with(binding) {
            guardarButton.setOnClickListener {
                guardarContactos(email)
            }
        }

        subscribe()
    }

    private fun guardarContactos(email: String) {

        with(binding) {
            val email1Value = correo1EditText.text.toString()
            val email2Value = correo2EditText.text.toString()
            val email3Value = correo3EditText.text.toString()
            val email4Value = correo4EditText.text.toString()
            val email5Value = correo5EditText.text.toString()

            val email1Ref = db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                .document(EMAIL1_FIELD)
            val email1Contact = Contact(email = email1Value)

            val email2Ref = db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                .document(EMAIL2_FIELD)
            val email2Contact = Contact(email = email2Value)

            val email3Ref = db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                .document(EMAIL3_FIELD)
            val email3Contact = Contact(email = email3Value)

            val email4Ref = db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                .document(EMAIL4_FIELD)
            val email4Contact = Contact(email = email4Value)

            val email5Ref = db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                .document(EMAIL5_FIELD)
            val email5Contact = Contact(email = email5Value)


            db.runBatch { batch ->
                batch.set(email1Ref, email1Contact)
                batch.set(email2Ref, email2Contact)
                batch.set(email3Ref, email3Contact)
                batch.set(email4Ref, email4Contact)
                batch.set(email5Ref, email5Contact)
            }.addOnSuccessListener {
                contactosViewModel.doContactUpdateToken(email)
            }.addOnFailureListener { e ->
                //Log.e(TAG, e.message!!)
                showToast(getString(R.string.mensajeError))
            }

        }

    }

    private fun subscribe() {
        contactosViewModel.contactoUpdateToken.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "LOADING")
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "SUCCESS")
                    val gson = Gson()
                    Log.d(TAG, gson.toJson(it.data))
                    showToast(getString(R.string.mensajeCorrecto))
                }
                Status.ERROR -> {
                    Log.d(TAG, "ERROR ${it.message!!}")
                }
            }
        }
    }


    private fun traerContactos(email: String) {

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