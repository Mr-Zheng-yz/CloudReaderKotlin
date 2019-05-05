package com.yanze.cloudreaderkotlin.adapter.rv

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.moviechild.SubjectsBean
import com.yanze.cloudreaderkotlin.utils.DensityUtil
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.view.dialog.DialogBuild
import kotlinx.android.synthetic.main.item_douban_top.view.*

class MovieTopAdapter : BaseRecyclerViewAdapter<SubjectsBean>() {

    private var width:Int = 0

    init {
        val px = DensityUtil.dip2px(36f)
        width = (DensityUtil.getDisplayWidth() - px) / 3
    }

    private var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<SubjectsBean> {
        return ViewHolder(getView(parent, R.layout.item_douban_top))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<SubjectsBean>(view) {
        override fun onBaseBindViewHolder(bean: SubjectsBean, position: Int) {
            DensityUtil.formatHeight(view.iv_top_photo,width,0.758f,1)
            ImageLoadUtil.showMovieImg(view.iv_top_photo, bean.images.large)
            view.tv_name.text = bean.title
            view.tv_rate.text = "${view.context.getString(R.string.string_rating)}${bean.rating.average}"
            view.setOnClickListener {
                listener?.onClick(bean, view.iv_top_photo)
            }
            view.setOnLongClickListener {
                val title = "Top${position + 1}：${bean.title}"
                DialogBuild.showCustom(it, title, "查看详情") { _, _ ->
                    listener?.onClick(bean, view.iv_top_photo)
                }
                return@setOnLongClickListener false
            }
        }
    }

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClick(bean: SubjectsBean, imageView: ImageView): Unit
    }
}