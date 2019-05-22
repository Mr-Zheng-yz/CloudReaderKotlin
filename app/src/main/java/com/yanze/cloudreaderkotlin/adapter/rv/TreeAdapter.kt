package com.yanze.cloudreaderkotlin.adapter.rv

import android.text.Html
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.*
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewAdapter
import com.tuju.jetpackfirstdemo.base.baseadapter.BaseRecyclerViewHolder
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.data.bean.wan.TreeBean
import com.yanze.cloudreaderkotlin.utils.RecyclerViewHelper
import kotlinx.android.synthetic.main.item_tree.view.*

class TreeAdapter : BaseRecyclerViewAdapter<TreeBean>() {
    var mProjectPosition = 26

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<TreeBean> {
        return ViewHolder(getView(parent, R.layout.item_tree))
    }

    inner class ViewHolder(view: View) : BaseRecyclerViewHolder<TreeBean>(view) {
        override fun onBaseBindViewHolder(bean: TreeBean, position: Int) {
            view.tv_tree_title.text = Html.fromHtml(bean.name)
            if ("开源项目主Tab" == bean.name) {
                mProjectPosition = position
            }

            //init flexbox
            //有关FlexBox属性，可参考
            //https://mr-zheng-yz.github.io/2019/05/15/FlexboxLayout%E6%B5%85%E6%9E%90-Google%E5%AE%98%E6%96%B9%E6%B5%81%E5%BC%8F%E5%B8%83%E5%B1%80%E6%96%B9%E6%A1%88/
            val layoutManager = RecyclerViewHelper.getFlexLayoutManager(view.context)
//            val layoutManager = FlexboxLayoutManager(view.context)
//            layoutManager.flexWrap = FlexWrap.WRAP
//            layoutManager.flexDirection = FlexDirection.ROW
//            layoutManager.justifyContent = JustifyContent.FLEX_START
//            layoutManager.alignItems = AlignItems.FLEX_START

            val tagAdapter = TreeTagAdapter(bean)
            tagAdapter.addAll(bean.children)

            view.rv_tree_tag.layoutManager = layoutManager
            view.rv_tree_tag.adapter = tagAdapter
            view.rv_tree_tag.isNestedScrollingEnabled = false
            view.rv_tree_tag.setHasFixedSize(false)
        }
    }

}