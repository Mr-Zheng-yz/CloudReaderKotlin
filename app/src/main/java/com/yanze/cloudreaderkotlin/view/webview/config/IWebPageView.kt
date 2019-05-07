package com.yanze.cloudreaderkotlin.view.webview.config

import android.view.View

interface IWebPageView {

    // 隐藏进度条
    fun hindProgressBar()

    // 显示webview
    fun showWebView()

    // 隐藏webview
    fun hindWebView()

    /**
     * 进度条变化时调用
     */
    fun startProgress(newProgress: Int)

    /**
     * 添加js监听
     */
    fun addImageClickListener()

    /**
     * 播放网络视频全屏调用
     */
    fun fullViewAddView(view: View)

    fun showVideoFullView()

    fun hindVideoFullView()

}