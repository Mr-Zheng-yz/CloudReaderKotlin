package com.yanze.cloudreaderkotlin.adapter.rv

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.moviechild.SubjectsBean
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import com.yanze.cloudreaderkotlin.view.movie.MovieDetailActivity
import kotlinx.android.synthetic.main.item_douban_top.view.*

class MovieTopAdapter : BaseRecyclerViewAdapter<SubjectsBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<SubjectsBean> {
        return ViewHolder(getView(parent, R.layout.item_douban_top))
    }

    inner class ViewHolder(view:View) :BaseRecyclerViewHolder<SubjectsBean>(view){
        override fun onBaseBindViewHolder(bean: SubjectsBean, position: Int) {
            ImageLoadUtil.showMovieImg(view.iv_top_photo, bean.images.large)
            view.tv_name.text = bean.title
            view.tv_rate.text = "${view.context.getString(R.string.string_rating)}${bean.rating.average}"
            view.setOnClickListener {
                view.context.showToast("电影详情")
                MovieDetailActivity.start(view.context as Activity,bean,view.iv_top_photo)
            }
        }
    }
}