package com.yanze.cloudreaderkotlin.ui.gank.customer

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import com.yanze.cloudreaderkotlin.repository.GankRepository

class CustomViewModel(val repository: GankRepository) : ViewModel() {

    var type = "all"
    var page = 1
    var gankResults = ArrayList<GankResult>()

    fun setDataType(type:String){
        this@CustomViewModel.type = type
    }

    fun handleNextPage(){
        page += page
    }

    fun reset(){
        page = 1
        gankResults.clear()
    }

    fun getGankData() = repository.getGankData(type, page, Constants.PAGE_COUNT)

}