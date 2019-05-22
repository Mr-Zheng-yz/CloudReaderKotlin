package com.yanze.cloudreaderkotlin.adapter.rv

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.TimeUtil
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.item_android.view.*

class GankDataAdapter : BaseRecyclerViewAdapter<GankResult>() {
    private var isAll = false

    fun setAllType(isall: Boolean) {
        this@GankDataAdapter.isAll = isall
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<GankResult> {
        return ViewHolder(getView(parent, R.layout.item_android))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<GankResult>(view) {
        @SuppressLint("SetTextI18n")
        override fun onBaseBindViewHolder(bean: GankResult, position: Int) {
            if (isAll && "福利" == bean.type) { //Kotlin中'=='表示对比的是值,相当于equal，而'==='对比的是址（同java的==）
                view.iv_all_welfare.visibility = View.VISIBLE
                view.ll_welfare_other.visibility = View.GONE
                ImageLoadUtil.displayListImage(view.iv_all_welfare,bean.url,1)
            }else{
                view.iv_all_welfare.visibility = View.GONE
                view.ll_welfare_other.visibility = View.VISIBLE
            }

            if (isAll) {
                view.tv_content_type.visibility = View.VISIBLE
                view.tv_content_type.text = "${view.context.getString(R.string.string_dian)}${bean.type}"
            }else{
                view.tv_content_type.visibility = View.GONE
            }

            if (bean.images != null
                    && bean.images.isNotEmpty()) {
                view.iv_android_pic.visibility = View.VISIBLE
                ImageLoadUtil.displayGif(view.iv_android_pic,bean.images[0])
            }else{
                view.iv_android_pic.visibility = View.GONE
            }

            //
            view.tv_android_des.text = bean.desc
            view.tv_android_who.text = bean.who ?: view.context.getString(R.string.no_name)
            view.tv_android_time.text = TimeUtil.getTranslateTime(bean.publishedAt)

            //点击事件
            view.setOnClickListener {
                WebViewActivity.loadUrl(view.context,bean.url,bean.desc)
            }
        }
    }

}