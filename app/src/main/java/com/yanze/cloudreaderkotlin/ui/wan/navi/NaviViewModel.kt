package com.yanze.cloudreaderkotlin.ui.wan.navi

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.repository.WanRepository

class NaviViewModel(private val repository: WanRepository) : ViewModel() {

    fun getNaviJson() = repository.getNaviJsonData()

}