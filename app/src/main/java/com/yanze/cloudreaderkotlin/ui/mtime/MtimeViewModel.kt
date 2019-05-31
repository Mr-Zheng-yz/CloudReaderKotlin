package com.yanze.cloudreaderkotlin.ui.mtime

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.repository.FilmRepository

class MtimeViewModel(private val repository: FilmRepository) : ViewModel() {

    private var movieId = 0

    fun setMovieId(id:Int){
        this.movieId = id
    }

    //获取热映电影
    fun getHotFilm() = repository.getHotFilm()

    //获取即将上映电影
    fun getComingFilm() = repository.getComingFilm()

    //获取电影详情
    fun getFilmDetail() = repository.getFilmDetail(movieId)

}