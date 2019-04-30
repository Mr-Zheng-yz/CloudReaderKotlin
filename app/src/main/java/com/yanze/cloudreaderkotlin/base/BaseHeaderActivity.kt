package com.yanze.cloudreaderkotlin.base

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.transition.ArcMotion
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.test.StatusBarUtils
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.statusbar.StatusBarUtil
import com.yanze.cloudreaderkotlin.widget.CustomChangeBounds
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_header_base.*
import kotlinx.android.synthetic.main.activity_header_base.view.*
import kotlinx.android.synthetic.main.base_header_title_bar.view.*
import kotlinx.android.synthetic.main.layout_loading_view.view.*
import java.lang.Exception

abstract class BaseHeaderActivity : AppCompatActivity() {

    protected lateinit var mContentView: View
    protected lateinit var mHeaderView: View
    private lateinit var mTitleView: View

    private lateinit var loadingView: View
    private lateinit var refreshView: View
    // 滑动多少距离后标题不透明
    private var slidingDistance: Int = 0
    // 这个是高斯图背景的高度
    private var imageBgHeight: Int = 0
    // 清除动画，防止内存泄漏
    private var changeBounds: CustomChangeBounds? = null
    private var mAnimationDrawable: AnimationDrawable? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
    //        super.setContentView(layoutResID)
        //父布局
        val ll: View = layoutInflater.inflate(R.layout.activity_header_base, null)

        //标题（父类自定义好的容器）
        mTitleView = layoutInflater.inflate(R.layout.base_header_title_bar, null)
        val titleParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mTitleView.layoutParams = titleParams
        ll.title_container.addView(mTitleView)
//        window.setContentView(ll)

        //头部（拿到子类的HeaderView）
        mHeaderView = layoutInflater.inflate(setHeaderLayout(), null)
        val headerParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mHeaderView.layoutParams = headerParams
        ll.header_container.addView(mHeaderView)
//        window.setContentView(ll)

        //内容（子类内容）
        mContentView = layoutInflater.inflate(layoutResID, null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mContentView.layoutParams = params
        ll.container.addView(mContentView)
//        window.setContentView(ll)
        setContentView(ll)

        //错误布局与加载布局
        loadingView = vs_loading.inflate()
        refreshView = vs_error_refresh.inflate()
        refreshView.visibility = View.GONE

        //设置toolbar
        setToolbar()

        //加载动画（帧动画）
        mAnimationDrawable = loadingView.img_progress.drawable as AnimationDrawable
        //默认进入界面就开启动画
        if (!mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.start()
        }
        //为加载失败布局，设置点击事件
        refreshView.setOnClickListener {
            showLoading()
            onRefresh()
        }
        mContentView.visibility = View.GONE
    }

    /**
     * a. 布局高斯透明图 header布局,获取子类HeaderLayout布局Id
     */
    protected abstract fun setHeaderLayout(): Int

    /**
     * b. 设置头部header高斯背景 imgUrl
     */
    protected abstract fun setHeaderImgUrl(): String

    /**
     * c. 设置头部header布局 高斯背景ImageView控件
     */
    protected abstract fun setHeaderImageView(): ImageView

    /**
     * 设置头部header布局 左侧的图片(需要设置曲线路径切换动画时重写)
     */
    protected fun setHeaderPicView(): ImageView {
        return ImageView(this)
    }

    /**
     * 1. 标题
     */
    override fun setTitle(text: CharSequence) {
        mTitleView.tb_base_title.setTitle(text)
    }

    /**
     * 2. 副标题
     */
    protected fun setSubTitle(text: CharSequence) {
        mTitleView.tb_base_title.subtitle = text
    }

    /**
     * 3. toolbar 单击“更多信息”
     */
    protected open fun setTitleClickMore() {}

    /**
     * 设置自定义 Shared Element切换动画
     * 默认不开启曲线路径切换动画，
     * 开启需要重写setHeaderPicView()，和调用此方法并将isShow值设为true
     *
     * @param imageView 共享的图片
     * @param isShow    是否显示曲线动画
     */
    protected fun setMotion(imageView: ImageView, isShow: Boolean) {
        if (!isShow) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            val arcMotion = ArcMotion()
            //设置曲线幅度
            arcMotion.minimumHorizontalAngle = 10f
            arcMotion.minimumVerticalAngle = 10f
            //插值器 控制速度
            val interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in)

            //实例化自定义的ChangeBounds
            changeBounds = CustomChangeBounds()
            changeBounds?.pathMotion = arcMotion
            changeBounds?.interpolator = interpolator
            changeBounds?.addTarget(imageView)
            //将切换动画应用到当前Activity的进入和返回
            window.sharedElementEnterTransition = changeBounds
            window.sharedElementReturnTransition = changeBounds
        }
    }


    /**
     * 初始化toolbar
     */
    private fun setToolbar() {
        setSupportActionBar(mTitleView.tb_base_title)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.icon_back)
        mTitleView.tb_base_title.overflowIcon = ContextCompat.getDrawable(this, R.drawable.actionbar_more)
        mTitleView.tb_base_title.setNavigationOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//LOLLIPOP以上才支持共享元素
                finishAfterTransition()
            } else {
                finish()
            }
        }
        mTitleView.tb_base_title.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.actionbar_more -> setTitleClickMore()
            }
            return@setOnMenuItemClickListener false
        }
    }

    /**
     * 显示popu内的图片(反射)
     */
