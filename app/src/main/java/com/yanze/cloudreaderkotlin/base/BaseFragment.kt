package com.yanze.cloudreaderkotlin.base

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.yanze.cloudreaderkotlin.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_base.view.*
import kotlinx.android.synthetic.main.layout_loading_view.*

abstract class BaseFragment : Fragment() {

    // fragment是否显示了
    protected var mIsVisible = false
    // 加载中
    private var loadingView: View? = null
    // 加载失败
    private var errorView: View? = null
    // 动画
    private lateinit var mAnimationDrawable: AnimationDrawable
    private var mCompositeDisposable: CompositeDisposable? = null


    //子View
    protected lateinit var childView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base, container, false)
        childView = inflater.inflate(setContent(), null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        childView.layoutParams = params
        view.rl_container.addView(childView)
        return view
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

    //Fragment可见
    protected fun onVisible() {
        loadData()
    }

    //Fragment不可见
    protected fun onInvisible() {}

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     * 在onActivityCreated中初始化View之后，再调用loadData()
     * 子Fragment要定义isPrepared（是否准备就绪）与isFirst（是否第一次加载），防止多次加载数据
     * if (!mIsPrepared || !mIsVisible || !mIsFirst) {
     *  return;
     *  }
     */
    protected open fun loadData() {}

    /**
     * 布局
     */
    abstract fun setContent(): Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingView = vs_loading.inflate()
        mAnimationDrawable = img_progress.drawable as AnimationDrawable

        //默认进入界面就开启动画
        if (!mAnimationDrawable.isRunning) {
            mAnimationDrawable.start()
        }

        childView.visibility = View.GONE
//        initViewModel()
    }


    /**
     * 加载失败后点击后的操作
     */
    open fun onRefresh() {
    }

    /**
     * 显示加载中状态
     */
    protected fun showLoading() {
        if (loadingView?.visibility != View.VISIBLE)
            loadingView?.visibility = View.VISIBLE
        // 开始动画
        if (!mAnimationDrawable.isRunning)
            mAnimationDrawable.start()
        if (childView.visibility != View.GONE)
            childView.visibility = View.GONE
        errorView?.visibility = View.GONE
    }

    /**
     * 显示内容
     */
    protected fun showContentView() {
        if (loadingView?.visibility != View.GONE)
            loadingView?.visibility = View.GONE
        if (mAnimationDrawable.isRunning)
            mAnimationDrawable.stop()
        if (childView.visibility != View.VISIBLE)
            childView.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    /**
     * 加载失败需要点击重新加载的状态
     */
    protected fun showError() {
        if (loadingView?.visibility != View.GONE)
            loadingView?.visibility = View.GONE
        if (mAnimationDrawable.isRunning)
            mAnimationDrawable.stop()
        if (childView.visibility != View.GONE)
            childView.visibility = View.GONE
        if (errorView == null) {
            errorView = vs_error_refresh.inflate()
            errorView?.setOnClickListener {
                showLoading()
                onRefresh()
            }
        } else {
            errorView?.visibility = View.VISIBLE
        }
    }

    /**
     * 管理订阅事件
     */
    protected fun addSubscription(dispos: Disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(dispos)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCompositeDisposable != null) {
            if (mCompositeDisposable?.isDisposed!!) {
                mCompositeDisposable?.clear()
            }
        }
    }

    protected fun removeDisposable() {
        if (mCompositeDisposable?.isDisposed!!) {
            mCompositeDisposable?.dispose()
        }
    }
}