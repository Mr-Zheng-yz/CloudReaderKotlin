package com.yanze.cloudreaderkotlin.ui.gank.android

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.GankDataAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import kotlinx.android.synthetic.main.fragment_android.*
import kotlinx.android.synthetic.main.fragment_android.view.*

class AndroidFragment : BaseFragment() {

    private var isFirst = true //第一次
    private var isPrepore = false //初始化完毕

    private lateinit var viewModel: GankViewModel
    private lateinit var adapter: GankDataAdapter

    override fun setContent() = R.layout.fragment_android

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getGankFactory(context))
                .get(GankViewModel::class.java)
        initView()
        isPrepore = true
    }

    private fun initView() {
        adapter = GankDataAdapter()
        childView.xrv_android.setPullRefreshEnabled(true)
        childView.xrv_android.setLoadingMoreEnabled(true)
        childView.xrv_android.layoutManager = LinearLayoutManager(context)
        childView.xrv_android.adapter = adapter
        childView.xrv_android.xrv_android.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                viewModel.reset()
                loadAndroidData()
            }

            override fun onLoadMore() {
                viewModel.handleNextPage()
                loadAndroidData()
            }
        })
    }

    override fun loadData() {
        if (!isFirst || !isPrepore) {
            return
        }
        loadAndroidData()
    }

    private fun loadAndroidData() {
        //如果目前Adapter条目数与viewModel存在的条目数相等，就说明是老数据了。
        if (viewModel.gankResults.isEmpty() || viewModel.page != viewModel.lastPage) {
            viewModel.getAndroidData().observe(this, Observer {
                when (it.state) {
                    Resource.LOADING -> {
                    }
                    Resource.SUCCESS -> {
                        isFirst = false
                        updateUi(it?.data?.results!!)
                        viewModel.updateLastPage()
                    }
                    Resource.ERROR -> {
                        childView.xrv_android.refreshComplete()
                        if (adapter.itemCount == 0) {
                            showError()
                        } else {
                            childView.xrv_android.noMoreLoading()
                        }
                    }
                }
            })
        } else {
            updateUi(viewModel.gankResults)
        }
    }

    //更新Ui
    private fun updateUi(gankDatas: List<GankResult>) {
        showContentView()
        if (viewModel.page == 1) {
            adapter.clear()
        }
        adapter.addAll(gankDatas)
        adapter.notifyDataSetChanged()
        xrv_android.refreshComplete()
    }

    override fun onRefresh() {
        loadAndroidData()
    }

    companion object {
        fun getInstance() = AndroidFragment()
    }
}