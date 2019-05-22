package com.yanze.cloudreaderkotlin.ui.wan.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.WanArticleAdapter
import com.yanze.cloudreaderkotlin.base.BaseActivity
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.WanHomeResultBean
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.utils.RecyclerViewHelper
import kotlinx.android.synthetic.main.fragment_banner.*

class ArticleListActivity : BaseActivity() {

    private lateinit var viewModel: ArticleViewModel
    private lateinit var adapter: WanArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_banner)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getArticleViewModelFactory(this@ArticleListActivity))
                .get(ArticleViewModel::class.java)
        initView()
        getInstantData()
        loadData()
    }

    private fun initView() {
        adapter = WanArticleAdapter()
        RecyclerViewHelper.initRefresh(xrv_wan)
        xrv_wan.adapter = adapter
        xrv_wan.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                if (!srl_wan.isRefreshing) {
                    viewModel.handleNextPage()
                    loadData()
                } else {
                    xrv_wan.refreshComplete()
                }
            }
        })

        //下拉刷新
        srl_wan.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        srl_wan.setOnRefreshListener {
            srl_wan.postDelayed({
                viewModel.reset()
                xrv_wan.reset()
                loadData()
            }, 350)
        }
    }

    private fun getInstantData() {
        val cid = intent.getIntExtra("cid", 0)
        val chapterName = intent.getStringExtra("chapterName")
        setTitle(chapterName)
        adapter.setNoShowChapterName()
        viewModel.setCid(cid)
    }

    private fun loadData() {
        viewModel.getArticleList().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                    if (adapter.itemCount == 0) {
                        showLoading()
                    }
                }
                Resource.SUCCESS -> {
                    updateUi(it.data!!)
                }
                Resource.ERROR -> {
                    if (srl_wan.isRefreshing) {
                        srl_wan.isRefreshing = false
                    }
                    if (viewModel.page == 0) {
                        showError()
                    } else {
                        xrv_wan.noMoreLoading()
                    }
                }
            }
        })
    }

    private fun updateUi(articleResultBean: WanHomeResultBean) {
        if (srl_wan.isRefreshing) {
            srl_wan.isRefreshing = false
        }
        showContentView()
        if (viewModel.page == 0) {
            adapter.clear()
        }
        adapter.addAll(articleResultBean.data.datas)
        adapter.notifyDataSetChanged()
        xrv_wan.refreshComplete()
    }

    override fun onRefresh() {
        srl_wan.postDelayed({
            loadData()
        },150)
    }

    companion object {
        fun start(context: Context, cid: Int, chapterName: String) {
            val intent = Intent(context, ArticleListActivity::class.java)
            intent.putExtra("cid", cid)
            intent.putExtra("chapterName", chapterName)
            context.startActivity(intent)
        }
    }

}
