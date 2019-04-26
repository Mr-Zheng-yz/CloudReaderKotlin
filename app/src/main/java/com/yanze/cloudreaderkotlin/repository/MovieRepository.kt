package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import com.yanze.cloudreaderkotlin.network.HttpClient

/**
 * 电影仓库，RoomDatabase后期加上
 */
class MovieRepository private constructor(private var network: HttpClient) {

    fun getHotMovie(): LiveData<Resource<HotMovieBean>> {
        val liveData = MutableLiveData<Resource<HotMovieBean>>()
        liveData.value = Resource.loading(null)
        network.getHotMovie().subscribe(object : DefaultSubscriber<HotMovieBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg,null))
            }

            override fun _onNext(entity: HotMovieBean) {
                liveData.postValue(Resource.success(entity))
            }

        })
        return liveData
    }

    fun getComingSoon(start:Int,count:Int):LiveData<Resource<HotMovieBean>>{
        val liveData = MutableLiveData<Resource<HotMovieBean>>()
        liveData.value = Resource.loading(null)
        network.getComingSoon(start,count).subscribe(object : DefaultSubscriber<HotMovieBean>() {
            override fun _onNext(entity: HotMovieBean) {
                liveData.postValue(Resource.success(entity))
            }

            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg,null))
            }

        })
        return liveData
    }

    companion object {
        private var instance: MovieRepository? = null

        fun getInstantce(network: HttpClient?): MovieRepository {
            if (instance == null) {
                synchronized(MovieRepository::class.java) {
                    if (instance == null) {
                        instance = MovieRepository(network!!)
                    }
                }
            }
            return instance!!
        }
    }

}