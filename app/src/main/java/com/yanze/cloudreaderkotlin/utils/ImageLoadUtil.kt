package com.yanze.cloudreaderkotlin.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yanze.cloudreaderkotlin.R
import jp.wasabeef.glide.transformations.BlurTransformation

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

        /**
         * 电影详情页，显示导演与演员图片
         * 没有加载中的图
         */
        fun showPersonImg(imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                    .load(url)
                    .crossFade(500)
                    .error(getDefaultPic(0))
                    .into(imageView)
        }

        /**
         * 加载列表图片
         * @type 0:电影  1：妹子  2.书籍
         */
        fun displayListImage(imageView: ImageView,url:String,type:Int){
            Glide.with(imageView.context)
                    .load(url)
                    .crossFade(500)
                    .placeholder(getDefaultPic(type))
                    .error(getDefaultPic(type))
                    .into(imageView)
        }

        /**
         * 加载Gif图(Gif被转换成静态图来加载)
         */
        fun displayGif(imageView: ImageView,url: String) {
            Glide.with(imageView.context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.shape_bg_loading)
                    .error(R.drawable.shape_bg_loading)
                    .into(imageView)
        }

        /**
         * 显示高斯模糊效果（电影详情头布局）
         */
        fun displayGaussFuzzy(imageView: ImageView, url: String) {
            // "23":模糊度；"4":图片缩放4倍后再进行模糊
            Glide.with(imageView.context)
                    .load(url)
                    .error(R.drawable.stackblur_default)
                    .placeholder(R.drawable.stackblur_default)
                    .crossFade(500)
                    .bitmapTransform(BlurTransformation(imageView.context,23,4))
                    .into(imageView)
        }

        /**
         * 加载固定宽高图片
         */
//        fun displayFormatImg(imageView:ImageView,imageUrl: String,imgWidthDp:Int,imgHeightDp:Int){
//            Glide.with(imageView.context)
//                    .load(imageUrl)
//                    .override(DensityUtil.dip2px(imgWidthDp.toFloat()), DensityUtil.dip2px(imgHeightDp.toFloat()))
//                    .transition()
//        }

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