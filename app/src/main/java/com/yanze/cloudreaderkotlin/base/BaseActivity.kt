package com.yanze.cloudreaderkotlin.base

import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.PerfectClickListener
import com.yanze.cloudreaderkotlin.utils.statusbar.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_base.view.*
import kotlinx.android.synthetic.main.layout_loading_view.view.*

abstract class BaseActivity : AppCompatActivity() {

    //父布局
    private lateinit var mParentView:View
    //内容
    private lateinit var mContentView: View
    //loading
    private lateinit var loadingView: View
    //error
    private var errorView: View? = null
    private lateinit var mAnimationDrawable: AnimationDrawable
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun setContentView(layoutResID: Int) {
        mParentView = layoutInflater.inflate(R.layout.activity_base, null)

        // 内容content
        mContentView = layoutInflater.inflate(layoutResID, null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mContentView.layoutParams = params
        mParentView.container.addView(mContentView)
        window.setContentView(mParentView)

        //设置透明状态栏，兼容4.4
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme))

        loadingView = mParentView.vs_loading.inflate()
        mAnimationDrawable = loadingView.img_progress.drawable as AnimationDrawable
        //默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning) {
            mAnimationDrawable.start()
        }

        setToolbar()
        mContentView.visibility = View.GONE
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
    }

    private fun setToolbar() {
        setSupportActionBar(mParentView.tool_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.icon_back)

        mParentView.tool_bar.setNavigationOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            }else{
                onBackPressed()
            }
        }
    }

    override fun setTitle(text: CharSequence) {
        mParentView.tool_bar.title = text
    }

    protected fun showLoading() {
        if (loadingView.visibility != View.VISIBLE) {
            loadingView.visibility = View.VISIBLE
        }
        if (!mAnimationDrawable.isRunning) {
            mAnimationDrawable.start()
        }
        if (mContentView.visibility != View.GONE) {
            mContentView.visibility = View.GONE
        }
        errorView?.visibility = View.GONE
    }

    protected fun showContentView() {
        if (loadingView.visibility != View.GONE) {
            loadingView.visibility = View.GONE
        }
        if (mAnimationDrawable.isRunning) {
            mAnimationDrawable.stop()
        }
        if (mContentView.visibility != View.VISIBLE) {
            mContentView.visibility = View.VISIBLE
        }
        errorView?.visibility = View.GONE
    }

    protected fun showError() {
        if (loadingView.visibility != View.GONE) {
            loadingView.visibility = View.GONE
        }
        if (mAnimationDrawable.isRunning) {
            mAnimationDrawable.stop()
        }
        if (mContentView.visibility != View.GONE) {
            mContentView.visibility = View.GONE
        }
        if (errorView == null) {
            errorView = mParentView.vs_error_refresh.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener(object : PerfectClickListener() {
                override fun onNoDoubleClick(v: View) {
                    showLoading()
                    onRefresh()
                }
            })
        }else{
            errorView?.visibility = View.VISIBLE
        }
    }

    protected open fun onRefresh(){}

    protected fun addSubscription(d: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(d)
    }

    protected fun removeDisposable(){
        mCompositeDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

}