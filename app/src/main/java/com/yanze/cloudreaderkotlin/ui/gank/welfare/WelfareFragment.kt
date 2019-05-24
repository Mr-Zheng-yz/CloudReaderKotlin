package com.yanze.cloudreaderkotlin.ui.gank.welfare

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.WelfareAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import kotlinx.android.synthetic.main.fragment_welfare.*

class WelfareFragment : BaseFragment() {

    private var isPrepared = false
    private var isFirst = true
    private lateinit var viewModel: WelfareViewModel
    private lateinit var adapter: WelfareAdapter

    override fun setContent() = R.layout.fragment_welfare

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getWelfFactory(context))
                .get(WelfareViewModel::class.java)
        initRecyclerView()
        isPrepared = true
        loadData()
    }

    private fun initRecyclerView() {
        adapter = WelfareAdapter(context!!)
        xrv_welfare.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        xrv_welfare.setPullRefreshEnabled(false)
        xrv_welfare.setLoadingMoreEnabled(true)
        xrv_welfare.adapter = adapter
        xrv_welfare.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewModel.handleNextPage()
                loadWelfareData()
            }
        })
    }

    override fun loadData() {
        if (!isPrepared || !isFirst) {
            return
        }
        loadWelfareData()
    }

    private fun loadWelfareData() {
        if (viewModel.welfareResult.isEmpty() || adapter.itemCount == viewModel.welfareResult.size) { //去请求新数据
            viewModel.getWelfareImage().observe(this, Observer { result ->
                when (result.state) {
                    Resource.LOADING -> {
                        if (viewModel.page == 1) {
                            showLoading()
                        }
                    }
                    Resource.SUCCESS -> {//加载成功
                        updateUi(result.data?.results!!)
                        result.data.results.let { viewModel.welfareResult.addAll(it) }
                    }
                    Resource.ERROR -> {
                        if (adapter.getData().isEmpty()) {
                            showError()
                        } else {
                            xrv_welfare.noMoreLoading()
                        }
                    }
                }
            })
        }else{
            updateUi(viewModel.welfareResult)
        }
    }

    private fun updateUi(gankResult: List<GankResult>) {
        if (viewModel.page == 1) {
            adapter.clear()
//            adapter.notifyItemRangeRemoved(0,adapter.itemCount)
        }
//        val positionStart = adapter.itemCount + 1
        adapter.addAll(gankResult)
//        adapter.notifyItemRangeInserted(positionStart, gankResult.size)
        adapter.notifyDataSetChanged()
        xrv_welfare.refreshComplete()
        showContentView()
    }

    override fun onRefresh() {
        loadWelfareData()
    }

    companion object {
        @JvmStatic
        fun getInstance() = WelfareFragment()
    }
}