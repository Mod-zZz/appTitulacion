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
import com.example.app_titulacion.data.model.UserModel
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
import com.example.app_titulacion.utils.Constants.TOKEN_FIELD
import com.example.app_titulacion.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


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
                actualizarTokens(
                    email,
                    email1Value,
                    email2Value,
                    email3Value,
                    email4Value,
                    email5Value
                )
                showToast(R.string.mensajeCorrecto.toString())
            }.addOnFailureListener { e ->
                Log.e(TAG, e.message!!)
                showToast(R.string.mensajeError.toString())
            }

        }

    }

    private fun actualizarTokens(
        email: String,
        email1Value: String,
        email2Value: String,
        email3Value: String,
        email4Value: String,
        email5Value: String
    ) {

        //TODO TRAER EL TOKEN DEL CORREO
        var tokenCorreo1: String = ""
        var tokenCorreo2: String = ""
        var tokenCorreo3: String = ""
        var tokenCorreo4: String = ""
        var tokenCorreo5: String = ""

        //region OBTENER EL TOKEN DE CADA CORREO REGISTRADO
        if (email1Value != "") {
//            val email1Ref = db.collection(USER_COL).document(email1Value).get().await()

//            db.collection(USER_COL).document(email1Value).get().addOnSuccessListener {
//                tokenCorreo1 = ((it.get(TOKEN_FIELD) as String?).toString())
//                Log.d(TAG, "tokenCorreo1 " + tokenCorreo1)
//            }
        }

        if (email2Value != "") {
            db.collection(USER_COL).document(email2Value).get().addOnSuccessListener {
                tokenCorreo2 = ((it.get(TOKEN_FIELD) as String?).toString())
                Log.d(TAG, "tokenCorreo2 " + tokenCorreo2)
            }
        }

        if (email3Value != "") {
            db.collection(USER_COL).document(email3Value).get().addOnSuccessListener {
                tokenCorreo3 = ((it.get(TOKEN_FIELD) as String?).toString())
                Log.d(TAG, "tokenCorreo3 " + tokenCorreo3)
            }
        }

        if (email4Value != "") {
            db.collection(USER_COL).document(email4Value).get().addOnSuccessListener {
                tokenCorreo4 = ((it.get(TOKEN_FIELD) as String?).toString())
                Log.d(TAG, "tokenCorreo4 " + tokenCorreo4)
            }
        }

        if (email5Value != "") {
            db.collection(USER_COL).document(email5Value).get().addOnSuccessListener {
                tokenCorreo5 = ((it.get(TOKEN_FIELD) as String?).toString())
                Log.d(TAG, "tokenCorreo5 " + tokenCorreo5)
            }
        }
        //endregion


        //region PASANDO A LA CLAVE SU VALOR CORRESPONDIENTE
        val dataUpdate1 = hashMapOf<String, Any>(
            TOKEN_FIELD to tokenCorreo1
        )

        val dataUpdate2 = hashMapOf<String, Any>(
            TOKEN_FIELD to tokenCorreo2
        )

        val dataUpdate3 = hashMapOf<String, Any>(
            TOKEN_FIELD to tokenCorreo3
        )

        val dataUpdate4 = hashMapOf<String, Any>(
            TOKEN_FIELD to tokenCorreo4
        )

        val dataUpdate5 = hashMapOf<String, Any>(
            TOKEN_FIELD to tokenCorreo5
        )
        //endregion

        //region ACTUALIZANDO LOS DATOS QUE CORRESPONDAN

        db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document(EMAIL1_FIELD)
            .update(dataUpdate1)

        db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document(EMAIL2_FIELD)
            .update(dataUpdate2)

        db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document(EMAIL3_FIELD)
            .update(dataUpdate3)

        db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document(EMAIL4_FIELD)
            .update(dataUpdate4)

        db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document(EMAIL5_FIELD)
            .update(dataUpdate5)

        //endregion


    }

    private fun getTokenFromTrustedContact(email: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val email1Ref = db.collection(USER_COL).document(email).get().await()
            val user = email1Ref.toObject(UserModel::class.java)

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showToast("Eres la cagada")
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