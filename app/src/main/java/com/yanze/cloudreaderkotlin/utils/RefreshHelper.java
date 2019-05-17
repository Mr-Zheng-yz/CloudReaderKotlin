package com.yanze.cloudreaderkotlin.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.example.xrecyclerview.XRecyclerView;
import com.yanze.cloudreaderkotlin.R;
import com.yanze.cloudreaderkotlin.view.MyDividerItemDecoration;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @author jingbin
 * @data 2019/1/20
 * @description
 */

public class RefreshHelper {

    public static void init(XRecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.clearHeader();
        recyclerView.setItemAnimator(null);
        MyDividerItemDecoration itemDecoration = new MyDividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL, false);
        Drawable drawable = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.shape_line);
        drawable.setColorFilter(CommonUtils.randomColor(),PorterDuff.Mode.SRC);
        itemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(itemDecoration);
    }
}
