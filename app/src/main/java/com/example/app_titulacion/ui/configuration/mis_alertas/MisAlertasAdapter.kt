package com.example.app_titulacion.ui.configuration.mis_alertas

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.app_titulacion.data.model.Notificacion
import com.example.app_titulacion.databinding.ItemNotificationBinding


import com.example.app_titulacion.utils.BaseViewHolder
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val vh =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MisAlertasViewHolder(vh)
    }

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
                listener.onItemClickSelected(item.latitud,item.longitud)
            }

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