package com.yanze.cloudreaderkotlin.adapter.rv

import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.wan.NaviBean
import kotlinx.android.synthetic.main.item_navi.view.*

class NaviAdapter : BaseRecyclerViewAdapter<NaviBean>() {

    private var listener: OnSelectListener? = null

    fun setOnSelectListener(listener: OnSelectListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<NaviBean> {
        return ViewHolder(getView(parent, R.layout.item_navi))
    }

    inner class ViewHolder(view:View) : BaseRecyclerViewHolder<NaviBean>(view){
        override fun onBaseBindViewHolder(bean: NaviBean, position: Int) {
            view.tv_title.isSelected = bean.selected
            view.tv_title.text = Html.fromHtml(bean.name)
            view.tv_title.setOnClickListener {
                listener?.onSelected(position)
            }
        }
    }


    public interface OnSelectListener{
        fun onSelected(position:Int)
    }

}