package com.yanze.cloudreaderkotlin.adapter.rv

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import com.yanze.cloudreaderkotlin.view.viewbigimage.ViewBigImageActivity
import kotlinx.android.synthetic.main.item_welfare.view.*

class WelfareAdapter(val context:Context) : BaseRecyclerViewAdapter<GankResult>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<GankResult> {
        return ViewHolder(getView(parent, R.layout.item_welfare))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<GankResult>(view) {
        override fun onBaseBindViewHolder(bean: GankResult, position: Int) {
            ImageLoadUtil.displayListImage(view.iv_welfare,bean.url,1)
            view.setOnClickListener {
                val imageList = arrayListOf<String>()
                for (child in data) {
                    imageList.add(child.url)
                }
                val imageTitle = arrayListOf<String>().let {
                    for (child in data) {
                        it.add(child.desc)
                    }
                    return@let it
                }
                ViewBigImageActivity.start(context,position,imageList,imageTitle)
            }
        }
    }
}