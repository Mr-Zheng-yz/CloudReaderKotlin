package com.yanze.cloudreaderkotlin.ui.navi_menu

import android.content.Context
import android.content.Intent
import com.yanze.cloudreaderkotlin.utils.showToast
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.base.BaseActivity
import com.yanze.cloudreaderkotlin.utils.BaseTools
import com.yanze.cloudreaderkotlin.utils.PerfectClickListener
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setTitle("关于云阅")
        showContentView()
        tv_version_name.text = "当前版本 V${BaseTools.getVersionName()}"

        // 直接写在布局文件里会很耗内存
        Glide.with(this).load(R.drawable.ic_cloudreader).into(iv_icon)
        tv_gankio.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_gankio.paint.isAntiAlias = true//抗锯齿
        tv_douban.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_douban.paint.isAntiAlias = true//抗锯齿
        tv_wanandroid.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_wanandroid.paint.isAntiAlias = true//抗锯齿

        tv_gankio.setOnClickListener(listener)
        tv_douban.setOnClickListener(listener)
        tv_wanandroid.setOnClickListener(listener)
        tv_download_url.setOnClickListener(listener)
        tv_about_star.setOnClickListener(listener)
        tv_good_cloud.setOnClickListener(listener)
    }


    private val listener = object : PerfectClickListener() {
        override fun onNoDoubleClick(v: View?) {
            var url = ""
            var title = "加载中..."
            when (v?.id) {
                R.id.tv_gankio -> {
                    url = getString(R.string.string_url_gankio)
                    title = "干货集中营"
                }
                R.id.tv_douban -> {
                    url = getString(R.string.string_url_douban)
                    title = "豆瓣开发者服务使用条款"
                }
                R.id.tv_wanandroid -> {
                    url = getString(R.string.string_url_wanandroid)
                    title = "玩Android"
                }
                R.id.tv_download_url->{
                    url = getString(R.string.string_download)
                    title = "下载"
                }
                R.id.tv_about_star->{
                    url = getString(R.string.github)
                    title = getString(R.string.app_name)
                }
                R.id.tv_good_cloud->{
                    url = getString(R.string.string_url_new_version)
                    title = "云阅 - fir.im"
                }
            }
            WebViewActivity.loadUrl(this@AboutActivity, url, title)
        }
    }

    /**
     * 检查更新
     */
    fun checkUpdate(v: View) {
        tv_new_version.postDelayed({
            showToast("暂无更新~")
        }
        ,300)
    }

    companion object{
        fun start(context: Context) {
            context.startActivity(Intent(context,AboutActivity::class.java))
        }
    }
}
