package com.yanze.cloudreaderkotlin.view.webview.config

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity

/**
 * Created by jingbin on 2016/11/17.
 * - 播放网络视频配置
 * - 上传图片(兼容)
 * 点击空白区域的左边,因是公司图片,自己编辑过,所以显示不全,见谅
 *
 * TODO(不明之处...)
 */
class MyWebChromeClient(private var mIWebPageView: IWebPageView) : WebChromeClient() {
    private val mActivity = mIWebPageView as WebViewActivity
    private var mXCustomView: View? = null
    private var mXcustomViewCallback: CustomViewCallback? = null
    private var mXProgressViewo: View? = null

    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mUploadMessageForAndroid5: ValueCallback<Array<Uri>>? = null

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE //横屏播放
        mIWebPageView.hindWebView()
        //如果一个视图已经存在，那么立刻终止并新建
        if (mXCustomView != null) {
            callback?.onCustomViewHidden()
            return
        }
        mActivity.fullViewAddView(view!!)
        mXCustomView = view
        mXcustomViewCallback = callback
        mIWebPageView.showVideoFullView()
    }

    //视频播放退出全屏会被调用
    override fun onHideCustomView() {
        if (mXCustomView == null)
            return
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT  //返回竖屏

        mXCustomView?.visibility = View.GONE
        mActivity.getVideoFullView()?.removeView(mXCustomView)
        mXCustomView = null
        mIWebPageView.hindVideoFullView()
        mXcustomViewCallback?.onCustomViewHidden()
        mIWebPageView.showWebView()
    }

    //视频加载时进程loading
    override fun getVideoLoadingProgressView(): View? {
        if (mXProgressViewo == null) {
            val inflater = LayoutInflater.from(mActivity)
            mXProgressViewo = inflater.inflate(R.layout.video_loading_progress, null)
        }
        return mXProgressViewo
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        mIWebPageView.startProgress(newProgress)
    }

    /**
     * 判断是否全屏
     */
    fun inCustomView() = (mXCustomView != null)

    private var title = ""

    fun getTitle() = "$title  "

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        //设置title
        mActivity.setTitleTxt(title)
        this.title = "$title"
    }

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        openFileChooserImplForAndroid5(filePathCallback)
        return true
    }

    //扩展浏览器上传文件
    //3.0++版本
    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
        openFileChooserImpl(uploadMsg)
    }

    //3.0--版本
    fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
        openFileChooserImpl(uploadMsg)
    }

    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
        openFileChooserImpl(uploadMsg)
    }

    private fun openFileChooserImpl(uploadMsg: ValueCallback<Uri>) {
        mUploadMessage = uploadMsg
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        mActivity.startActivityForResult(Intent.createChooser(i, "文件选择"), FILECHOOSER_RESULTCODE)
    }

    private fun openFileChooserImplForAndroid5(uploadMsg: ValueCallback<Array<Uri>>?) {
        mUploadMessageForAndroid5 = uploadMsg
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"

        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择")

        mActivity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5)
    }

    /**
     * 5.0以下 上传图片成功后的回调
     */
    fun mUploadMessage(intent: Intent?, resultCode: Int) {
        if (null == mUploadMessage)
            return
        val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
        mUploadMessage?.onReceiveValue(result)
        mUploadMessage = null
    }

    /**
     * 5.0以上 上传图片成功后的回调
     */
    fun mUploadMessageForAndroid5(intent: Intent?, resultCode: Int) {
        if (null == mUploadMessageForAndroid5)
            return
        val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
        if (result != null) {
            mUploadMessageForAndroid5?.onReceiveValue(arrayOf(result))
        } else {
            mUploadMessageForAndroid5?.onReceiveValue(arrayOf())
        }
        mUploadMessageForAndroid5 = null
    }

    companion object {
        const val FILECHOOSER_RESULTCODE = 1
        const val FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2
    }
}