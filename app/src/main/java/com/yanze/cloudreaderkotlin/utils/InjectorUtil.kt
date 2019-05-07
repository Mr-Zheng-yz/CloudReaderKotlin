package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache
import com.yanze.cloudreaderkotlin.repository.MovieRepository
import com.yanze.cloudreaderkotlin.ui.movie.MovieViewModelFactory

object InjectorUtil {

    private fun getMovieRepository(acache:ACache) = MovieRepository.getInstantce(HttpClient.getInstance(),acache)

    fun getMovieFactory(context:Context?) = MovieViewModelFactory(getMovieRepository(ACache.get(context)))

}