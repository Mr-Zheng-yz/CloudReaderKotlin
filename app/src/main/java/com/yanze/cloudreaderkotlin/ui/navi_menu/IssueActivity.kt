package com.yanze.cloudreaderkotlin.ui.navi_menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.base.BaseActivity
import com.yanze.cloudreaderkotlin.utils.BaseTools
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.PerfectClickListener
import com.yanze.cloudreaderkotlin.utils.ToastUtil
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_issue.*

class IssueActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)
        setTitle("问题反馈")
        showContentView()

        tv_issues.setOnClickListener(listener)
        tv_qq.setOnClickListener(listener)
        tv_email.setOnClickListener(listener)
        tv_csdn.setOnClickListener(listener)
    }

    private val listener = object : PerfectClickListener() {
        override fun onNoDoubleClick(v: View?) {
            when (v?.id) {
                R.id.tv_issues -> {
                    WebViewActivity.loadUrl(this@IssueActivity,getString(R.string.my_issue),"Issues")
                }
                R.id.tv_qq -> {
                    if (BaseTools.isApplicationAvilible(this@IssueActivity, "com.tencent.mobileqq")) {
                        val url = "mqqwpa://im/chat?chat_type=wpa&uin=646929482"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    } else {
                        ToastUtil.showToastLong("先安装一个QQ吧..")
                    }
                }
                R.id.tv_email -> {
                    try {
                        val data = Intent(Intent.ACTION_SENDTO)
                        data.data = Uri.parse("mailto:646929482@qq.com")
                        startActivity(data)
                    } catch (e: Exception) {
                        ToastUtil.showToastLong("请先安装邮箱~")
                    }

                }
                R.id.tv_csdn -> {
                    WebViewActivity.loadUrl(v.context, CommonUtils.getString(R.string.my_csdn), "CSDN")
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context,IssueActivity::class.java))
        }
    }
}
