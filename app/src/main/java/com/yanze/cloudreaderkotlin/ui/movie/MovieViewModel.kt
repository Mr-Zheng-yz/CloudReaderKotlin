package com.yanze.cloudreaderkotlin.ui.movie

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.data.bean.MovieDetailBean
import com.yanze.cloudreaderkotlin.repository.MovieRepository

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    var movie: HotMovieBean? = null
    var start: Int = 0
    var count: Int = 21

    var startTop: Int = 0

    var movieDetail: MovieDetailBean? = null //电影详情

    //获取热映电影
    fun getMovie() = repository.getHotMovie()

    //获取待上映电影
    fun getComing() = repository.getComingSoon(start, count)

    //获取电影详情
    fun getMovieDetail(movieId: String) = repository.getMovieDetail(movieId)

    //获取Top250
    fun getMovieTop250() = repository.getTop250Movie(startTop, count)

    fun handleNextStart() {
        start += count
    }

    fun handleNextStartTop() {
        startTop += count
    }
}