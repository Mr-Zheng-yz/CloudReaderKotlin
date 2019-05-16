package com.yanze.cloudreaderkotlin.ui.wan.navi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.WanRepository

class NaviViewModelFactory(private val repository: WanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NaviViewModel(repository) as T
    }

}