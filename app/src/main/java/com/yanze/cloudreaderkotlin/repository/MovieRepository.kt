package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache

/**
 * 电影仓库，RoomDatabase后期加上
 */
class MovieRepository private constructor(private var network: HttpClient, private val acache: ACache) {

    fun getHotMovie(): LiveData<Resource<HotMovieBean>> {
        val liveData = MutableLiveData<Resource<HotMovieBean>>()
        liveData.value = Resource.loading(null)
        val hotMovieBean = acache.getAsObject(Constants.HOT_MOVIE) as HotMovieBean?
        if (hotMovieBean != null) {
            liveData.postValue(Resource.success(hotMovieBean))
        } else {
            requestHotMovie(liveData)
        }
        return liveData
    }

    fun getComingSoon(start: Int, count: Int): LiveData<Resource<HotMovieBean>> {
        val liveData = MutableLiveData<Resource<HotMovieBean>>()
        liveData.value = Resource.loading(null)
        network.getComingSoon(start, count).subscribe(object : DefaultSubscriber<HotMovieBean>() {
            override fun _onNext(entity: HotMovieBean) {
                if (entity.subjects.isEmpty()) {
                    liveData.postValue(Resource.error("没有即将上映的电影数据~", null))
                } else {
                    liveData.postValue(Resource.success(entity))
                }
            }

            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

        })
        return liveData
    }

    //请求热门电影
    private fun requestHotMovie(liveData: MutableLiveData<Resource<HotMovieBean>>) {
        network.getHotMovie().subscribe(object : DefaultSubscriber<HotMovieBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: HotMovieBean) {
                liveData.postValue(Resource.success(entity))
                acache.remove(Constants.HOT_MOVIE)
                acache.put(Constants.HOT_MOVIE, entity, 24 * 60 * 60)//缓存为一天
            }

        })
    }

    companion object {
        private var instance: MovieRepository? = null

        fun getInstantce(network: HttpClient?, acache: ACache): MovieRepository {
            if (instance == null) {
                synchronized(MovieRepository::class.java) {
                    if (instance == null) {
                        instance = MovieRepository(network!!, acache)
                    }
                }
            }
            return instance!!
        }
    }

}