package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.GankIoDataBean
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import androidx.lifecycle.MutableLiveData
import com.baize.fireeyekotlin.utils.log.L
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache

class WelfareRepository(private var network: HttpClient, private val acache: ACache) {

    //获取福利图片
    fun getWelfareImages(start: Int, count: Int): LiveData<Resource<GankIoDataBean>> {
        L.i(msg = "请求页数：start:$start  count:$count")
        val liveData = MutableLiveData<Resource<GankIoDataBean>>()
        liveData.postValue(Resource.loading(null))
        val welfareBean = acache.getAsObject("${Constants.WELFARE_IMAGE}$start$count") as GankIoDataBean?
        if (welfareBean != null) {
            liveData.postValue(Resource.success(welfareBean))
        } else {
            getWelfareImage(start, count, liveData)
        }
        return liveData
    }

    //请求福利图片
    private fun getWelfareImage(start: Int, count: Int, liveData: MutableLiveData<Resource<GankIoDataBean>>) {
        L.i(msg = "请求页数：type:福利  start:$start  count:$count")
        network.getGankIoData("福利", start, count)
                .subscribe(object : DefaultSubscriber<GankIoDataBean>() {
                    override fun _onError(errMsg: String) {
                        liveData.postValue(Resource.error(errMsg, null))
                    }

                    override fun _onNext(entity: GankIoDataBean) {
                        if (entity.results != null && !entity.results.isEmpty()) {
                            liveData.postValue(Resource.success(entity))
                            acache.put("${Constants.WELFARE_IMAGE}$start$count", entity, 24 * 60 * 60)
                        } else {
                            liveData.postValue(Resource.error("没有更多数据了~", null))
                        }
                    }
                })
    }

    companion object {
        private var instance: WelfareRepository? = null

        fun getInstantce(network: HttpClient, acache: ACache): WelfareRepository {
            if (instance == null) {
                synchronized(WelfareRepository::class.java) {
                    if (instance == null) {
                        instance = WelfareRepository(network, acache)
                    }
                }
            }
            return instance!!
        }
    }

}