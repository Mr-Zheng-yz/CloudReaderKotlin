package com.yanze.cloudreaderkotlin.view.webview.config

import android.content.Context
import android.text.TextUtils
import android.webkit.JavascriptInterface
import com.baize.fireeyekotlin.utils.log.L

class ImageClickInterface(val context: Context) {

    @JavascriptInterface
    fun imageClick(imageUrl: String, hasLink: String) {
        // 查看大图
//        Intent intent = new Intent(context, ViewBigImageActivity.class);
//        context.startActivity(intent);
        L.i(msg = "----点击了图片 url: $imageUrl")
    }

    @JavascriptInterface
    fun textClick(type: String, item_pk: String) {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
            L.i(msg = "----点击了文字: type:$type item_pk:$item_pk")
        }
    }


}