package com.yanze.cloudreaderkotlin.network.service

import com.yanze.cloudreaderkotlin.data.bean.NaviJsonBean
import com.yanze.cloudreaderkotlin.data.bean.TreeResultBean
import com.yanze.cloudreaderkotlin.data.bean.WanBannerResultBean
import com.yanze.cloudreaderkotlin.data.bean.WanHomeResultBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WanService {

    /**
     * 导航数据
     */
    @GET("navi/json")
    fun getNaviJson(): Observable<NaviJsonBean>

    /**
     * 体系数据
     */
    @GET("tree/json")
    fun getTreeJson(): Observable<TreeResultBean>

    /**
     * 玩安卓首页数据，文章列表、知识体系下的文章
     */
    @GET("article/list/{page}/json")
    fun getHomeList(@Path("page") page: Int): Observable<WanHomeResultBean>
//    fun getHomeList(@Path("page") page: Int, @Query("cid") cid: Int):Observable<WanHomeResultBean>

    /**
     * WanAndroid首页轮播图
     */
    @GET("banner/json")
    fun getWanAndroidBanner(): Observable<WanBannerResultBean>

}