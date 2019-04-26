package com.yanze.cloudreaderkotlin.network

import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.network.service.MovieService
import io.reactivex.Observable

class HttpClient: BaseReqo() {

    private fun getDouBanService(): MovieService = ServiceCreate.create(MovieService::class.java, ServiceCreate.API_DOUBAN)

    //==============================豆瓣电影================================
    fun getHotMovie(): Observable<HotMovieBean> = transform(getDouBanService().getHotMovie())

    fun getComingSoon(start: Int, count: Int) = transform(getDouBanService().getComingSoon(start, count))

    fun getMovieDetail(id: String) = transform(getDouBanService().getMovieDetail(id))

    fun getMovieTop250(start: Int, count: Int) = transform(getDouBanService().getMovieTop250(start, count))
    //==============================豆瓣电影end==============================

    companion object {
        private var network: HttpClient? = null

        fun getInstance(): HttpClient? {
            if (network == null) {
                synchronized(HttpClient::class.java) {
                    if (network == null) {
                        network = HttpClient()
                    }
                }
            }
            return network
        }
    }
}