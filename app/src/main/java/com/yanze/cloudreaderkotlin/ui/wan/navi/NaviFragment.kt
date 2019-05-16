package com.yanze.cloudreaderkotlin.ui.wan.navi

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.NaviAdapter
import com.yanze.cloudreaderkotlin.adapter.rv.NaviContentAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.NaviJsonBean
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import kotlinx.android.synthetic.main.fragment_navi.view.*

class NaviFragment : BaseFragment() {

    private var isFirst = true
    private var isPrepared = false

    private lateinit var viewModel: NaviViewModel

    private lateinit var naviAdapter: NaviAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var naviContentAdapter: NaviContentAdapter

    private var currentPosition = 0

    override fun setContent() = R.layout.fragment_navi

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getNaviJsonFactory(context))
                .get(NaviViewModel::class.java)
        initView()
        isPrepared = true
    }

    private fun initView() {
        //init navi
        naviAdapter = NaviAdapter()
        layoutManager = LinearLayoutManager(context)
        childView.rv_navi.layoutManager = layoutManager
        childView.rv_navi.adapter = naviAdapter

        //init content
        val layoutManager2 = LinearLayoutManager(context)
        naviContentAdapter = NaviContentAdapter(context!!)
        childView.rv_detail.layoutManager = layoutManager2
        childView.rv_detail.adapter = naviContentAdapter


        //为导航栏设置点击选中监听
        naviAdapter.setOnSelectListener(object : NaviAdapter.OnSelectListener {
            override fun onSelected(position: Int) {
                selectItem(position)
                moveToCenter(position)
                layoutManager2.scrollToPositionWithOffset(position, 0)
            }
        })

        //为导航内容添加滑动监听
        childView.rv_detail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val itemPosition = layoutManager2.findFirstVisibleItemPosition()
                if (currentPosition != itemPosition) {
                    selectItem(itemPosition)
                    moveToCenter(itemPosition)
                }
            }
        })
    }

    override fun loadData() {
        if (!isPrepared || !isFirst) {
            return
        }
        loadNaviJsonData()
    }

    private fun loadNaviJsonData() {
        viewModel.getNaviJson().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                    showLoading()
                }
                Resource.SUCCESS -> {
                    updateUi(it.data!!)
                    isFirst = false
                }
                Resource.ERROR -> {
                    showError()
                }
            }
        })
    }

    //更新数据
    private fun updateUi(resultBean: NaviJsonBean) {
        showContentView()
        naviAdapter.clear()
        naviAdapter.addAll(resultBean.data)
        naviAdapter.notifyDataSetChanged()
        selectItem(0)

        naviContentAdapter.clear()
        naviContentAdapter.addAll(resultBean.data)
        naviContentAdapter.notifyDataSetChanged()
    }

    /**
     * 同步导航栏标题选中状态
     */
    private fun selectItem(position: Int) {
        if (position < 0 || naviAdapter.getData().size < position) {
            return
        }
        naviAdapter.getData()[currentPosition].selected = false
        naviAdapter.notifyItemChanged(currentPosition)
        currentPosition = position
        naviAdapter.getData()[position].selected = true
        naviAdapter.notifyItemChanged(position)
    }

    /**
     * 将选中的导航栏标题居中
     */
    private fun moveToCenter(position: Int) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        val clickAt = childView.rv_navi.getChildAt(position - layoutManager.findFirstVisibleItemPosition())
        if (clickAt != null) {
            val y = (clickAt.top - childView.rv_navi.height / 2)
            childView.rv_navi.smoothScrollBy(0, y)
        }
    }

    override fun onRefresh() {
        loadNaviJsonData()
    }

    companion object {
        fun getInstance() = NaviFragment()
    }

}