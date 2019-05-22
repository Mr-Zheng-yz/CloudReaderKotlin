package com.yanze.cloudreaderkotlin.adapter.rv

import android.content.Context
import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.*
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.wan.NaviBean
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.RecyclerViewHelper
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.item_navi_content.view.*
import kotlinx.android.synthetic.main.item_navi_tag.view.*

class NaviContentAdapter(val context: Context) : BaseRecyclerViewAdapter<NaviBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<NaviBean> {
        return ViewHolder(getView(parent, R.layout.item_navi_content))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<NaviBean>(view) {
        override fun onBaseBindViewHolder(bean: NaviBean, position: Int) {
            view.tv_navi_content_name.text = Html.fromHtml(bean.name)

            //流式布局菜单
            val layoutManager = RecyclerViewHelper.getFlexLayoutManager(context)
//            val layoutManager = FlexboxLayoutManager(context)
//            layoutManager.flexWrap = FlexWrap.WRAP
//            layoutManager.flexDirection = FlexDirection.ROW
//            layoutManager.justifyContent = JustifyContent.FLEX_START
//            layoutManager.alignItems = AlignItems.FLEX_START

            view.rv_content.layoutManager = layoutManager
            val tagAdapter = NaviTagAdapter()
            tagAdapter.addAll(bean.articles)
            view.rv_content.adapter = tagAdapter
            view.rv_content.isNestedScrollingEnabled = false //不滑动
            view.rv_content.setHasFixedSize(false)
        }
    }

//    class NaviTagAdapter : BaseRecyclerViewAdapter<NaviBean.ArticleBean>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TagViewHolder(getView(parent,R.layout.item_navi_tag))
//
//        inner class TagViewHolder(view: View) : BaseRecyclerViewHolder<NaviBean.ArticleBean>(view) {
//            override fun onBaseBindViewHolder(bean: NaviBean.ArticleBean, position: Int) {
//                view.tv_navi_tag.setTextColor(CommonUtils.randomColor())
//                view.tv_navi_tag.text = Html.fromHtml(bean.title)
//                view.tv_navi_tag.setOnClickListener {
//                    WebViewActivity.loadUrl(view.context, bean.link, bean.title)
//                }
//            }
//        }
//    }

}