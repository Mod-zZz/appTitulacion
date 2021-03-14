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

                if (correo1EditText.text.toString() != "") {
                    actualizarTokenCorreo_1(correo1EditText.text.toString(), email)
                }
                if (correo1EditText.text.toString() != "") {
                    actualizarTokenCorreo_2(correo2EditText.text.toString(), email)
                }
                if (correo1EditText.text.toString() != "") {
                    actualizarTokenCorreo_3(correo3EditText.text.toString(), email)
                }
                if (correo1EditText.text.toString() != "") {
                    actualizarTokenCorreo_4(correo4EditText.text.toString(), email)
                }
                if (correo1EditText.text.toString() != "") {
                    actualizarTokenCorreo_5(correo5EditText.text.toString(), email)
                }

            }

            Email1Button.setOnClickListener {
                if (correo1EditText.text.toString() != "") {
                    actualizarTokenCorreo_1(correo1EditText.text.toString(), email)
                }

            }

            Email2Button.setOnClickListener {
                if (correo2EditText.text.toString() != "") {
                    actualizarTokenCorreo_2(correo2EditText.text.toString(), email)
                }

            }

            Email3Button.setOnClickListener {
                if (correo3EditText.text.toString() != "") {
                    actualizarTokenCorreo_3(correo3EditText.text.toString(), email)
                }

            }

            Email4Button.setOnClickListener {
                if (correo4EditText.text.toString() != "") {
                    actualizarTokenCorreo_4(correo4EditText.text.toString(), email)
                }

            }

            Email5Button.setOnClickListener {

                if (correo5EditText.text.toString() != "") {
                    actualizarTokenCorreo_5(correo5EditText.text.toString(), email)
                }

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
                showToast(R.string.mensajeCorrecto.toString())
                showToast("Sí guardo por primera vez ó actualizó el contacto de confianza no olvide probar")
            }.addOnFailureListener { e ->
                //Log.e(TAG, e.message!!)
                showToast(R.string.mensajeError.toString())
            }

        }

    }

    private fun actualizarTokenCorreo_1(emailValue: String, email: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val tokenC = db.collection(USER_COL).document(emailValue).get().await().toString()

            if (tokenC != "") {
                val dataUpdate = hashMapOf<String, Any>(
                    TOKEN_FIELD to tokenC
                )

                db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                    .document(EMAIL2_FIELD)
                    .update(dataUpdate).addOnSuccessListener {
                        showToast(R.string.mensajeCorrecto.toString())
                    }.addOnFailureListener { e ->
                        showToast(R.string.mensajeError.toString())
                    }
            }

        }

    private fun actualizarTokenCorreo_2(emailValue: String, email: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val tokenC = db.collection(USER_COL).document(emailValue).get().await().toString()

            if (tokenC != "") {
                val dataUpdate = hashMapOf<String, Any>(
                    TOKEN_FIELD to tokenC
                )

                db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                    .document(EMAIL2_FIELD)
                    .update(dataUpdate).addOnSuccessListener {
                        showToast(R.string.mensajeCorrecto.toString())
                    }.addOnFailureListener { e ->
                        showToast(R.string.mensajeError.toString())
                    }
            }


        }


    private fun actualizarTokenCorreo_3(emailValue: String, email: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val tokenC = db.collection(USER_COL).document(emailValue).get().await().toString()

            if (tokenC != "") {

                val dataUpdate = hashMapOf<String, Any>(
                    TOKEN_FIELD to tokenC
                )

                db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                    .document(EMAIL3_FIELD)
                    .update(dataUpdate).addOnSuccessListener {
                        showToast(R.string.mensajeCorrecto.toString())
                    }.addOnFailureListener { e ->
                        showToast(R.string.mensajeError.toString())
                    }
            }
        }


    private fun actualizarTokenCorreo_4(emailValue: String, email: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val tokenC = db.collection(USER_COL).document(emailValue).get().await().toString()

            if (tokenC != "") {

                val dataUpdate = hashMapOf<String, Any>(
                    TOKEN_FIELD to tokenC
                )

                db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                    .document(EMAIL4_FIELD)
                    .update(dataUpdate).addOnSuccessListener {
                        showToast(R.string.mensajeCorrecto.toString())
                    }.addOnFailureListener { e ->
                        Log.e(TAG, e.message!!)
                        showToast(R.string.mensajeError.toString())
                    }
            }


        }

    private fun actualizarTokenCorreo_5(emailValue: String, email: String) =
        CoroutineScope(Dispatchers.IO).launch {

            val tokenC = db.collection(USER_COL).document(emailValue).get().await().toString()

            if (tokenC != "") {
                val dataUpdate = hashMapOf<String, Any>(
                    TOKEN_FIELD to tokenC
                )

                db.collection(USER_COL).document(email).collection(COLEC_CONTACT)
                    .document(EMAIL5_FIELD)
                    .update(dataUpdate).addOnSuccessListener {
                        showToast(R.string.mensajeCorrecto.toString())
                    }.addOnFailureListener { e ->
                        Log.e(TAG, e.message!!)
                        showToast(R.string.mensajeError.toString())
                    }
            }
        }

    private fun actualizarTokenCorreo_example(emailValue: String, email: String) {
        var tokenC: String

        db.collection(USER_COL).document(emailValue).get().addOnSuccessListener {
            tokenC = ((it.get(TOKEN_FIELD) as String?).toString())

            val dataUpdate = hashMapOf<String, Any>(
                TOKEN_FIELD to tokenC
            )

            db.collection(USER_COL).document(email).collection(COLEC_CONTACT).document(EMAIL5_FIELD)
                .update(dataUpdate).addOnSuccessListener {
                    showToast(R.string.mensajeCorrecto.toString())
                }.addOnFailureListener { e ->
                    Log.e(TAG, e.message!!)
                    showToast(R.string.mensajeError.toString())
                }
        }
    }


    private fun getTokenFromTrustedContact(email: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val email1Ref = db.collection(USER_COL).document(email).get().await()
            val user = email1Ref.toObject(UserModel::class.java)

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showToast(R.string.mensajeError.toString())
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