package com.yanze.cloudreaderkotlin.ui.gank.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.GankRepository

class CsutomViewModelFactory(private val repository: GankRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomViewModel(repository) as T
    }

}