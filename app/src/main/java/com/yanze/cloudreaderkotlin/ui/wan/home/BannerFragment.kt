package com.yanze.cloudreaderkotlin.ui.wan.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.baize.fireeyekotlin.utils.log.L
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.WanArticleAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.WanBannerResultBean
import com.yanze.cloudreaderkotlin.data.bean.WanHomeResultBean
import com.yanze.cloudreaderkotlin.data.bean.wan.WanBannerBean
import com.yanze.cloudreaderkotlin.utils.*
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_banner.view.*
import kotlinx.android.synthetic.main.header_wan_home.view.*

class BannerFragment : BaseFragment() {

    private var isFirst = true
    private var isPrepore = false
    private lateinit var viewModel: BannerViewModel
    private lateinit var adapter: WanArticleAdapter
    private lateinit var headerView: View
    private var isLoadBanner = false

    private lateinit var mBanner:Banner

    override fun setContent(): Int = R.layout.fragment_banner

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getWanBannerFactory(context))
                .get(BannerViewModel::class.java)
        initView()
        isPrepore = true
        loadData()
    }

    private fun initView() {
        adapter = WanArticleAdapter()
        RecyclerViewHelper.initRefresh(childView.xrv_wan)
        childView.xrv_wan.adapter = adapter
        childView.xrv_wan.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                if (!childView.srl_wan.isRefreshing) {
                    viewModel.handleNextPage()
                    loadHomeData()
                } else {
                    childView.xrv_wan.refreshComplete()
                }
            }
        })

        //init headerView
        headerView = LayoutInflater.from(context).inflate(R.layout.header_wan_home, null)
        mBanner = headerView.findViewById(R.id.banner)
        DensityUtil.formatBannerHeight(mBanner, headerView.ll_banner_image)
        childView.xrv_wan.addHeaderView(headerView)

        //下拉刷新
        childView.srl_wan.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        childView.srl_wan.setOnRefreshListener {
            childView.srl_wan.postDelayed({
                viewModel.reset()
                childView.xrv_wan.reset()
                loadHomeData()
            }, 350)
        }
    }

    override fun loadData() {
        if (!isPrepore || !isFirst) {
            return
        }
        childView.srl_wan.isRefreshing = true
        childView.srl_wan.postDelayed({
            loadBanner()
            loadHomeData()
        }, 150)
    }

    private fun loadBanner() {
        viewModel.getHomeBanner().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    showBannerView(it.data!!)
                    headerView.rl_banner.visibility = View.VISIBLE
                }
                Resource.ERROR -> {
                    isLoadBanner = false
                    headerView.rl_banner.visibility = View.GONE
                }
            }
        })
    }

    /**
     * 设置banner图
     */
    private fun showBannerView(result: WanBannerResultBean) {
        val bannerImages = viewModel.handleBannerData<WanBannerBean>(result, action = {
            return@handleBannerData it.imagePath
        })
        val bannerTitle = viewModel.handleBannerData<WanBannerBean>(result, action = {
            return@handleBannerData it.title
        })
        L.i(msg = "$bannerImages")
        L.i(msg = "$bannerTitle")

        mBanner.setBannerTitles(bannerTitle)
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
        mBanner.setImages(bannerImages).setImageLoader(GlideImageLoader()).start()
        mBanner.setOnBannerListener {
            WebViewActivity.loadUrl(context!!,result.data[it].url,result.data[it].title)
        }
        val size = bannerImages.size
        var position1 = 0
        var position2 = 0
        if (size > 1) {
            position1 = size - 2
            position2 = size - 1
        }
        ImageLoadUtil.displayListImage(headerView.iv_banner_one, bannerImages[position1], 3)
        ImageLoadUtil.displayListImage(headerView.iv_banner_two, bannerImages[position2], 3)
        headerView.iv_banner_one.setOnClickListener {
            WebViewActivity.loadUrl(context!!,result.data[position1].url,result.data[position1].title)
        }
        headerView.iv_banner_two.setOnClickListener {
            WebViewActivity.loadUrl(context!!,result.data[position2].url,result.data[position2].title)
        }
        isLoadBanner = true
    }

    private fun loadHomeData() {
        viewModel.getHomeData().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    updateUi(it.data!!)
                    if (viewModel.page == 0) {
                        isFirst = false
                    }
                }
                Resource.ERROR -> {
                    if (viewModel.page == 0) {
                        showError()
                    } else {
                        childView.xrv_wan.refreshComplete()
                        childView.xrv_wan.noMoreLoading()
                    }
                    context?.showToast("${it.message}")
                }
            }
        })
    }

    private fun updateUi(homeResultBean: WanHomeResultBean) {
        if (childView.srl_wan.isRefreshing) {
            childView.srl_wan.isRefreshing = false
        }
        showContentView()
        if (viewModel.page == 0) {
            adapter.clear()
        }
        adapter.addAll(homeResultBean.data.datas)
        adapter.notifyDataSetChanged()
        childView.xrv_wan.refreshComplete()
    }

    override fun onRefresh() {
        if (!isLoadBanner) {
            loadBanner()
        }
        childView.srl_wan.isRefreshing = true
        childView.srl_wan.postDelayed({
            loadHomeData()
        }, 350)
    }

    override fun onResume() {
        super.onResume()
        if (isLoadBanner) {
            mBanner.startAutoPlay()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isLoadBanner) {
            mBanner.stopAutoPlay()
        }
    }

    companion object {
        fun getInstance() = BannerFragment()
    }

}