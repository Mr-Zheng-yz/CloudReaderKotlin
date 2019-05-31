package com.yanze.cloudreaderkotlin.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.MovieTopAdapter
import com.yanze.cloudreaderkotlin.base.BaseActivity
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.moviechild.SubjectsBean
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import kotlinx.android.synthetic.main.activity_movie_top.*

@Deprecated("豆瓣API已关闭...")
class MovieTopActivity : BaseActivity() {

    private lateinit var adapter: MovieTopAdapter
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_top)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getMovieFactory(this@MovieTopActivity))
                .get(MovieViewModel::class.java)
        setTitle("豆瓣电影Top250")
        initRecyclerView()
        loadDoubanTop250()
    }

    private fun initRecyclerView() {
        adapter = MovieTopAdapter()
        adapter.setListener(object : MovieTopAdapter.OnClickListener {
            override fun onClick(bean: SubjectsBean, imageView: ImageView) {
                MovieDetailActivity.start(this@MovieTopActivity, bean, imageView)
            }
        })
        xrv_top.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        xrv_top.setPullRefreshEnabled(false)
        xrv_top.setLoadingMoreEnabled(true)
        xrv_top.itemAnimator = null
        xrv_top.clearHeader()
        xrv_top.adapter = adapter
        xrv_top.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewModel.handleNextStartTop()
                loadDoubanTop250()
            }
        })
    }

    private fun loadDoubanTop250() {
        handleData(viewModel.getMovieTop250(), action = {
            updateItemMovie(it.subjects)
//                val positionStart = adapter.itemCount + 1
//                adapter.addAll(it.subjects)
//                adapter.notifyItemRangeInserted(positionStart, it.subjects.size)
//                xrv_top.refreshComplete()
        })
    }

    private fun updateItemMovie(lists: List<SubjectsBean>) {
        val positionStart = adapter.itemCount + 1
        adapter.addAll(lists)
        adapter.notifyItemRangeInserted(positionStart, lists.size)
        xrv_top.refreshComplete()
    }

    private fun <T> handleData(liveData: LiveData<Resource<T>>, action: (T) -> Unit) =
            liveData.observe(this, Observer { result ->
                when (result.state) {
                    Resource.LOADING -> {
                        if (viewModel.startTop == 0)
                            showLoading()
                    }
                    Resource.SUCCESS -> {
                        if (viewModel.startTop == 0) {
                            showContentView()
                            adapter.clear()
                        }
                        result.data?.let { action(it) }
                    }
                    Resource.ERROR -> {
                        if (adapter.getData().isEmpty()) {
                            showError()
                        } else {
                            xrv_top.noMoreLoading()
                        }
                    }
                }
            })

    override fun onRefresh() {
        loadDoubanTop250()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MovieTopActivity::class.java)
            context.startActivity(intent)
        }
    }
}
