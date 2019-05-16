package com.yanze.cloudreaderkotlin.network.service

import com.yanze.cloudreaderkotlin.data.bean.NaviJsonBean
import com.yanze.cloudreaderkotlin.data.bean.TreeResultBean
import io.reactivex.Observable
import retrofit2.http.GET

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
    fun getTreeJson():Observable<TreeResultBean>


}