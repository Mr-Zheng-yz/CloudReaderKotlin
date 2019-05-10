package com.yanze.cloudreaderkotlin.ui.gank.android

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.repository.GankRepository

class GankViewModel(private val repository: GankRepository) : ViewModel() {

    var page = 1

    /**
     * 获取大安卓数据
     */
    fun getAndroidData() = repository.getGankData("Android", page, Constants.PAGE_COUNT)


}