package com.yanze.cloudreaderkotlin.ui.gank

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.MyFragmentPageAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.test.TextFragment
import com.yanze.cloudreaderkotlin.ui.gank.android.AndroidFragment
import com.yanze.cloudreaderkotlin.ui.gank.customer.CsutomerFragment
import com.yanze.cloudreaderkotlin.ui.gank.welfare.WelfareFragment
import kotlinx.android.synthetic.main.fragment_gank.view.*

class GankFragment : BaseFragment() {

    private var mIsFirst = true
    private var mIsPrepared = false

    //    private lateinit var mTitleList:ArrayList<String>
    private var mTitleList = ArrayList<String>()
    private var mFragments = ArrayList<Fragment>()

    override fun setContent() = R.layout.fragment_gank

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showContentView()
        mIsPrepared = true
    }

    override fun loadData() {
        if (!mIsPrepared || !mIsFirst) {
            return
        }
        initFragmentList()
        childView.vp_gank.postDelayed({
            val adapter = MyFragmentPageAdapter(childFragmentManager, mFragments, mTitleList)
            childView.vp_gank.adapter = adapter
            adapter.notifyDataSetChanged()
            //左右预加载页面个数
            childView.vp_gank.offscreenPageLimit = 3
            childView.tab_gank.tabMode = TabLayout.MODE_FIXED
            childView.tab_gank.setupWithViewPager(childView.vp_gank)
            mIsFirst = false
        }, 20)
    }

    private fun initFragmentList() {
        mTitleList.clear()
        mFragments.clear()
//        mTitleList.add("每日推荐")
        mTitleList.add("福利")
        mTitleList.add("干货订制")
        mTitleList.add("大安卓")
//        mFragments.add(TextFragment())
        mFragments.add(WelfareFragment.getInstance())
        mFragments.add(CsutomerFragment.getInstance())
        mFragments.add(AndroidFragment.getInstance())
//        mTitleList = arrayListOf("每日推荐", "福利", "干货定制", "大安卓")
//        mFragments = arrayListOf(TextFragment(), TextFragment(), TextFragment(), TextFragment())
    }

    companion object {
        fun getInstance() = GankFragment()
    }

}