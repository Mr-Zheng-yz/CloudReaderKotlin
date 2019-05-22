package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.google.android.flexbox.*
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.view.MyDividerItemDecoration

class RecyclerViewHelper {

    companion object {
        fun initRefresh(recyclerView: XRecyclerView) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.setPullRefreshEnabled(false)
            recyclerView.clearHeader()
            recyclerView.itemAnimator = null
            val itemDecoration = MyDividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL, false)
            val drawable = ContextCompat.getDrawable(recyclerView.context, R.drawable.shape_line)
            drawable!!.setColorFilter(CommonUtils.randomColor(), PorterDuff.Mode.SRC)
            itemDecoration.setDrawable(drawable)
            recyclerView.addItemDecoration(itemDecoration)
        }

        fun getFlexLayoutManager(context: Context): FlexboxLayoutManager {
            //流式布局菜单
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexWrap = FlexWrap.WRAP
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            return layoutManager
        }

    }


}