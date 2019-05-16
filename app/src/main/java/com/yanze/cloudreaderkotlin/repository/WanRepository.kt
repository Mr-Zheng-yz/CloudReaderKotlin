package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.NaviJsonBean
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache

class WanRepository private constructor(private var network: HttpClient, private val acache: ACache) {

    //获取NaviJson数据
    fun getNaviJsonData(): LiveData<Resource<NaviJsonBean>> {
        val liveData = MutableLiveData<Resource<NaviJsonBean>>()
        liveData.postValue(Resource.loading(null))
        val naviJsonBean = acache.getAsObject(Constants.NAVI_JSON) as NaviJsonBean?
        if (naviJsonBean == null) {
            requestNaviJson(liveData)
        } else {
            liveData.postValue(Resource.success(naviJsonBean))
        }
        return liveData
    }

    //请求导航数据
    private fun requestNaviJson(liveData: MutableLiveData<Resource<NaviJsonBean>>) {
        network.getNaviJson().subscribe(object : DefaultSubscriber<NaviJsonBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: NaviJsonBean) {
                if (entity.data.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put(Constants.NAVI_JSON, entity)
                } else {
                    liveData.postValue(Resource.error("数据为空~", null))
                }
            }
        })
    }


    companion object {
        private var instance: WanRepository? = null

        fun getInstance(network: HttpClient, acache: ACache): WanRepository {
            if (instance == null) {
                synchronized(MovieRepository::class.java) {
                    if (instance == null) {
                        instance = WanRepository(network, acache)
                    }
                }
            }
            return instance!!
        }
    }
}