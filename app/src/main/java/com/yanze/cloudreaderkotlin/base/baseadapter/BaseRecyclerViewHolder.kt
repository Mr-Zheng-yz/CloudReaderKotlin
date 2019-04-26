package com.tuju.jetpackfirstdemo.base.baseadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<T> constructor(var view: View) : RecyclerView.ViewHolder(view) {

//    var view:View = LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
//
//    init {
//       var view1 =  LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
//    }

    /**
     * 在构造方法后面使用   :super  来调用父构造方法
     */
//    constructor(parent:ViewGroup,layoutId:Int) : super(LayoutInflater.from(parent.context).inflate(layoutId,parent,false))

    abstract fun onBaseBindViewHolder(bean: T, position: Int)

}