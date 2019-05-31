package com.yanze.cloudreaderkotlin.adapter.rv

import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.film.FilmDetailBean
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import kotlinx.android.synthetic.main.item_film_detail_image.view.*

class FilmDetailImageAdapter : BaseRecyclerViewAdapter<FilmDetailBean.StageImgBean.StageBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<FilmDetailBean.StageImgBean.StageBean> {
        return ImgViewHolder(getView(parent, R.layout.item_film_detail_image))
    }

    class ImgViewHolder(view: View) : BaseRecyclerViewHolder<FilmDetailBean.StageImgBean.StageBean>(view) {
        override fun onBaseBindViewHolder(bean: FilmDetailBean.StageImgBean.StageBean, position: Int) {
            ImageLoadUtil.displayListImage(view.iv_image, bean.imgUrl, 1)
        }
    }
}