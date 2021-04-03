package com.example.app_titulacion.ui.configuration.mis_alertas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_titulacion.data.model.Notificacion
import com.example.app_titulacion.databinding.ItemNotificationBinding
import com.example.app_titulacion.utils.BaseViewHolder

class MisAlertasAdapter() :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

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
        override fun bind(item: Notificacion) = with(binding) {
            tvDe.text = item.de
            tvFecha.text = item.fecha
            tvIncedente.text = item.incidente
            tvOrigen.text = if (item.origen!!) "Saliente" else "Entrante"
            tvPara.text = item.para
            tvResultado.text = if (item.resultado!!) "Exito" else "Fallo"
        }

    }

    fun updateList(list: List<Notificacion>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}