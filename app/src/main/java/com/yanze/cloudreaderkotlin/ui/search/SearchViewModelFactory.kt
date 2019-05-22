package com.yanze.cloudreaderkotlin.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.GankRepository
import com.yanze.cloudreaderkotlin.repository.WanRepository

class SearchViewModelFactory(private val repository: WanRepository,private val gankRepository: GankRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository,gankRepository) as T
    }

}