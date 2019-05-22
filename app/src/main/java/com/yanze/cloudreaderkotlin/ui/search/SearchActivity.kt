package com.yanze.cloudreaderkotlin.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xrecyclerview.XRecyclerView
import com.google.android.material.tabs.TabLayout
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.rv.CategoryArticleAdapter
import com.yanze.cloudreaderkotlin.adapter.rv.GankDataAdapter
import com.yanze.cloudreaderkotlin.adapter.rv.SearchTagKeyAdapter
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.search.SearchHotTagBean
import com.yanze.cloudreaderkotlin.utils.*
import com.yanze.cloudreaderkotlin.utils.statusbar.StatusBarUtil
import com.yanze.cloudreaderkotlin.view.MyDividerItemDecoration
import com.yanze.cloudreaderkotlin.view.dialog.MyDialog
import com.yanze.cloudreaderkotlin.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_search.*
import java.time.Instant

class SearchActivity : AppCompatActivity(), XRecyclerView.LoadingListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var hotAdapter: SearchTagKeyAdapter
    private lateinit var wanAdapter: CategoryArticleAdapter
    private lateinit var gankAdapter: GankDataAdapter
    private lateinit var myDialog: MyDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme), 0)
        viewModel = ViewModelProviders.of(this, InjectorUtil.getSearchViewModelFactory(this@SearchActivity))
                .get(SearchViewModel::class.java)
        initRecycler()
        initView()
        initHotKey()
    }

    private fun initRecycler() {
        //hot key
        hotAdapter = SearchTagKeyAdapter()
        rv_search_tag.layoutManager = RecyclerViewHelper.getFlexLayoutManager(this)
        rv_search_tag.adapter = hotAdapter
        hotAdapter.setTagClickCallback(object : SearchTagKeyAdapter.HotTagCallback {
            override fun tagClick(bean: SearchHotTagBean) {
                viewModel.keyWord = bean.name
                et_content.setText(bean.name)
                BaseTools.hideSoftKeyBoard(this@SearchActivity)
            }
        })

        //search content
        xrv_search.layoutManager = LinearLayoutManager(this)
        xrv_search.setPullRefreshEnabled(false)
        xrv_search.setLoadingMoreEnabled(true)
        xrv_search.setLoadingListener(this)

        wanAdapter = CategoryArticleAdapter()
        gankAdapter = GankDataAdapter()

        val itemDecoration = MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, false)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this@SearchActivity, R.drawable.shape_line)!!)
        xrv_search.addItemDecoration(itemDecoration)
    }

    private fun initView() {
        myDialog = MyDialog.showDialog(this@SearchActivity)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        et_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val content = et_content.text.toString().trim()
                if (content.isNotEmpty()) {
                    iv_clear.visibility = View.VISIBLE
                    viewModel.setKey(content)
                    load()
                } else {
                    iv_clear.visibility = View.GONE
                }
            }
        })
        et_content.setOnEditorActionListener { v, actionId, event ->
            if (viewModel.keyWord.isNotEmpty() && tl_search.selectedTabPosition == 3 && actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (Patterns.WEB_URL.matcher(viewModel.keyWord).matches() || URLUtil.isValidUrl(viewModel.keyWord)) {
                    BaseTools.hideSoftKeyBoard(this)
                    WebViewActivity.loadUrl(this, viewModel.keyWord, "加载中...")
                } else {
                    showToast("请输入正确的链接~")
                }
            } else {
                BaseTools.hideSoftKeyBoard(this)
            }
            return@setOnEditorActionListener false
        }
        iv_clear.setOnClickListener {
            viewModel.resetKeyword()
            et_content.setText("")
            et_content.isFocusable = true
            et_content.isFocusableInTouchMode = true
            et_content.requestFocus()
            showHotTagView(true)
            BaseTools.showSoftKeyBoard(this, et_content)
        }
        tl_search.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                initViewModel(tab.position)
            }
        })
        initViewModel(0)
    }

    private fun initViewModel(position: Int) {
        if (position == 0) {
            wanAdapter = CategoryArticleAdapter()
            xrv_search.adapter = wanAdapter
        } else if (position == 1 || position == 2) {
            gankAdapter = GankDataAdapter()
            xrv_search.adapter = gankAdapter
        }
        if (viewModel.keyWord.isNotEmpty()) {
            load()
        }
    }

    private fun initHotKey() {
        viewModel.getHotKey().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                }
                Resource.SUCCESS -> {
                    hotAdapter.clear()
                    hotAdapter.addAll(it.data?.data!!)
                    hotAdapter.notifyDataSetChanged()
                    showHotTagView(true)
                }
                Resource.ERROR -> {
                    showHotTagView(false)
                }
            }
        })
    }

    private fun load() {
        when (tl_search.selectedTabPosition) {
            0 -> {
                viewModel.wanReset()
                viewModel.wanSearch()
                loadWanData()
            }
            1 -> {
                viewModel.gankReset()
                viewModel.gankType = "Android"
                loadGankData()
            }
            2 -> {
                viewModel.gankReset()
                viewModel.gankType = "all"
                loadGankData()
            }
            else -> {
            }
        }
    }

    private fun loadWanData() {
        viewModel.wanSearch().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                    if (viewModel.wanPage == 0) {
                        myDialog.show()
                    }
                }
                Resource.SUCCESS -> {
                    myDialog.dismiss()
                    if (viewModel.wanPage == 0) {
                        wanAdapter.clear()
                    }
                    wanAdapter.addAll(it.data?.data?.datas!!)
                    wanAdapter.notifyDataSetChanged()
                    xrv_search.refreshComplete()
                    showHotTagView(false)
                }
                Resource.ERROR -> {
                    myDialog.dismiss()
                    if (viewModel.wanPage == 1) {
                        wanAdapter.clear()
                        wanAdapter.notifyDataSetChanged()
                        xrv_search.visibility = View.GONE
                    } else {
                        xrv_search.noMoreLoading()
                    }
                    showToast("${it.message}")
                }
            }
        })
    }

    private fun loadGankData() {
        viewModel.gankSearch().observe(this, Observer {
            when (it.state) {
                Resource.LOADING -> {
                    if (viewModel.gankPage == 1) {
                        myDialog.show()
                    }
                }
                Resource.SUCCESS -> {
                    myDialog.dismiss()
                    if (viewModel.gankPage == 1) {
                        gankAdapter.clear()
                    }
                    gankAdapter.addAll(it.data?.results!!)
                    gankAdapter.notifyDataSetChanged()
                    xrv_search.refreshComplete()
                    showHotTagView(false)
                }
                Resource.ERROR -> {
                    myDialog.dismiss()
                    if (viewModel.gankPage == 1) {
                        gankAdapter.clear()
                        gankAdapter.notifyDataSetChanged()
                        xrv_search.visibility = View.GONE
                    } else {
                        xrv_search.noMoreLoading()
                    }
                    showToast("${it.message}")
                }
            }
        })
    }

    //加载更多
    override fun onLoadMore() {
        when (tl_search.selectedTabPosition) {
            0 -> {
                viewModel.wanNextPage()
                loadWanData()
            }
            1, 2 -> {
                viewModel.gankNextPage()
                loadGankData()
            }
            else -> {
            }
        }
    }

    override fun onRefresh() {
    }

    private fun showHotTagView(isShow: Boolean) {
        if (isShow) {
            ll_search_tag.visibility = View.VISIBLE
            xrv_search.visibility = View.GONE
        } else {
            ll_search_tag.visibility = View.GONE
            xrv_search.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}
