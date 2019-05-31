package com.yanze.cloudreaderkotlin.network

import com.yanze.cloudreaderkotlin.data.bean.GankIoDataBean
import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.network.service.FilmService
import com.yanze.cloudreaderkotlin.network.service.GankService
import com.yanze.cloudreaderkotlin.network.service.MovieService
import com.yanze.cloudreaderkotlin.network.service.WanService
import io.reactivex.Observable

class HttpClient : BaseReqo() {

    private fun getDouBanService(): MovieService = ServiceCreate.create(MovieService::class.java, ServiceCreate.API_DOUBAN)
    private fun getGankService(): GankService = ServiceCreate.create(GankService::class.java, ServiceCreate.API_GANKIO)
    private fun getWanService(): WanService = ServiceCreate.create(WanService::class.java, ServiceCreate.API_WAN_ANDROID)
    private fun getFilmService(): FilmService = ServiceCreate.create(FilmService::class.java, ServiceCreate.API_MTIME)
    private fun getMtimeTicketService(): FilmService = ServiceCreate.create(FilmService::class.java, ServiceCreate.API_MTIME_TICKET)

    //==============================豆瓣电影================================
    fun getHotMovie(): Observable<HotMovieBean> = transform(getDouBanService().getHotMovie())

    fun getComingSoon(start: Int, count: Int) = transform(getDouBanService().getComingSoon(start, count))

    fun getMovieDetail(id: String) = transform(getDouBanService().getMovieDetail(id))

    fun getMovieTop250(start: Int, count: Int) = transform(getDouBanService().getMovieTop250(start, count))
    //==============================豆瓣电影end==============================

    //==============================干货集中营===============================
    fun getGankIoData(type: String, page: Int, pre_page: Int): Observable<GankIoDataBean> = transform(getGankService().getGankIoData(type, page, pre_page))

    fun searchGank(page: Int, type: String, keyword: String) = transform(getGankService().searchGank(page, type, keyword))
    //==============================干货集中营end===============================

    //==============================玩安卓API===============================
    fun getNaviJson() = transform(getWanService().getNaviJson())

    fun getTreeJson() = transform(getWanService().getTreeJson())

    fun getWanHome(page: Int, cid: Int?) = transform(getWanService().getHomeList(page, cid))

    fun getWanAndroidBanner() = transform(getWanService().getWanAndroidBanner())

    fun getHotkey() = transform(getWanService().getHotkey())

    fun searchWan(page: Int, keyword: String) = transform(getWanService().searchWan(page, keyword))

    //==============================玩安卓end===============================

    //==============================时光网电影==============================
    fun getHotFilm() = transform(getFilmService().getHotFilm())

    fun getComingFilm() = transform(getFilmService().getComingFilm())

    fun getFilmDetail(movieId: Int) = transform(getMtimeTicketService().getFilmDetail(movieId = movieId))
    //==============================时光网电影end==============================

    companion object {
        private var network: HttpClient? = null

        fun getInstance(): HttpClient {
            if (network == null) {
                synchronized(HttpClient::class.java) {
                    if (network == null) {
                        network = HttpClient()
                    }
                }
            }
            return network!!
        }
    }
}