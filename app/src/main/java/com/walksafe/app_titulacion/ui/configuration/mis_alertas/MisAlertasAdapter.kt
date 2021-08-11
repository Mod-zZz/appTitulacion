package com.walksafe.app_titulacion.ui.configuration.mis_alertas

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.walksafe.app_titulacion.R
import com.walksafe.app_titulacion.data.model.Notificacion
import com.walksafe.app_titulacion.databinding.ItemNotificationBinding


import com.walksafe.app_titulacion.utils.BaseViewHolder
import com.walksafe.app_titulacion.utils.Constants.AGRESION_FISICA
import com.walksafe.app_titulacion.utils.Constants.AGRESION_SEXUAL
import com.walksafe.app_titulacion.utils.Constants.AGRESION_VERBAL
import com.walksafe.app_titulacion.utils.Constants.SOS
import com.walksafe.app_titulacion.utils.showToast
import java.text.SimpleDateFormat
import java.util.*
import java.time.*

class MisAlertasAdapter(
    private val listener: MisAlertasListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface MisAlertasListener {
        fun onItemClickSelected(latitud: String, longitud: String)
    }

    private val list: MutableList<Notificacion> = mutableListOf()

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val vh =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MisAlertasViewHolder(vh)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MisAlertasViewHolder -> holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int = list.count()

    inner class MisAlertasViewHolder(private val binding: ItemNotificationBinding) :
        BaseViewHolder<Notificacion>(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun bind(item: Notificacion) = with(binding) {
            tvDe.text = "De: " + item.de.toString()
            tvPara.text = "Para: " + item.para.toString()

            val fecha = item.fecha.toString()
            val fechaF = fecha.substring(0, 10)

            tvFecha.text = "Fecha: " + dateFormatter(fechaF.toLong())
            tvIncedente.text = "Incidente: " + item.incidente.toString()
            tvOrigen.text = "Notificación: " + if (item.origen!!) "Saliente" else "Entrante"
            tvResultado.text = "Estado: " + if (item.resultado!!) "Exitoso" else "Fallado"

            tvLatitud.text = item.latitud
            tvLongitud.text = item.longitud

            root.setOnClickListener {
                if (item.latitud == "Latitud" && item.longitud == "Longitud") {
                    Toast.makeText(context, R.string.msjNoLatiLong, Toast.LENGTH_SHORT).show()
                } else {
                    listener.onItemClickSelected(item.latitud, item.longitud)
                }

            }

            rootLayout.background.setColorFilter(
                if (item.incidenciaId == AGRESION_SEXUAL && item.origen == false) {
                    Color.parseColor("#C63637")
                } else if (item.incidenciaId == AGRESION_FISICA && item.origen == false) {
                    Color.parseColor("#378FAE")
                } else if (item.incidenciaId == AGRESION_VERBAL && item.origen == false) {
                    Color.parseColor("#F7BD56")
                } else if (item.incidenciaId == SOS && item.origen == false) {
                    Color.parseColor("#C63637")
                } else {
                    Color.parseColor("#EAF2E3")
                }
                , PorterDuff.Mode.SRC_ATOP
            )


//            rootLayout.setBackgroundColor(
//                if (item.incidenciaId == AGRESION_SEXUAL && item.origen == false) {
//                    ContextCompat.getColor(context, R.color.red)
//                } else if (item.incidenciaId == AGRESION_FISICA && item.origen == false) {
//                    ContextCompat.getColor(context, R.color.yellow)
//                } else if (item.incidenciaId == AGRESION_VERBAL && item.origen == false) {
//                    ContextCompat.getColor(context, R.color.orange)
//                } else if (item.incidenciaId == SOS && item.origen == false) {
//                    ContextCompat.getColor(context, R.color.red)
//                } else {
//                    ContextCompat.getColor(context, R.color.white_fond)
//                }
//            )

            tvClic.text = "Ver la ruta en google Maps."

        }

    }

    fun dateFormatter(epoch: Long): String {
        // epoch = 1557954848
        val date = Date(epoch * 1000L)
//        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSSSS")
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return sdf.format(date)

    }

    fun updateList(list: List<Notificacion>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}