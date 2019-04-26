package com.yanze.cloudreaderkotlin.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yanze.cloudreaderkotlin.R

class ImageLoadUtil {
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

        /**
         * 电影列表图片
         */
        fun showMovieImg(image: ImageView, url: String) {
            Glide.with(image.context)
                    .load(url)
                    .crossFade(500)
                    .override(CommonUtils.getDimens(R.dimen.movie_detail_width).toInt()
                            , CommonUtils.getDimens(R.dimen.movie_detail_height).toInt())
                    .placeholder(getDefaultPic(0))
                    .error(getDefaultPic(0))
                    .into(image)
        }

        private fun getDefaultPic(type: Int): Int {
            when (type) {
                0 -> return R.drawable.img_default_movie
                1 -> return R.drawable.img_default_meizi
                2 -> return R.drawable.img_default_book
                3 -> return R.drawable.shape_bg_loading
                else -> return R.drawable.img_default_meizi
            }
        }
    }
}