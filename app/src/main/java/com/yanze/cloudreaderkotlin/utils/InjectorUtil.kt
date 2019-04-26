package com.yanze.cloudreaderkotlin.utils

import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.repository.MovieRepository
import com.yanze.cloudreaderkotlin.view.movie.MovieViewModelFactory

object InjectorUtil {

    private fun getMovieRepository() = MovieRepository.getInstantce(HttpClient.getInstance())

    fun getMovieFactory() = MovieViewModelFactory(getMovieRepository())

}