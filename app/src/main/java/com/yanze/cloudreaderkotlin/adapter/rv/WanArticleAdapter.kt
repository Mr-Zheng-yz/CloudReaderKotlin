package com.yanze.cloudreaderkotlin.adapter.rv

import android.annotation.SuppressLint
import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.wan.ArticlesBean
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.item_wan_article.view.*

class WanArticleAdapter : BaseRecyclerViewAdapter<ArticlesBean>() {

    /**
     * 是我的收藏页进来的，全部是收藏状态。bean里面没有返回isCollect信息
     */
    var isCollectList = false
    /**
     * 不显示类别信息
     */
    var isNoShowChapterName = false
    /**
     * 列表中是否显示图片
     */
    private var isNoImage = false

    fun setCollectList(){
        this.isCollectList = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ArticlesBean> {
        return ViewHolder(getView(parent, R.layout.item_wan_article))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<ArticlesBean>(view) {
        @SuppressLint("SetTextI18n")
        override fun onBaseBindViewHolder(bean: ArticlesBean, position: Int) {
            val nullBean = bean as ArticlesBean? ?: return
            if (isCollectList) {
                nullBean.collect = true
            }
            //parent
            view.cl_parent.setOnClickListener {
                WebViewActivity.loadUrl(view.context, nullBean.link, nullBean.title)
            }
            view.iv_new.visibility = if (nullBean.fresh) View.VISIBLE else View.GONE //new
            //tag
            view.tv_tag_name.setOnClickListener {
                view.context.showToast("${nullBean.chapterName}")
            }
            view.tv_tag_name.text = nullBean.chapterName ?: ""
            view.tv_tag_name.visibility = if (isNoShowChapterName) View.GONE else View.VISIBLE
            //image
            view.iv_image.visibility = if (nullBean.isShowImage) View.VISIBLE else View.GONE
            ImageLoadUtil.displayListImage(view.iv_image, nullBean.envelopePic, 1)
            //txt
            view.tv_title.text = Html.fromHtml(nullBean.title)
            view.tv_time.text = nullBean.niceDate
            view.textView3.text = "${view.context.getString(R.string.string_dian)}${nullBean.author}"
            //checkbox
            view.vb_collect.isChecked = bean.collect
            view.vb_collect.setOnClickListener {
                view.vb_collect.isSelected = !view.vb_collect.isChecked
            }
        }
    }

}