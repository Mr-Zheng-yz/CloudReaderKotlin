package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yanze.cloudreaderkotlin.R
import com.youth.banner.loader.ImageLoader

class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, url: Any?, imageView: ImageView?) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.shape_bg_loading)
                .error(R.drawable.shape_bg_loading)
                .crossFade(1000)
                .into(imageView)
    }
}