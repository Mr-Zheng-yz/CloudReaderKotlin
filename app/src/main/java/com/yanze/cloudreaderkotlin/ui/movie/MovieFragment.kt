package com.yanze.cloudreaderkotlin.ui.movie

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
import com.yanze.cloudreaderkotlin.utils.ImageLoadUtil
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.utils.showToast
import com.yanze.cloudreaderkotlin.view.dialog.MyDialog
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.android.synthetic.main.header_item_move.view.*

@Deprecated("豆瓣API已关闭...")
class MovieFragment : BaseFragment() {
    private var isPrepared = false //初始化完成
    private var isFirst = true //第一次加载
    private lateinit var adapter: MovieListAdapter
    private lateinit var viewModel: MovieViewModel
    private var headerView: View? = null
    private var myDialog: MyDialog? = null

    override fun setContent(): Int {
        return R.layout.fragment_movie
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMovieFactory(context))
                .get(MovieViewModel::class.java)
        myDialog = MyDialog.showDialog(context)
        initRecyclerView()
        isPrepared = true
        loadData()
    }

    private fun initRecyclerView() {
        headerView = layoutInflater.inflate(R.layout.header_item_move, null)
        ImageLoadUtil.displayRandom(headerView?.iv_img!!,R.drawable.header_item_one)
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
                    0 -> {
                        childView.rv_movie.noMoreLoading()
                    }
                    1 -> {
                        viewModel.handleNextStart()
                        loadComingSoonMovie()
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
                        myDialog?.show()
                        loadHotMovie()
                    }
                    1 -> {
                        myDialog?.show()
                        viewModel.start = 0
                        loadComingSoonMovie()
                    }
                }
            }
        })
        headerView?.ll_movie_top?.setOnClickListener {
//            context?.let {  }
            MovieTopActivity.start(context!!)
        }
    }

    override fun loadData() {
        if (!isPrepared || !mIsVisible || !isFirst) {
            return
        }
        loadHotMovie()
    }

    //获取热映电影
    private fun loadHotMovie() = handleData(viewModel.getMovie()) {
        adapter.clear()
        adapter.addAll(it.subjects)
        adapter.notifyDataSetChanged()
        childView.rv_movie.refreshComplete()
        if (isFirst) {
            isFirst = false
        }
    }

    //获取即将上映电影
    private fun loadComingSoonMovie() {
        handleData(viewModel.getComing()) {
            if (viewModel.start == 0) {
                adapter.clear()
                adapter.notifyDataSetChanged()
            }
            // +2 一个刷新头布局 一个自己新增的头布局
            val positionStart = adapter.itemCount + 2
            adapter.addAll(it.subjects)
            adapter.notifyItemRangeInserted(positionStart, it.subjects.size)
            rv_movie.refreshComplete()
        }
    }

    private fun <T> handleData(liveData: LiveData<Resource<T>>
                               , action: (T) -> Unit) = liveData.observe(this, Observer { result ->
        if (result.state == Resource.LOADING) {
//            showLoading()
        } else if (result.state == Resource.SUCCESS && result.data != null) {
            myDialog?.dismiss()
            action(result.data)
            showContentView()
        } else {
            myDialog?.dismiss()
            if (headerView?.tl_movie?.selectedTabPosition == 0) {
                if (adapter.itemCount == 0) {
                    showError()
                    context?.showToast("${result.message}")
                }
            }else{
                if (viewModel.start == 0) {
                    headerView?.tl_movie?.setScrollPosition(0,0f,true)
                    headerView?.tl_movie?.getTabAt(0)?.select()
                    context?.showToast("${result.message}")
                }
                if (adapter.itemCount == 0) {
                    showError()
                }else{
                    childView.rv_movie.noMoreLoading()
                }
            }
        }
    })

    override fun onRefresh(){
        if ( headerView?.tl_movie?.selectedTabPosition === 0!!) {
            loadHotMovie()
        } else {
            loadComingSoonMovie()
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = MovieFragment()
    }
}