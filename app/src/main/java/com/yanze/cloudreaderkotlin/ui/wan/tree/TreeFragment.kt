package com.yanze.cloudreaderkotlin.ui.wan.tree

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.TreeAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.TreeResultBean
import com.yanze.cloudreaderkotlin.utils.CommonUtils
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import kotlinx.android.synthetic.main.fragment_tree.view.*
import kotlinx.android.synthetic.main.header_item_tree.view.*

class TreeFragment : BaseFragment() {

    private var isFirst = true
    private var isPrepored = false
    private lateinit var viewModel: TreeViewModel
    private lateinit var adapter: TreeAdapter

    override fun setContent() = R.layout.fragment_tree

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getTreeJsonFactory(context))
                .get(TreeViewModel::class.java)
        initView()
        isPrepored = true
    }

    private fun initView() {
        adapter = TreeAdapter()
        val layoutManager = LinearLayoutManager(context)
        childView.rv_tree.setPullRefreshEnabled(false)
        childView.rv_tree.setLoadingMoreEnabled(false)
        childView.rv_tree.clearHeader()
        childView.rv_tree.layoutManager = layoutManager
        childView.rv_tree.adapter = adapter
        //init recycler headerView
        val headerView = LayoutInflater.from(context).inflate(R.layout.header_item_tree, null)
        childView.rv_tree.addHeaderView(headerView)
        headerView.tv_position.setOnClickListener {
            layoutManager.scrollToPositionWithOffset(adapter.mProjectPosition + 2, 0) //跳过头布局
        }

        childView.srl_tree.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        childView.srl_tree.setOnRefreshListener {
            childView.srl_tree.postDelayed({
                childView.rv_tree.reset()
                loadTreeData()
            }, 150)
        }
    }

    override fun loadData() {
        if (!isFirst || !isPrepored) {
            return
        }
        childView.srl_tree.isRefreshing = true
        childView.srl_tree.postDelayed({
            loadTreeData()
        },150)
    }

    private fun loadTreeData() {
        viewModel.getTreeJson().observe(this, androidx.lifecycle.Observer {
            when (it.state) {
                Resource.LOADING -> {
                    showLoading()
                }
                Resource.SUCCESS -> {
                    updateUi(it.data!!)
                    isFirst = false
                }
                Resource.ERROR -> {
                    if (childView.srl_tree.isRefreshing) {
                        childView.srl_tree.isRefreshing = false
                    }
                    showError()
                }
            }
        })
    }

    private fun updateUi(treeResultBean: TreeResultBean) {
        showContentView()
        if (childView.srl_tree.isRefreshing) {
            childView.srl_tree.isRefreshing = false
        }

        adapter.clear()
        adapter.addAll(treeResultBean.data)
        adapter.notifyDataSetChanged()
        childView.srl_tree.isRefreshing = false
    }

    override fun onRefresh() {
        loadTreeData()
    }

    companion object {
        fun getInstance() = TreeFragment()
    }
}