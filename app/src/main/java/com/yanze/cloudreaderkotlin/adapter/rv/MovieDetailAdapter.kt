package com.yanze.cloudreaderkotlin.adapter.rv

import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.moviechild.PersonBean
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.item_movie_detail_person.view.*

class MovieDetailAdapter : BaseRecyclerViewAdapter<PersonBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<PersonBean> {
        return ViewHolder(getView(parent, R.layout.item_movie_detail_person))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<PersonBean>(view) {
        override fun onBaseBindViewHolder(bean: PersonBean, position: Int) {
            ImageLoadUtil.showPersonImg(view.iv_avatar, bean.avatars.large)
            view.tv_person_name.text = bean.name
            view.tv_person_type.text = bean.type
            view.setOnClickListener {
                view.context.showToast(bean.alt)
            }
        }
    }

}