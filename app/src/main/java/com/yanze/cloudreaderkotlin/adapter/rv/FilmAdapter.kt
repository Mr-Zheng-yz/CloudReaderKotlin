package com.yanze.cloudreaderkotlin.adapter.rv

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.nineoldandroids.view.ViewHelper
import com.nineoldandroids.view.ViewPropertyAnimator
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.film.FilmeItemBean
import com.yanze.cloudreaderkotlin.ui.mtime.FilmDetailActivity
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.item_film.view.*

class FilmAdapter : BaseRecyclerViewAdapter<FilmeItemBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<FilmeItemBean> {
        return ViewHolder(getView(parent, R.layout.item_film))
    }

    class ViewHolder(view: View) : BaseRecyclerViewHolder<FilmeItemBean>(view) {
        @SuppressLint("SetTextI18n")
        override fun onBaseBindViewHolder(bean: FilmeItemBean, position: Int) {
            //View
            ImageLoadUtil.showMovieImg(view.iv_one_photo, bean.img)
            view.tv_one_title.text = bean.tCn
            view.tv_one_directors.text = bean.dN
            view.tv_one_casts.text = bean.actors
            view.tv_one_genres.text = "${view.context.getString(R.string.string_type)}${bean.movieType}"
            view.tv_one_rating_rate.text = "${view.context.getString(R.string.string_rating)}${bean.r}"
            view.view_color.setBackgroundColor(CommonUtils.randomColor())

            ViewHelper.setScaleX(itemView,0.8f)
            ViewHelper.setScaleY(itemView, 0.8f)
            ViewPropertyAnimator.animate(itemView).scaleX(1f).setDuration(350).setInterpolator(OvershootInterpolator()).start()
            ViewPropertyAnimator.animate(itemView).scaleY(1f).setDuration(350).setInterpolator(OvershootInterpolator()).start()
            view.ll_one_item.setOnClickListener {
//                view.context.showToast(bean.tCn)
                FilmDetailActivity.start(view.context as Activity,bean,view.iv_one_photo)
            }
        }
    }
}