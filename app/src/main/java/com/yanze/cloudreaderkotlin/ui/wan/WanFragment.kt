package com.yanze.cloudreaderkotlin.ui.wan

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.MyFragmentPageAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.test.TextFragment
import com.yanze.cloudreaderkotlin.ui.wan.home.BannerFragment
import com.yanze.cloudreaderkotlin.ui.wan.navi.NaviFragment
import com.yanze.cloudreaderkotlin.ui.wan.tree.TreeFragment
import kotlinx.android.synthetic.main.fragment_wan.view.*

class WanFragment : BaseFragment() {

    private lateinit var mTitleList: ArrayList<String>
    private lateinit var mFragments: ArrayList<Fragment>

    override fun setContent() = R.layout.fragment_wan

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initFragmentList()

        initView()
        showContentView()
    }

    /**
     * 注意使用的是：getChildFragmentManager，
     * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
     * 但会内存溢出，在显示时加载数据
     */
    private fun initView() {
        val adapter = MyFragmentPageAdapter(childFragmentManager, mFragments, mTitleList)
        childView.vp_wan.adapter = adapter
        childView.vp_wan.offscreenPageLimit = 2
        adapter.notifyDataSetChanged()
        childView.tab_wan.tabMode = TabLayout.MODE_FIXED
        childView.tab_wan.setupWithViewPager(childView.vp_wan)
    }

    private fun initFragmentList() {
        mTitleList = arrayListOf("玩安卓","知识体系","导航数据")
        mFragments = arrayListOf(BannerFragment.getInstance(),TreeFragment.getInstance(),NaviFragment.getInstance())
    }

    companion object {
        fun getInstance() = WanFragment()
    }
}