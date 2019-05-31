package com.yanze.cloudreaderkotlin.ui.mtime

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.FilmComingAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.film.ComingFilmeBean
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_banner.view.*

class FilmComingFragment : BaseFragment() {

    private lateinit var viewModel: MtimeViewModel
    private lateinit var adapter: FilmComingAdapter
    private var isFirst = true
    private var isPrepared = false

    override fun setContent(): Int = R.layout.fragment_banner

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMTimeViewModelFactory(context))
                .get(MtimeViewModel::class.java)
        initView()
        showContentView()
        isPrepared = true
    }

    private fun initView() {
        //refresh
        childView.srl_wan.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        childView.srl_wan.setOnRefreshListener {
            childView.srl_wan.postDelayed({
                childView.xrv_wan.reset()
                loadComingData()
            }, 120)
        }

        //recyclerView
        adapter = FilmComingAdapter()
        childView.xrv_wan.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        childView.xrv_wan.setPullRefreshEnabled(false)
        childView.xrv_wan.setLoadingMoreEnabled(true)
        childView.xrv_wan.itemAnimator = null
        childView.xrv_wan.clearHeader()
        childView.xrv_wan.setHasFixedSize(false)
        childView.xrv_wan.adapter = adapter
        childView.xrv_wan.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                childView.xrv_wan.noMoreLoading()
            }

            override fun onRefresh() {
            }
        })
    }

    override fun loadData() {
        if (!isFirst || !isPrepared) {
            return
        }
        childView.srl_wan.isRefreshing = true
        childView.srl_wan.postDelayed({
            loadComingData()
        }, 360)
    }

    private fun loadComingData() {
        viewModel.getComingFilm().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    childView.srl_wan.isRefreshing = false
                    val beans = ArrayList<ComingFilmeBean>()
                    beans.addAll(it.data?.attention!!)
                    beans.addAll(it.data.moviecomings)
                    // 用HashSet 根据id去重 https://www.cnblogs.com/woshimrf/p/java-list-distinct.html
                    val set = LinkedHashSet<ComingFilmeBean>(beans)
                    beans.clear()
                    beans.addAll(set)

                    val positionStart = adapter.itemCount + 1
                    adapter.addAll(beans)
                    // +1 一个刷新头布局
                    adapter.notifyItemRangeInserted(positionStart,beans.size)
                    isFirst = false
                }
                Resource.ERROR -> {
                    childView.srl_wan.isRefreshing = false
                    showError()
                    context?.showToast("${it.message}")
                }
            }
        })
    }

    override fun onRefresh() {
        loadComingData()
    }

    companion object {
        fun getInstance(type: String) = FilmComingFragment()
    }
}