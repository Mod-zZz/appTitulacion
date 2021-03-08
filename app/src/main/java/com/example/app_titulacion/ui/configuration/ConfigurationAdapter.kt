package com.example.app_titulacion.ui.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_titulacion.databinding.ItemConfigurationBinding
import com.example.app_titulacion.utils.BaseViewHolder

class ConfigurationAdapter(private val list: List<String>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val vh =
            ItemConfigurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConfigurationViewHolder(vh)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ConfigurationViewHolder -> holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int = list.count()

    inner class ConfigurationViewHolder(private val binding: ItemConfigurationBinding) :
        BaseViewHolder<String>(binding.root) {
        override fun bind(item: String) = with(binding) {
            tvDescription.text = item
        }
    }
}