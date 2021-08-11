package com.walksafe.app_titulacion.ui.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walksafe.app_titulacion.databinding.ItemConfigurationBinding
import com.walksafe.app_titulacion.utils.BaseViewHolderWithIndex

class ConfigurationAdapter(
    private val list: List<String>,
    private val listener: ConfigurationListener
) : RecyclerView.Adapter<BaseViewHolderWithIndex<*>>() {

    interface ConfigurationListener {
        fun onItemClickSelected(pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithIndex<*> {
        val vh =
            ItemConfigurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConfigurationViewHolder(vh)
    }

    override fun onBindViewHolder(holder: BaseViewHolderWithIndex<*>, position: Int) {
        when (holder) {
            is ConfigurationViewHolder -> holder.bind(list[position], position)
        }
    }

    override fun getItemCount(): Int = list.count()

    inner class ConfigurationViewHolder(private val binding: ItemConfigurationBinding) :
        BaseViewHolderWithIndex<String>(binding.root) {
        override fun bind(item: String, i: Int) = with(binding) {
            tvDescription.text = item
            root.setOnClickListener { 
                listener.onItemClickSelected(i)
            }
        }
    }
}