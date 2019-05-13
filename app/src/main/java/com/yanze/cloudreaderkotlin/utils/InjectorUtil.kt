package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache
import com.yanze.cloudreaderkotlin.repository.GankRepository
import com.yanze.cloudreaderkotlin.repository.MovieRepository
import com.yanze.cloudreaderkotlin.repository.WelfareRepository
import com.yanze.cloudreaderkotlin.ui.gank.android.GankViewModelFactory
import com.yanze.cloudreaderkotlin.ui.gank.customer.CsutomViewModelFactory
import com.yanze.cloudreaderkotlin.ui.gank.welfare.WelfareViewModelFactory
import com.yanze.cloudreaderkotlin.ui.movie.MovieViewModelFactory

object InjectorUtil {

    private fun getMovieRepository(acache: ACache) = MovieRepository.getInstantce(HttpClient.getInstance(), acache)

    //获取福利仓库
    private fun getWelfareRepository(acache: ACache) = WelfareRepository.getInstantce(HttpClient.getInstance(), acache)

    //获取干货数据仓库
    private fun getGankRepository(acache: ACache) = GankRepository.getInstance(HttpClient.getInstance(), acache)

    fun getMovieFactory(context: Context?) = MovieViewModelFactory(getMovieRepository(ACache.get(context)))

    //获取福利工厂类
    fun getWelfFactory(context: Context?) = WelfareViewModelFactory(getWelfareRepository(ACache.get(context)))

    fun getGankFactory(context: Context?) = GankViewModelFactory(getGankRepository(ACache.get(context)))

    fun getCustomFactory(context:Context?) = CsutomViewModelFactory(getGankRepository(ACache.get(context)))
}