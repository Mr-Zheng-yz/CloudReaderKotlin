package com.yanze.cloudreaderkotlin.adapter.rv

import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.film.FilmDetailBean
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import kotlinx.android.synthetic.main.item_film_detail_actor.view.*

class FilmDetailActorAdapter : BaseRecyclerViewAdapter<FilmDetailBean.ActorBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<FilmDetailBean.ActorBean> {
        return ActorViewHolder(getView(parent, R.layout.item_film_detail_actor))
    }

    class ActorViewHolder(view:View):BaseRecyclerViewHolder<FilmDetailBean.ActorBean>(view){
        override fun onBaseBindViewHolder(bean: FilmDetailBean.ActorBean, position: Int) {
            ImageLoadUtil.showPersonImg(view.iv_actor,bean.img)
            view.tv_actor_name.text = bean.name
            view.tv_actor_rolename.text = bean.roleImg
        }
    }
}