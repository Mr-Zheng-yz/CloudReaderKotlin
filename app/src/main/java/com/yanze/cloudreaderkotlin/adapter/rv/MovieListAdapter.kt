package com.yanze.cloudreaderkotlin.adapter.rv

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.nineoldandroids.view.ViewHelper
import com.nineoldandroids.view.ViewPropertyAnimator
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.moviechild.SubjectsBean
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.StringFormatUtil
import com.yanze.cloudreaderkotlin.ui.movie.MovieDetailActivity
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieListAdapter(var context:Context?) : BaseRecyclerViewAdapter<SubjectsBean>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<SubjectsBean> {
        return ViewHolder(getView(parent, R.layout.item_movie))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<SubjectsBean>(view) {
        override fun onBaseBindViewHolder(bean: SubjectsBean, position: Int) {
            ImageLoadUtil.showMovieImg(view.iv_move_img, bean.images.large)
            view.tv_movie_title.text = bean.title
            view.tv_movie_directors.text = StringFormatUtil.formatName(bean.directors)
            view.tv_movie_casts.text = StringFormatUtil.formatName(bean.casts)
            view.tv_movie_genres.text = "${view.tv_movie_genres.context.getString(R.string.string_type)}${StringFormatUtil.formatGenres(bean.genres)}"
            view.tv_movie_rating_rate.text = "${view.tv_movie_genres.context.getString(R.string.string_rating)}${(bean.rating.average)}"
            view.rl_movie_item.setOnClickListener {
//                context?.showToast("电影详情")
                MovieDetailActivity.start(context as Activity,bean,view.iv_move_img)
            }
            view.view_color.setBackgroundColor(CommonUtils.randomColor())
            ViewHelper.setScaleX(itemView, 0.8f)
            ViewHelper.setScaleY(itemView, 0.8f)
            ViewPropertyAnimator.animate(itemView).scaleX(1f).setDuration(350).setInterpolator(OvershootInterpolator()).start()
            ViewPropertyAnimator.animate(itemView).scaleY(1f).setDuration(350).setInterpolator(OvershootInterpolator()).start()
        }
    }
}