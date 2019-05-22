package com.yanze.cloudreaderkotlin.network.service

import com.yanze.cloudreaderkotlin.data.bean.GankIoDataBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GankService {

    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("data/{type}/{pre_page}/{page}")
    abstract fun getGankIoData(@Path("type") id: String, @Path("page") page: Int, @Path("pre_page") pre_page: Int): Observable<GankIoDataBean>

    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     * eg:http://gank.io/api/day/2015/08/06
     */
    @GET("day/{year}/{month}/{day}")
    abstract fun getGankIoDay(@Path("year") year: String, @Path("month") month: String, @Path("day") day: String): Observable<String>

    /**
     * 搜索
     */
    @GET("search/query/{keyWord}/category/{type}/count/20/page/{page}")
    fun searchGank(@Path("page") page: Int, @Path("type") type: String, @Path("keyWord") keyWord: String): Observable<GankIoDataBean>

}