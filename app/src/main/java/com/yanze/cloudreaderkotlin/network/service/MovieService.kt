package com.yanze.cloudreaderkotlin.network.service

import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.data.bean.MovieDetailBean
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 豆瓣电影
 */
interface MovieService {

    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    fun getHotMovie(): Observable<HotMovieBean>

    /**
     * 豆瓣即将上映电影
     */
    @GET("v2/movie/coming_soon")
    fun getComingSoon(@Query("start") start: Int, @Query("count") count: Int): Observable<HotMovieBean>


    /**
     * 获取电影详情
     *
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    fun getMovieDetail(@Path("id") id: String): Observable<MovieDetailBean>

    /**
     * 获取豆瓣电影top250
     *
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    fun getMovieTop250(@Query("start") start: Int, @Query("count") count: Int): Observable<HotMovieBean>
}