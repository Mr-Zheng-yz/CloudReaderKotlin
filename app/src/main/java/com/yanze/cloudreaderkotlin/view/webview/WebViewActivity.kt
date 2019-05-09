package com.yanze.cloudreaderkotlin.view.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.yanze.cloudreaderkotlin.MainActivity
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.utils.*
import com.yanze.cloudreaderkotlin.utils.statusbar.StatusBarUtil
import com.yanze.cloudreaderkotlin.view.viewbigimage.ViewBigImageActivity
import com.yanze.cloudreaderkotlin.view.webview.config.*
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity(), IWebPageView {

    // title
    private lateinit var mTitle: String
    // 网页链接
    private lateinit var mUrl: String
    private lateinit var mWebChromeClient: MyWebChromeClient
    private var videoFullView: FrameLayout? = null //全屏容器

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        getIntentData()
        initTitle()
        initWebView()
        view_webview.loadUrl(mUrl)
        getDataFromBrowser(intent)
    }

    private fun getIntentData() {
        if (intent != null) {
            mTitle = intent.getStringExtra("mTitle")
            mUrl = intent.getStringExtra("mUrl")
        }
    }

    private fun initTitle() {
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme))
        setSupportActionBar(title_tool_bar)
        //去除默认Title显示
        actionBar?.setDisplayShowTitleEnabled(false)
        title_tool_bar.overflowIcon = ContextCompat.getDrawable(this@WebViewActivity, R.drawable.actionbar_more)
        tv_gun_title.postDelayed({ tv_gun_title.isSelected = true }, 1900)
        setTitleTxt(mTitle)
    }

    fun setTitleTxt(title: String?) {
        tv_gun_title.text = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.webview_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> handleFinish()  //返回键
            R.id.actionbar_share -> {
                showToast("分享")
            }
            R.id.actionbar_cope -> {
                BaseTools.copy(view_webview.url)
                showToast("复制成功")
            }
            R.id.actionbar_open -> BaseTools.openLink(this@WebViewActivity, view_webview.url)  //打开链接
            R.id.actionbar_webview_refresh -> view_webview?.reload() //刷新
            R.id.actionbar_collect -> {
                showToast("添加到收藏，未完成")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        pb_progress.visibility = View.VISIBLE
        val ws = view_webview.settings
        //网页内容那个的宽度是否可大于WebView控件的宽度
        ws.loadWithOverviewMode = false
        //保存表单数据
        ws.saveFormData = true
        //是否应该支持使用屏幕缩放控件和手势缩放
        ws.setSupportZoom(true)
        ws.builtInZoomControls = true
        ws.displayZoomControls = false
        //启用应用缓存
        ws.setAppCacheEnabled(true)
        //设置缓存模式
        ws.cacheMode = WebSettings.LOAD_DEFAULT
        //setDefaultZoom api19被弃用
        //是否可以任意比例缩放
        ws.useWideViewPort = true
        //不缩放
        view_webview.setInitialScale(100)
        //告诉WebView弃用JavaScrip执行。默认是false
        ws.javaScriptEnabled = true
        //页面加载好以后，再放开图片
        ws.blockNetworkImage = false
        //使用localStorage则必须打开
        ws.domStorageEnabled = true
        //排版适应屏幕
        ws.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        //webView从5.0开始默认不允许混合模式，https中不能加载http资源，需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        /** 设置字体默认缩放大小（改变网页字体大小，setTextSize api14被弃用）**/
        ws.textZoom = 100

        mWebChromeClient = MyWebChromeClient(this@WebViewActivity)
        view_webview.webChromeClient = mWebChromeClient
        //与js交互
        view_webview.addJavascriptInterface(ImageClickInterface(this), "injectedObject")
        view_webview.webViewClient = MyWebViewClient(this)
        view_webview.setOnLongClickListener {
            return@setOnLongClickListener handleLongImage()
        }
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            getDataFromBrowser(intent)
        }
    }

    /**
     * 作为三方浏览器打开
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private fun getDataFromBrowser(intent: Intent) {
        val data = intent.data
        if (data != null) {
            val scheme = data.scheme
            val host = data.host
            val path = data.path
            val url = "$scheme://$host$path"
            view_webview.loadUrl(url)
        }
    }

    /**
     * 长按图片事件处理
     */
    private fun handleLongImage(): Boolean {
        val hitTestResult = view_webview.hitTestResult
        if (hitTestResult.type == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            //弹出保存图片的对话框
            AlertDialog.Builder(this@WebViewActivity)
                    .setItems(arrayOf("查看大图", "保存图片到相册")) { _, which ->
                        val picUrl = hitTestResult.extra
                        when (which) {
                            0 -> ViewBigImageActivity.start(this@WebViewActivity,0, arrayListOf("$picUrl"), arrayListOf("$picUrl"))
                            1 -> RxSaveImage.saveImageToGallery(this@WebViewActivity,"$picUrl","$picUrl")
                        }
                    }
            return true
        }
        return false
    }

    override fun hindProgressBar() {
        pb_progress.visibility = View.GONE
    }

    override fun showWebView() {
        view_webview.visibility = View.VISIBLE
    }

    override fun hindWebView() {
        view_webview.visibility = View.GONE
    }

    override fun startProgress(newProgress: Int) {
        pb_progress.visibility = View.VISIBLE
        pb_progress.progress = newProgress
        if (newProgress == 100) {
            pb_progress.visibility = View.GONE
        }
    }

    override fun fullViewAddView(view: View) {
        val decor = window.decorView as FrameLayout
        videoFullView = FullscreenHolder(this@WebViewActivity)
        videoFullView?.addView(view)
        decor.addView(videoFullView)
    }

    override fun showVideoFullView() {
        videoFullView?.visibility = View.VISIBLE
    }

    override fun hindVideoFullView() {
        videoFullView?.visibility = View.GONE
    }

    override fun addImageClickListener() {
//        loadImageClickJS();
//        loadTextClickJS();
    }

    private fun loadImageClickJS() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        view_webview.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()")
    }

    private fun loadTextClickJS() {
        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        view_webview.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()")
    }

    fun getVideoFullView() = videoFullView

    /**
     * 全屏时按键执行退出全屏方法
     */
    fun hideCustomView() {
        mWebChromeClient.onHideCustomView()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            when {
                mWebChromeClient.inCustomView() -> {
                    hideCustomView()
                    return true
                }
                view_webview.canGoBack() -> {
                    view_webview.goBack()
                    return true
                }
                else -> handleFinish()
            }
        }
        return false
    }

    /**
     * 直接通过三方浏览器打开时，回退到首页
     */
    private fun handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
        if (!MainActivity.isLaunch) {
            MainActivity.start(this@WebViewActivity)
        }
    }

    /**
     * 上传图片之后的回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MyWebChromeClient.FILECHOOSER_RESULTCODE -> mWebChromeClient.mUploadMessage(intent, resultCode)
            MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5 -> mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode)
        }
    }

    companion object {
        fun loadUrl(context: Context, url: String?, title: String? = "云阅Kotlin") {
            if (CheckNetwork.isNetworkConnected(context)) {
                val intent = Intent(context, WebViewActivity::class.java)
//                intent.putExtra("mTitle", context.getString(R.string.app_name))
//                intent.putExtra("mUrl", context.getString(R.string.my_blog))
                intent.putExtra("mTitle", title ?: context.getString(R.string.app_name))
                intent.putExtra("mUrl", url ?: context.getString(R.string.my_blog))
                context.startActivity(intent)
            } else {
                context.showToast("当前网络不可用，请检查你的网络设置")
            }
        }
    }
}
