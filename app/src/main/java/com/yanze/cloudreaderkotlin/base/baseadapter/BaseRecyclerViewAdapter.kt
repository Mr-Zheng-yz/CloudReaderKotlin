package com.tuju.jetpackfirstdemo.base.baseadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewHolder<T>>() {

    protected val data = ArrayList<T>()

    protected fun getView(parent: ViewGroup, layoutId: Int) = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>, position: Int) {
        holder.onBaseBindViewHolder(data[position], position)
    }

    fun getData(): List<T> = data

    fun add(bean: T) {
        data.add(bean)
    }

    fun addAll(lists: List<T>) {
        data.addAll(lists)
    }

    fun addAll(position: Int, lists: List<T>) {
        data.addAll(position, lists)
    }

    fun clear() {
        data.clear()
    }

}