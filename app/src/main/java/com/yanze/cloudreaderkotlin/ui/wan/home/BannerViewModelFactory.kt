package com.yanze.cloudreaderkotlin.ui.wan.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.WanRepository

class BannerViewModelFactory(private val repository: WanRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BannerViewModel(repository) as T
    }

}