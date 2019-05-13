package com.yanze.cloudreaderkotlin.ui.gank.android

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.repository.GankRepository

class GankViewModel(private val repository: GankRepository) : ViewModel() {

    var page = 1

    var lastPage = 1 //上次加载页数

    var gankResults = ArrayList<GankResult>()

    fun reset() {
        page = 1
        lastPage = 1
        gankResults.clear()
    }

    fun handleNextPage() {
        page += 1
    }

    fun updateLastPage() {
        lastPage = page
    }

    /**
     * 获取大安卓数据
     */
    fun getAndroidData() = repository.getGankData("Android", page, Constants.PAGE_COUNT)


}