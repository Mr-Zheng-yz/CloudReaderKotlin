package com.yanze.cloudreaderkotlin.ui.gank.welfare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.WelfareRepository

class WelfareViewModelFactory(private val repository: WelfareRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WelfareViewModel(repository) as T
    }
}