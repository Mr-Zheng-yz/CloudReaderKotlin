package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.GankIoDataBean
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache

class GankRepository(val network: HttpClient, val acache: ACache) {

    //获取Gank数据
    fun getGankData(type: String, page: Int, pre_page: Int): LiveData<Resource<GankIoDataBean>> {
        val liveData = MutableLiveData<Resource<GankIoDataBean>>()
        val gankDataBean = acache.getAsObject("${Constants.GANKIODATA_CACHE}$type$page") as GankIoDataBean?
        if (gankDataBean != null) {
            liveData.postValue(Resource.success(gankDataBean))
        } else {
            requestGankData(type, page, pre_page, liveData)
        }
        return liveData
    }

    //搜素
    fun searchByKey(keyWord: String,type:String,page:Int): LiveData<Resource<GankIoDataBean>> {
        val liveData = MutableLiveData<Resource<GankIoDataBean>>()
        liveData.postValue(Resource.loading(null))
        val resultBean = acache.getAsObject("${Constants.GANK_SEARCH}$type$keyWord$page") as GankIoDataBean?
        if (resultBean == null) {
            searchGank(keyWord, type, page, liveData)
        }else{
            liveData.postValue(Resource.success(resultBean))
        }
        return liveData
    }



    //请求Gank数据
    private fun requestGankData(type: String, page: Int, pre_page: Int, liveData: MutableLiveData<Resource<GankIoDataBean>>) {
        network.getGankIoData(type, page, pre_page)
                .subscribe(object : DefaultSubscriber<GankIoDataBean>() {
                    override fun _onError(errMsg: String) {
                        liveData.postValue(Resource.error(errMsg, null))
                    }

                    override fun _onNext(entity: GankIoDataBean) {
                        if (entity.results != null && entity.results.isNotEmpty()) {
                            liveData.postValue(Resource.success(entity))
                            acache.put("${Constants.GANKIODATA_CACHE}$type$page", entity, 24 * 60 * 60)
                        } else {
                            liveData.postValue(Resource.error("~~网络异常~~", null))
                        }
                    }
                })
    }

    //请求搜索接口
    private fun searchGank(keyWord: String, type: String, page: Int, liveData: MutableLiveData<Resource<GankIoDataBean>>) {
        network.searchGank(page, type, keyWord).subscribe(object : DefaultSubscriber<GankIoDataBean>() {
            override fun _onNext(entity: GankIoDataBean) {
                if (entity.results != null && entity.results.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put("${Constants.GANK_SEARCH}$type$keyWord$page", entity,24 * 60 * 60)
                } else {
                    liveData.postValue(Resource.error("没有更多数据了~", null))
                }
            }

            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg,null))
            }
        })
    }

    companion object {
        private var instance: GankRepository? = null
        fun getInstance(network: HttpClient, acache: ACache): GankRepository {
            if (instance == null) {
                synchronized(GankRepository::class.java) {
                    if (instance == null) {
                        instance = GankRepository(network, acache)
                    }
                }
            }
            return instance!!
        }
    }

}