//    @SuppressLint("RestrictedApi")
//    override fun onPrepareOptionsPanel(view: View, menu: Menu?): Boolean {
//        if (menu != null) {
//            if (menu.javaClass.simpleName == "MenuBuilder") {
//                try {
//                    val m = menu.javaClass.getDeclaredMethod(
//                            "setOptionalIconsVisible", java.lang.Boolean.TYPE)
//                    m.isAccessible = true
//                    m.invoke(menu, true)
//                } catch (e: Exception) {
//                    Log.e("yanze", "onMenuOpened...unable to set icons for overflow menu", e)
//                }
//
//            }
//        }
//        return super.onPrepareOptionsPanel(view, menu)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.base_header_menu, menu)
        return true
    }

    /**
     * 初始化滑动渐变，一定要实现
     *
     * @param imgUrl header头部的高斯背景imageUrl
     * @param mHeaderBg header头部高斯背景ImageView控件
     */
    protected fun initSlideShapeTheme(imgUrl: String, mHeaderBg: ImageView) {
        setImgHeaderBg(imgUrl)

        //toolbar的高
        val toolbarHeight = mTitleView.tb_base_title.layoutParams.height
        val headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this)

        //使背景向上移动到图片的最底端，保留（titlebar+statusbar）的高度
        val params = mTitleView.iv_base_titlebar_bg.layoutParams
        val ivTitleHeadBgParams = mTitleView.iv_base_titlebar_bg.layoutParams as ViewGroup.MarginLayoutParams
        val marginTop = params.height - headerBgHeight
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0)

        mTitleView.iv_base_titlebar_bg.imageAlpha = 0
        StatusBarUtils.setTranslucentImageHeader(this, 0, mTitleView.tb_base_title)

        //上移图片，是空白状态栏消失（这样下方就空了状态栏的高度）
        val layoutParams = mHeaderBg.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0)

        val imgItemBgParams = mHeaderBg.layoutParams
        //获得高斯图背景高度
        imageBgHeight = imgItemBgParams.height

        //变色
        initScrollViewListener()
        initNewSlidingParams()
    }

    /**
     * 加载titlebar背景
     */
    private fun setImgHeaderBg(imgUrl: String) {
        if (!TextUtils.isEmpty(imgUrl)) {
            //高斯模糊背景
            Glide.with(this)
                    .load(imgUrl)
                    .bitmapTransform(BlurTransformation(this, 23, 4))
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            mTitleView.tb_base_title.setBackgroundColor(Color.TRANSPARENT)
                            mTitleView.iv_base_titlebar_bg.imageAlpha = 0
                            mTitleView.iv_base_titlebar_bg.visibility = View.VISIBLE
                            return false
                        }
                    })
                    .into(mTitleView.iv_base_titlebar_bg)
        }
    }

    private fun initScrollViewListener() {
        //兼容23以下滚动监听
        mns_base.setOnScrollChangeListener { x, y, oldX, oldY ->
            scrollChangeHeader(y)
        }
    }

    private fun initNewSlidingParams() {
        val titleBarAndStatusHeight = CommonUtils.getDimens(R.dimen.nav_bar_height) + StatusBarUtil.getStatusBarHeight(this)
        slidingDistance = (imageBgHeight - titleBarAndStatusHeight - CommonUtils.getDimens(R.dimen.base_header_activity_slide_more)).toInt()
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private fun scrollChangeHeader(scrolledY: Int) {
        var y = scrolledY   //方法参数默认val类型，不可修改
        if (y < 0) {
            y = 0
        }
        val alpha = Math.abs(y) * 1f / (slidingDistance)
        val drawable = mTitleView.iv_base_titlebar_bg.drawable
        if (y <= slidingDistance) {
            //title部分的渐变
            drawable.mutate().alpha = (alpha * 255).toInt()
            mTitleView.iv_base_titlebar_bg.setImageDrawable(drawable)
        } else {
            drawable.mutate().alpha = 255
            mTitleView.iv_base_titlebar_bg.setImageDrawable(drawable)
        }
    }

    protected fun showLoading() {
        if (loadingView.visibility != View.VISIBLE) {
            loadingView.visibility = View.VISIBLE
        }
        if (!mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.start()
        }
        if (mContentView.visibility != View.GONE) {
            mContentView.visibility = View.GONE
        }
        if (refreshView.visibility != View.GONE) {
            refreshView.visibility = View.GONE
        }
    }

    protected fun showContentView() {
        if (loadingView.visibility != View.GONE) {
            loadingView.visibility = View.GONE
        }
        if (mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.stop()
        }
        if (mContentView.visibility != View.VISIBLE) {
            mContentView.visibility = View.VISIBLE
        }
        if (refreshView.visibility != View.GONE) {
            refreshView.visibility = View.GONE
        }
    }

    protected fun showError() {
        if (loadingView.visibility != View.GONE) {
            loadingView.visibility = View.GONE
        }
        if (mAnimationDrawable?.isRunning!!) {
            mAnimationDrawable?.stop()
        }
        if (mContentView.visibility != View.GONE) {
            mContentView.visibility = View.GONE
        }
        if (refreshView.visibility != View.VISIBLE) {
            refreshView.visibility = View.VISIBLE
        }
    }

    /**
     * 失败后点击刷新
     */
    open fun onRefresh() {
    }

    protected fun addSubscription(d: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(d)
    }

    override fun onDestroy() {
        mCompositeDisposable?.clear()
        changeBounds?.addListener(null)
        changeBounds?.removeTarget(setHeaderPicView())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementEnterTransition = null
            window.sharedElementReturnTransition = null
        }
        mAnimationDrawable?.stop()
        mAnimationDrawable = null
        super.onDestroy()
    }
}