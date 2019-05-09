package com.yanze.cloudreaderkotlin.ui.gank.welfare

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.repository.WelfareRepository

class WelfareViewModel(private val repository: WelfareRepository) : ViewModel() {

    var page = 1

    var welfareResult = ArrayList<GankResult>()

    //获取福利图片
    fun getWelfareImage() = repository.getWelfareImages(page, Constants.PAGE_COUNT)

    fun handleNextPage() {
        page += 1
    }

}