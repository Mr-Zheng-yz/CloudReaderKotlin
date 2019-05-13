package com.yanze.cloudreaderkotlin.adapter.rv

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.utils.DensityUtil
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import com.yanze.cloudreaderkotlin.view.viewbigimage.ViewBigImageActivity
import kotlinx.android.synthetic.main.item_welfare.view.*

class WelfareAdapter(val context: Context) : BaseRecyclerViewAdapter<GankResult>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<GankResult> {
        return ViewHolder(getView(parent, R.layout.item_welfare))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<GankResult>(view) {
        override fun onBaseBindViewHolder(bean: GankResult, position: Int) {
            /**
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */
            if (position % 2 == 0) {
                DensityUtil.setViewMargin(view, false, 12, 6, 12, 0)
            } else {
                DensityUtil.setViewMargin(view, false, 6, 12, 12, 0)
            }
            ImageLoadUtil.displayListImage(view.iv_welfare, bean.url, 1)
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
                ViewBigImageActivity.start(context, position, imageList, imageTitle)
            }
        }
    }
}