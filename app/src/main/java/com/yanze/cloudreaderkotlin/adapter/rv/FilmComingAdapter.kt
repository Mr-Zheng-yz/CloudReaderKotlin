package com.yanze.cloudreaderkotlin.adapter.rv

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.film.ComingFilmeBean
import com.yanze.cloudreaderkotlin.data.bean.film.FilmeItemBean
import com.yanze.cloudreaderkotlin.ui.mtime.FilmDetailActivity
import com.yanze.cloudreaderkotlin.utils.DensityUtil
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.item_film_coming.view.*

class FilmComingAdapter : BaseRecyclerViewAdapter<ComingFilmeBean>() {
    private var width = 0

    init {
        val px = DensityUtil.dip2px(36f)
        width = (DensityUtil.getDisplayWidth() - px) / 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ComingFilmeBean> {
        return ComingViewHolder(getView(parent, R.layout.item_film_coming))
    }

    inner class ComingViewHolder(view: View) : BaseRecyclerViewHolder<ComingFilmeBean>(view) {
        override fun onBaseBindViewHolder(bean: ComingFilmeBean, position: Int) {
            ImageLoadUtil.displayListImage(view.iv_top_photo, bean.image, 0)
            view.tv_name.text = bean.title
            view.tv_rate.text = bean.releaseDate
            DensityUtil.formatHeight(view.iv_top_photo, width, 0.758f, 1)
            view.setOnClickListener {
//                view.context.showToast(bean.title)
                var actor1 = bean.actor1
                val actor2 = bean.actor2
                if (actor2.isNotEmpty()) {
                    actor1 = "${actor1} / $actor2"
                }
                val filmItemBean = FilmeItemBean(id = bean.id, dN = bean.director, tCn = bean.title, tEn = bean.releaseDate
                        , movieType = bean.type, img = bean.image, locationName = bean.locationName, actors = actor1)
                FilmDetailActivity.start(view.context as Activity,filmItemBean,view.iv_top_photo)
            }
        }
    }

}