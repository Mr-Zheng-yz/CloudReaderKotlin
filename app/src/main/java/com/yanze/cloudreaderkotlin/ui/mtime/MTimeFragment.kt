package com.yanze.cloudreaderkotlin.ui.mtime

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.yanze.cloudreaderkotlin.R
import com.yanze.cloudreaderkotlin.adapter.MyFragmentPageAdapter
import com.yanze.cloudreaderkotlin.base.BaseFragment
import com.yanze.cloudreaderkotlin.test.TextFragment
import kotlinx.android.synthetic.main.fragment_mtime.view.*

class MTimeFragment : BaseFragment() {

    private var isFirst: Boolean = true
    private var isPrepared: Boolean = false
    private var fragments: ArrayList<Fragment> = ArrayList()
    private var titlesList: ArrayList<String> = ArrayList()

    override fun setContent(): Int = R.layout.fragment_mtime

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showContentView()
        isPrepared = true
    }

    override fun loadData() {
        if (!isFirst || !isPrepared) {
            return
        }
        childView.vp_mtime.postDelayed({
            initFragments()
            val adapter = MyFragmentPageAdapter(childFragmentManager, fragments, titlesList)
            childView.vp_mtime.adapter = adapter
            adapter.notifyDataSetChanged()
            childView.tab_mtime.tabMode = TabLayout.MODE_FIXED
            childView.tab_mtime.setupWithViewPager(childView.vp_mtime)
            isFirst = false
        }, 100)
    }

    private fun initFragments() {
        titlesList.clear()
        fragments.clear()
        titlesList.add("热映榜")
        titlesList.add("即将上映")
        fragments.add(FilmShowingFragment.getInstance("热映榜"))
        fragments.add(FilmComingFragment.getInstance("即将上映"))
    }

    companion object {
        fun getInstance() = MTimeFragment()
    }

}