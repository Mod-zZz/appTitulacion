package com.walksafe.app_titulacion.ui.configuration.mi_perfil
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.walksafe.app_titulacion.R
import com.walksafe.app_titulacion.databinding.FragmentPerfilBinding
import com.walksafe.app_titulacion.utils.Constants
import com.walksafe.app_titulacion.utils.Constants.APP_EMAIL
import com.walksafe.app_titulacion.utils.Constants.CELL_FIELD
import com.walksafe.app_titulacion.utils.Constants.CITY_FIELD
import com.walksafe.app_titulacion.utils.Constants.DISTRICT_FIELD
import com.walksafe.app_titulacion.utils.Constants.APP_TOKEN
import com.walksafe.app_titulacion.utils.Constants.USER_COL
import com.walksafe.app_titulacion.utils.showToast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions


class PerfilFragment : Fragment() {

    private val TAG = "PerfilFragment"

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


        //*********TRAER EL EMAIL GUARADO EN EL SHARE PREFERNECES*********//
        val email: String;
        val token: String

        sharedPreferences =
            this.requireActivity().getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)

        email = sharedPreferences.getString(APP_EMAIL, "").toString()
        token = sharedPreferences.getString(APP_TOKEN, "").toString()

        //********* FIN TRAER EL EMAIL GUARADO EN EL SHARE PREFERNECES*********//

        traerEmailSession(email)
        recuperarInformacionUsaurio(email)
        actualizarInformacionUsuario(email, token)

    }

    private fun traerEmailSession(email: String) {
        binding.correoEditText.setText(email)
    }

    private fun recuperarInformacionUsaurio(email: String) {

        with(binding) {
            db.collection(USER_COL).document(email).get().addOnSuccessListener {
                celularEditText.setText(it.get(CELL_FIELD) as String?)
                ciudadEditText.setText(it.get(CITY_FIELD) as String?)
                distritoEditText.setText(it.get(DISTRICT_FIELD) as String?)
            }

        }
    }

    private fun actualizarInformacionUsuario(email: String, MyToken: String) {
        // TODO IMPLEMENTAR SPINNER EN CIUDAD / DISTRITO
        //https://www.youtube.com/watch?v=dp_ruQOP1sU

        with(binding) {

            actualizarButton.setOnClickListener {

                val dataUpdate = hashMapOf<String, Any>(
                    CELL_FIELD to celularEditText.text.toString(),
                    CITY_FIELD to ciudadEditText.text.toString(),
                    DISTRICT_FIELD to distritoEditText.text.toString()
                )

                db.collection(USER_COL).document(email).update(dataUpdate)
                    .addOnSuccessListener {
                        showToast(getString(R.string.mensajeCorrecto))
                    }.addOnFailureListener { e ->
                        showToast(getString(R.string.mensajeError))
                    }

            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}