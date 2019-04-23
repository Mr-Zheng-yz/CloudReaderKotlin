package com.yanze.cloudreaderkotlin.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yanze.cloudreaderkotlin.R

class ImageLoadUtil{
    companion object {

        /**
         * 加载原型图，用到显示头像
         */
        @JvmStatic
        fun displayCircle(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.context)
                    .load(imageUrl)
                    .crossFade(500)
                    .transform(GlideCircleTransform(imageView.context))
                    .error(R.drawable.ic_avatar_default)
                    .into(imageView)
        }

    }
}