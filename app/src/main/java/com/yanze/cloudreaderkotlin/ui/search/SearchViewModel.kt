package com.yanze.cloudreaderkotlin.ui.search

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.repository.GankRepository
import com.yanze.cloudreaderkotlin.repository.WanRepository

class SearchViewModel(private val wanRepository: WanRepository, private val gankRepository: GankRepository) : ViewModel() {

    var keyWord = ""

    var wanPage = 0
    var gankPage = 1
    var gankType = "all"

    fun setKey(key: String) {
        this.keyWord = key
    }

    fun wanSearch() = wanRepository.searchByKey(wanPage, keyWord)

    fun gankSearch() = gankRepository.searchByKey(keyWord, gankType, gankPage)

    fun getHotKey() = wanRepository.getHotkey()

    fun wanNextPage() {
        wanPage += 1
    }

    fun gankNextPage() {
        gankPage += 1
    }

    fun wanReset() {
        wanPage = 0
    }

    fun gankReset(){
        gankPage = 1
    }

    fun resetKeyword(){
        this.keyWord = ""
    }
}