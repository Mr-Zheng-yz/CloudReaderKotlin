package com.yanze.cloudreaderkotlin.ui.navi_menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.base.BaseActivity
import com.yanze.cloudreaderkotlin.utils.QRCodeUtil
import com.yanze.cloudreaderkotlin.utils.ShareUtils
import kotlinx.android.synthetic.main.activity_scan_down.*

class ScanDownActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_down)
        showContentView()

        setTitle("扫码下载")
        QRCodeUtil.showThreadImage(this@ScanDownActivity, Constants.DOWNLOAD_URL, iv_erweima, R.drawable.ic_cloudreader_mip)
        tv_share.setOnClickListener {
            ShareUtils.share(this@ScanDownActivity, R.string.string_share_text)
        }
    }

    companion object {
        fun start(context:Context){
            context.startActivity(Intent(context,ScanDownActivity::class.java))
        }
    }

}
