package com.yanze.cloudreaderkotlin.network

import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.network.service.MovieService
import io.reactivex.Observable

class HttpClient {

    private fun getDouBanService(): MovieService = ServiceCreate.create(MovieService::class.java, ServiceCreate.API_DOUBAN)

    //==============================豆瓣电影================================
    fun getHotMovie(): Observable<HotMovieBean> = getDouBanService().getHotMovie()

    fun getComingSoon(start: Int, count: Int) = getDouBanService().getComingSoon(start, count)

    fun getMovieDetail(id: String) = getDouBanService().getMovieDetail(id)

    fun getMovieTop250(start: Int, count: Int) = getDouBanService().getMovieTop250(start, count)
    //==============================豆瓣电影end==============================

}