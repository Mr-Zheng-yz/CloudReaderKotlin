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

        /**
         * 热门电影头部图片
         */
        fun displayRandom(iv: ImageView, imgResource: Int) {
            Glide.with(iv.context)
                    .load(imgResource)
                    .placeholder(getMusicDefaultPic(3))
                    .error(getMusicDefaultPic(3))
                    .crossFade(500)
                    .into(iv)
        }

        private fun getDefaultPic(type: Int): Int {
            return when (type) {
                0 -> R.drawable.img_default_movie
                1 -> R.drawable.img_default_meizi
                2 -> R.drawable.img_default_book
                3 -> R.drawable.shape_bg_loading
                else -> R.drawable.img_default_meizi
            }
        }

        private fun getMusicDefaultPic(imgNumber: Int): Int {
            return when (imgNumber) {
                1 -> R.drawable.img_two_bi_one
                2 -> R.drawable.img_four_bi_three
                3 -> R.drawable.img_one_bi_one
                4 -> R.drawable.shape_bg_loading
                else -> R.drawable.img_four_bi_three
            }
        }

    }
}