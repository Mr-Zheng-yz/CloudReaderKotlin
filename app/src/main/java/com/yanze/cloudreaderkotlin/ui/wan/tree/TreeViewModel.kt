package com.yanze.cloudreaderkotlin.ui.wan.tree

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.repository.WanRepository

class TreeViewModel (private val repository: WanRepository): ViewModel() {

    fun getTreeJson() = repository.getTreeJson()

}