package com.yanze.cloudreaderkotlin.ui.wan.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.CategoryArticleAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.WanHomeResultBean
import com.yanze.cloudreaderkotlin.ui.wan.home.BannerViewModel
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.view.MyDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_category_article.*

class CategoryArticleFragment : BaseFragment() {

    private var isFirst = true
    private var isPrepared = false
    private lateinit var viewModel: BannerViewModel
    private lateinit var adapter: CategoryArticleAdapter
    private var categoryId: Int? = null

    override fun setContent(): Int = R.layout.fragment_category_article

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getWanBannerFactory(context))
                .get(BannerViewModel::class.java)
        categoryId = arguments?.getInt(ID)
        initView()
        isPrepared = true
        loadData()
    }

    private fun initView() {
        adapter = CategoryArticleAdapter()
        xrv_category.layoutManager = LinearLayoutManager(context)
        xrv_category.setPullRefreshEnabled(false)
        xrv_category.setLoadingMoreEnabled(true)
        xrv_category.adapter = adapter
        val itemDecoration = MyDividerItemDecoration(context, DividerItemDecoration.VERTICAL, false)
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.shape_line) as Drawable
        itemDecoration.setDrawable(drawable)
        xrv_category.addItemDecoration(itemDecoration)

        xrv_category.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewModel.handleNextPage()
                loadCategoryData()
            }
        })
    }

    override fun loadData() {
        if (!isFirst || !isPrepared) {
            return
        }
        viewModel.setData(categoryId)
        loadCategoryData()
    }

    private fun loadCategoryData() {
        viewModel.getHomeData().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    updateUi(it.data!!)
                    isFirst = false
                }
                Resource.ERROR -> {
                    if (adapter.itemCount == 0) {
                        showError()
                    } else {
                        xrv_category.noMoreLoading()
                    }
                }
            }
        })
    }

    private fun updateUi(wanResultBean: WanHomeResultBean) {
        showContentView()
        if (viewModel.page == 0) {
            adapter.clear()
        }
        adapter.addAll(wanResultBean.data.datas)
        adapter.notifyDataSetChanged()
        xrv_category.refreshComplete()
    }

    override fun onRefresh() {
        loadCategoryData()
    }

    companion object {
        private const val ID = "categoryId"

        fun getInstance(categoryId: Int): CategoryArticleFragment {
            val fragment = CategoryArticleFragment()
            val bundle = Bundle()
            bundle.putInt(ID, categoryId)
            fragment.arguments = bundle
            return fragment
        }
    }

}