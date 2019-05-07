package com.yanze.cloudreaderkotlin.adapter

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

/**
 * 1.普通，主页使用(不传MtitleList)
 * 2.接收首页传递的标题(传mTItleList)
 */
class MyFragmentPageAdapter(fm: FragmentManager, var mFragment: List<Fragment>, var mTitleList: List<String> = ArrayList()) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = mFragment[position]

    override fun getCount() = mFragment.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position < mTitleList.size) Html.fromHtml(mTitleList[position]) else ""
    }
}