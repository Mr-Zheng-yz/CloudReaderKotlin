package com.yanze.cloudreaderkotlin.view.viewbigimage

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.utils.CheckNetwork
import com.yanze.cloudreaderkotlin.utils.PermissionHandler
import com.yanze.cloudreaderkotlin.utils.RxSaveImage
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.activity_view_big_image.*
import kotlinx.android.synthetic.main.viewpager_very_image.view.*
import java.lang.Exception
import java.util.ArrayList

class ViewBigImageActivity : AppCompatActivity(), OnPhotoTapListener, ViewPager.OnPageChangeListener {

    //* 接收传过来的uri地址
    private var imageList: MutableList<String>? = null
    //* 图片标题（可摒弃）
    private var imageTitles: MutableList<String>? = null
    //* 传过来展示的是第几个图片
    private var code: Int = 0
    //* 1:单张图片 2：图片集合
    private var selet: Int = 0
    //* 当前页数
    private var page: Int = 0
    //* 本应用图片的id
    private var imageId: Int = 0
    //* 是否是本应用中的图片（暂未用到）
    private var isApp: Boolean = false
    //* 用于判断是否是加载本地图片（暂未用到）
    private var isLocal: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_big_image)
        initView()
        getIntentData()
    }

    private fun getIntentData() {
        val bundle = intent.extras
        if (bundle != null) {
            code = bundle.getInt("code")
            selet = bundle.getInt("selet")
            isLocal = bundle.getBoolean("isLocal", false)
            imageList = bundle.getStringArrayList("imageList")
            imageTitles = bundle.getStringArrayList("imageTitles")
            isApp = bundle.getBoolean("isApp", false)
            imageId = bundle.getInt("id", 0)
        }

        if (isApp) {//本地图片
            val myAdapter = MyPageAdapter()
            very_image_viewpager.adapter = myAdapter
            very_image_viewpager.isEnabled = false
        } else {
            val adapter = ViewPagerAdapter()
            very_image_viewpager.adapter = adapter
            very_image_viewpager.currentItem = code
            page = code
            very_image_viewpager.addOnPageChangeListener(this@ViewBigImageActivity)
            very_image_viewpager.isEnabled = false
            //设定当前的页数和总页数
            if (selet == 2) {
                very_image_viewpager_text.text = "${code + 1} / ${imageList?.size}"
            }
        }
    }

    private fun initView() {
        tv_save_big_image.setOnClickListener {
            if (!CheckNetwork.isNetworkConnected(this@ViewBigImageActivity)) {
                showToast("当前网络不可用，请检查你的网络设置")
                return@setOnClickListener
            }
            if (!PermissionHandler.isHandlePermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return@setOnClickListener
            }
            if (isApp) {
                showToast("图片已存在")
            } else {
                //网络图片
                RxSaveImage.saveImageToGallery(this@ViewBigImageActivity,"${imageList?.get(page)}","${imageTitles?.get(page)}")
            }
        }
    }

    override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
        finish()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        very_image_viewpager_text.text = "${position + 1} / ${imageList?.size}"
        page = position
    }

    override fun onDestroy() {
        imageList?.clear()
        imageList = null
        imageTitles?.clear()
        imageTitles = null
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionHandler.onRequestPermissionsResult("存储权限被拒绝，请到设置中开启", requestCode, permissions, grantResults, null)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 本应用图片适配器
     */
    inner class MyPageAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return 1
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = this@ViewBigImageActivity.layoutInflater.inflate(R.layout.viewpager_very_image, container, false)
            view.loading.visibility = View.GONE
            if (imageId != 0) {
                view.zoom_image_view.setImageResource(imageId)
            }
            view.zoom_image_view.setOnPhotoTapListener(this@ViewBigImageActivity)
            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    /**
     * ViewPager的适配器
     */
    inner class ViewPagerAdapter : PagerAdapter() {

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = if (imageList == null || imageList!!.isEmpty()) 0 else imageList!!.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = this@ViewBigImageActivity.layoutInflater.inflate(R.layout.viewpager_very_image, container,false)
            //保存网络图片的路径
            val adapterImageEntity = getItem(position)
            val imageUrl = if (isLocal) "file://$adapterImageEntity" else "$adapterImageEntity"

            view.loading.visibility = View.VISIBLE
            view.loading.isClickable = false
            Glide.with(this@ViewBigImageActivity).load(imageUrl)
                    .crossFade(700)
                    .listener(object : RequestListener<String, GlideDrawable> { //监听图片加载是否完成
                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            view.loading.visibility = View.GONE
                            //获取图片加载成功真实高度
                            val height = view.zoom_image_view.height
                            val wHeight = windowManager.defaultDisplay.height //屏幕高度
                            if (height > wHeight) {
                                view.zoom_image_view.scaleType = ImageView.ScaleType.CENTER_INSIDE
                            } else {
                                view.zoom_image_view.scaleType = ImageView.ScaleType.FIT_CENTER
                            }
                            return false
                        }

                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            this@ViewBigImageActivity.showToast("资源加载异常")
                            view.loading.visibility = View.GONE
                            return false
                        }
                    })
                    .into(view.zoom_image_view)
            view.zoom_image_view.setOnPhotoTapListener(this@ViewBigImageActivity)
            container.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

        private fun getItem(position: Int) = imageList?.get(position)
    }

    companion object {
        /**
         * selet： 图片类型 1：头像，不显示页数  2：大图显示当前页数
         *
         * @param position    大图的话是第几张图片 从0开始
         * @param imageList   图片集合
         * @param imageTitles 图片标题集合
         */
        fun start(context: Context, position: Int, imageList: ArrayList<String>, imageTitles: ArrayList<String>) {
            val bundle = Bundle()
            bundle.putInt("selet", 2)
            bundle.putInt("code", position)
            bundle.putStringArrayList("imageList", imageList)
            bundle.putStringArrayList("imageTitles", imageTitles)
            val intent = Intent(context, ViewBigImageActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        /**
         * 查看单张图片
         */
        fun start(context: Context, imageUrl: String,imageTitle: String) {
            val bundle = Bundle()
            bundle.putInt("selet", 1)
            bundle.putInt("code", 0)
            bundle.putStringArrayList("imageList", arrayListOf(imageUrl))
            bundle.putStringArrayList("imageTitles", arrayListOf(imageTitle))
            val intent = Intent(context, ViewBigImageActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}
