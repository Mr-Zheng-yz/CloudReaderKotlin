package com.yanze.cloudreaderkotlin.ui.gank.customer

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.GankDataAdapter
import com.yanze.cloudreaderkotlin.app.Constants.Companion.GANK_CALA
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.utils.InjectorUtil
import com.yanze.cloudreaderkotlin.utils.SPUtils
import com.yanze.cloudreaderkotlin.utils.showToast
import kotlinx.android.synthetic.main.dialog_gank_bottomsheet.view.*
import kotlinx.android.synthetic.main.fragment_custom.view.*
import kotlinx.android.synthetic.main.header_item_gank_custom.view.*

class CsutomerFragment : BaseFragment(), View.OnClickListener {

    private var isPrepared = false
    private var isFirst = true
    private lateinit var viewModel: CustomViewModel
    private lateinit var adapter: GankDataAdapter

    private lateinit var headerView: View
    private var bottomDialog: BottomSheetDialog? = null

    override fun setContent() = R.layout.fragment_custom

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getCustomFactory(context))
                .get(CustomViewModel::class.java)
        initData()
        initView()
        isPrepared = true
    }

    private fun initData() {
        val type = SPUtils.getString(GANK_CALA, "全部")
        when (type) {
            "全部" -> viewModel.setDataType("all")
            "IOS" -> viewModel.setDataType("iOS")
            else -> viewModel.setDataType(type)
        }
    }

    private fun initView() {
        adapter = GankDataAdapter()
        childView.xrv_custom.setPullRefreshEnabled(false)
        childView.xrv_custom.clearHeader() //去掉刷新头
        childView.xrv_custom.itemAnimator = null //去掉显示动画
        childView.xrv_custom.setLoadingMoreEnabled(true)
        headerView = View.inflate(context, R.layout.header_item_gank_custom, null)
        childView.xrv_custom.addHeaderView(headerView)
        initHeader()
        childView.xrv_custom.layoutManager = LinearLayoutManager(context)
        childView.xrv_custom.adapter = adapter
        childView.xrv_custom.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
            }

            override fun onLoadMore() {
                viewModel.handleNextPage()
                loadGankData()
            }
        })
    }

    private fun initHeader() {
        val gankCala = SPUtils.getString(GANK_CALA, "全部")
        headerView.tx_name.text = gankCala
        //dialog
        headerView.ll_choose_catalogue.setOnClickListener {
            if (bottomDialog == null) {
                bottomDialog = BottomSheetDialog(context!!)
                val contentView = layoutInflater.inflate(R.layout.dialog_gank_bottomsheet, null)
                initDialogView(contentView)
                bottomDialog?.setContentView(contentView)
            }
            bottomDialog?.show()
        }
    }

    private fun initDialogView(contentView: View) {
        contentView.gank_all.setOnClickListener(this)
        contentView.gank_ios.setOnClickListener(this)
        contentView.gank_app.setOnClickListener(this)
        contentView.gank_qian.setOnClickListener(this)
        contentView.gank_movie.setOnClickListener(this)
        contentView.gank_resouce.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val selectType = when (v?.id) {
            R.id.gank_all -> "全部"
            R.id.gank_ios -> "IOS"
            R.id.gank_app -> "App"
            R.id.gank_qian -> "前端"
            R.id.gank_movie -> "休息视频"
            R.id.gank_resouce -> "拓展资源"
            else -> "全部"
        }
        bottomDialog?.dismiss()
        if (isOtherType(selectType)) {
            changeContent(selectType)
        }
    }

    private fun isOtherType(selectType: String): Boolean {
        val click = SPUtils.getString(GANK_CALA, "全部")
        return if (click == selectType) {
            context?.showToast("当前已是$selectType" + "分类")
            false
        } else {
            true
        }
    }

    private fun changeContent(content: String) {
        when (content) {
            "全部" -> {
                headerView.tx_name.text = "全部"
                viewModel.setDataType("all")
            }
            "IOS" -> {
                headerView.tx_name.text = "IOS"
                viewModel.setDataType("iOS") //严格大小写
            }
            else -> {
                headerView.tx_name.text = content
                viewModel.setDataType(content)
            }
        }
        childView.xrv_custom.reset()
        viewModel.reset()
        showLoading()
        SPUtils.putString(GANK_CALA, content)
        loadGankData()
    }

    override fun loadData() {
        if (!isFirst || !isPrepared) {
            return
        }
        loadGankData()
    }

    private fun loadGankData() {
        viewModel.getGankData().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                    if (viewModel.page == 1) {
                        showLoading()
                    }
                }
                Resource.SUCCESS -> {
                    updateUi(it?.data?.results!!)
                    isFirst = false
                }
                Resource.ERROR -> {
                    if (adapter.itemCount == 0 || viewModel.page == 1) {
                        showError()
                    } else {
                        childView.xrv_custom.noMoreLoading()
                    }
                    context?.showToast(msg = "${it.message}")
                }
            }
        })
    }

    //更新UI
    private fun updateUi(gankResult: List<GankResult>) {
        if (viewModel.page == 1) {
            adapter.clear()
        }
        adapter.addAll(gankResult)
        adapter.notifyDataSetChanged()
        childView.xrv_custom.refreshComplete()
        showContentView()
    }

    override fun onRefresh() {
        loadGankData()
    }

    companion object {
        fun getInstance() = CsutomerFragment()
    }

}