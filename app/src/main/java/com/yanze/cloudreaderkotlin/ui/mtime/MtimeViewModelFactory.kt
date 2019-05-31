package com.yanze.cloudreaderkotlin.ui.mtime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanze.cloudreaderkotlin.repository.FilmRepository

class MtimeViewModelFactory(private val repository: FilmRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MtimeViewModel(repository) as T
    }
}