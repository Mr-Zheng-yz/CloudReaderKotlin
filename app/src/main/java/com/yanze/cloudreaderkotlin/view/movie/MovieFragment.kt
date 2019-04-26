package com.yanze.cloudreaderkotlin.view.movie

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.google.android.material.tabs.TabLayout
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.MovieListAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.android.synthetic.main.header_item_move.view.*

class MovieFragment : BaseFragment() {
    private var isPrepared = false //初始化完成
    private var isFirst = true //第一次加载
    private lateinit var adapter: MovieListAdapter
    private lateinit var viewModel: MovieViewModel
    private var headerView: View? = null

    override fun setContent(): Int {
        return R.layout.fragment_movie
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMovieFactory())
                .get(MovieViewModel::class.java)
        initRecyclerView()
        isPrepared = true
        loadData()
    }

    private fun initRecyclerView() {
        headerView = layoutInflater.inflate(R.layout.header_item_move, null)
        childView.rv_movie.layoutManager = LinearLayoutManager(context)
        childView.rv_movie.setPullRefreshEnabled(false)
        childView.rv_movie.clearHeader()
        childView.rv_movie.itemAnimator = null
        childView.rv_movie.addHeaderView(headerView)
        adapter = MovieListAdapter(context)
        childView.rv_movie.adapter = adapter
        childView.rv_movie.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                when (headerView?.tl_movie?.selectedTabPosition) {
                    1 -> {
                        viewModel.handleNextStart()
                        loadComingSoonMovie()
                    }
                    0 -> {
                        childView.rv_movie.noMoreLoading()
                    }
                }
            }

            override fun onRefresh() {
            }
        })
        headerView?.tl_movie?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (headerView?.tl_movie?.selectedTabPosition) {
                    0 -> {
                        viewModel.start = 0
                    }
                    1 -> {

                    }
                }
            }
        })
    }

    override fun loadData() {
        if (!isPrepared || !mIsVisible || !isFirst) {
            return
        }
        activity?.showToast("加载数据...")
        loadHotMovie()
    }

    //获取热映电影
    private fun loadHotMovie() = handleData(viewModel.getMovie()) {
        adapter.addAll(it.subjects)
    }

    //获取即将上映电影
    private fun loadComingSoonMovie() {
        handleData(viewModel.getComing()) {
            viewModel.start++
        }
    }

    private fun <T> handleData(liveData: LiveData<Resource<T>>
                               , action: (T) -> Unit) = liveData.observe(this, Observer { result ->
        if (result.state == Resource.LOADING) {
            showLoading()
        } else if (result.state == Resource.SUCCESS && result.data != null) {
            isFirst = false
            adapter.clear()
            action(result.data)
            adapter.notifyDataSetChanged()
            showContentView()
        } else {
            showError()
            context?.showToast("加载失败" + result.message)
        }
    })

    companion object {
        @JvmStatic
        fun getInstance() = MovieFragment()
    }
}