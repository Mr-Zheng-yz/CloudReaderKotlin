package com.yanze.cloudreaderkotlin.ui.mtime

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.FilmAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_banner.view.*

class FilmShowingFragment : BaseFragment() {

    private var isFirst = true
    private var isPrepored = false
    private lateinit var viewModel: MtimeViewModel
    private lateinit var adapter: FilmAdapter

    override fun setContent(): Int = R.layout.fragment_banner

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMTimeViewModelFactory(context))
                .get(MtimeViewModel::class.java)
        initView()
        showContentView()
        isPrepored = true
        loadData()
    }

    private fun initView() {
        //refresh
        childView.srl_wan.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        childView.srl_wan.setOnRefreshListener {
            childView.srl_wan.postDelayed({
                childView.xrv_wan.reset()
                loadShowingData()
            }, 150)
        }

        //recyclerView
        adapter = FilmAdapter()
        childView.xrv_wan.layoutManager = LinearLayoutManager(context)
        childView.xrv_wan.setPullRefreshEnabled(false)
        childView.xrv_wan.itemAnimator = null
        childView.xrv_wan.clearHeader()
        childView.xrv_wan.setHasFixedSize(true)
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
        if (!isPrepored || !isFirst) {
            return
        }
        childView.srl_wan.isRefreshing = true
        childView.postDelayed({
            loadShowingData()
        }, 360)
    }

    private fun loadShowingData() {
        viewModel.getHotFilm().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    childView.srl_wan.isRefreshing = false
                    adapter.clear()
                    val positionStart = adapter.itemCount + 1
                    adapter.addAll(it.data?.ms!!)
                    adapter.notifyItemRangeInserted(positionStart, it.data.ms.size)
                    childView.xrv_wan.reset()
//                    adapter.notifyDataSetChanged()
                    isFirst = false
                }
                Resource.ERROR -> {
                    childView.srl_wan.isRefreshing = false
                    if (adapter.itemCount == 0) {
                        showError()
                    }
                    context?.showToast("${it.message}")
                }
            }
        })
    }

    override fun onRefresh() {
        loadShowingData()
    }

    companion object{
        fun getInstance(type:String) = FilmShowingFragment()
    }

}