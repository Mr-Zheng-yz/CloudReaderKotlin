package com.yanze.cloudreaderkotlin.view.webview.config

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yanze.cloudreaderkotlin.utils.BaseTools
import com.yanze.cloudreaderkotlin.utils.CheckNetwork
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity

/**
 * Created by jingbin on 2016/11/17.
 * 监听网页链接:
 * - 优酷视频直接跳到自带浏览器
 * - 根据标识:打电话、发短信、发邮件
 * - 进度条的显示
 * - 添加javascript监听
 */
class MyWebViewClient(val mIWebPageView: IWebPageView) : WebViewClient() {
    private var mActivity:WebViewActivity = mIWebPageView as WebViewActivity

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url == null || url.isEmpty()) {
            return false
        }
        if (url.startsWith("http:") || url.startsWith("https:")) {
            //可能有提示下载Apk文件
            if (url.contains(".apk")) {
                handleOtherwise(mActivity,url)
                return true
            }
            return false
        }

        handleOtherwise(mActivity,url)
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (!CheckNetwork.isNetworkConnected(mActivity)) {
            mIWebPageView.hindProgressBar()
        }
        // html加载完成之后，添加监听图片的点击js函数
        mIWebPageView.addImageClickListener()
        super.onPageFinished(view, url)
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
        if (newScale - oldScale > 7) {
            view?.setInitialScale((oldScale / newScale * 100).toInt()) //异常放大，缩回去。
        }
    }

    /**
     * 网页里可能唤起其他的app
     */
    private fun handleOtherwise(activity: Activity, url: String) {
        var appPackageName = ""
        // 支付宝支付
        if (url.contains("alipays")) {
            appPackageName = "com.eg.android.AlipayGphone"

            // 微信支付
        } else if (url.contains("weixin://wap/pay")) {
            appPackageName = "com.tencent.mm"

            // 京东产品详情
        } else if (url.contains("openapp.jdmobile")) {
            appPackageName = "com.jingdong.app.mall"
        } else {
            startActivity(url)
        }
        if (BaseTools.isApplicationAvilible(activity, appPackageName)) {
            startActivity(url)
        }
    }

    private fun startActivity(url: String) {
        try {
            val intent1 = Intent()
            intent1.action = "android.intent.action.VIEW"
            val uri = Uri.parse(url)
            intent1.data = uri
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mActivity.startActivity(intent1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}