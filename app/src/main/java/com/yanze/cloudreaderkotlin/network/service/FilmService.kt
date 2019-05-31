package com.yanze.cloudreaderkotlin.network.service

import com.yanze.cloudreaderkotlin.data.bean.ComingMovieResultBean
import com.yanze.cloudreaderkotlin.data.bean.MtimeMovieResultBean
import com.yanze.cloudreaderkotlin.data.bean.FilmDetailResultMovie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 时光网电影
 */
interface FilmService {

    /**
     * 时光网热映电影
     */
    @GET("Showtime/LocationMovies.api?locationId=561")
    fun getHotFilm(): Observable<MtimeMovieResultBean>

    /**
     * 时光网即将上映电影
     */
    @GET("Movie/MovieComingNew.api?locationId=561")
    fun getComingFilm(): Observable<ComingMovieResultBean>

    /**
     * 获取电影详情
     * FilmDetailBasicBean 561为武汉地区
     *
     * @param movieId 电影bean里的id
     */
    @GET("movie/detail.api?locationId=561")
    fun getFilmDetail(@Query("movieId") movieId: Int): Observable<FilmDetailResultMovie>
//    fun getFilmDetail(@Query("locationId") locationId: String = "561", @Query("movieId") movieId: Int): Observable<FilmDetailResultMovie>

}