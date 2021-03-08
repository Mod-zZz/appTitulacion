package com.example.app_titulacion.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}

abstract class BaseViewHolderWithIndex<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T, i: Int)
}