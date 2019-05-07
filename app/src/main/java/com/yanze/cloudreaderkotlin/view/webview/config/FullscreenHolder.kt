package com.yanze.cloudreaderkotlin.view.webview.config

import android.content.Context
import android.view.MotionEvent
import android.widget.FrameLayout

class FullscreenHolder(context: Context) : FrameLayout(context) {
    init {
        setBackgroundColor(context.resources.getColor(android.R.color.black))
    }

    override fun onTouchEvent(event: MotionEvent?) = true

}