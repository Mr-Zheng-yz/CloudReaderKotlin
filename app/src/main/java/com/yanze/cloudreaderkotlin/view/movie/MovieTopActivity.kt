package com.yanze.cloudreaderkotlin.view.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.MovieTopAdapter
import com.yanze.cloudreaderkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_top.*

class MovieTopActivity : BaseActivity() {

    private lateinit var adapter: MovieTopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_top)
        setTitle("豆瓣电影Top250")
        initRecyclerView()
        loadDoubanTop250()
    }

    private fun initRecyclerView(){
        adapter = MovieTopAdapter()
        xrv_top.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        xrv_top.setPullRefreshEnabled(false)
        xrv_top.setLoadingMoreEnabled(true)
        xrv_top.itemAnimator = null
        xrv_top.clearHeader()
        xrv_top.adapter = adapter

    }

    private fun loadDoubanTop250() {

    }

    override fun onRefresh() {
        loadDoubanTop250()
    }

    companion object {
        fun start(context:Context){
            val intent = Intent(context, MovieTopActivity::class.java)
            context.startActivity(intent)
        }
    }
}
