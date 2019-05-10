package com.yanze.cloudreaderkotlin.ui.gank.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.GankRepository

class GankViewModelFactory(private val repository: GankRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GankViewModel(repository) as T
    }
}