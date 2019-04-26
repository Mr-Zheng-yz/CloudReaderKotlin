package com.yanze.cloudreaderkotlin.view.movie

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.repository.MovieRepository

class MovieViewModel(private val repository: MovieRepository):ViewModel() {

    var movie:HotMovieBean? = null
    var start:Int = 0
    var count:Int = 20

    //获取热映电影
    fun getMovie() = repository.getHotMovie()

    //获取待上映电影
    fun getComing() = repository.getComingSoon(start,count)

    fun handleNextStart() {
        start += count
    }
}