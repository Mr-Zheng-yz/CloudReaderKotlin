package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.HotMovieBean
import com.yanze.cloudreaderkotlin.data.bean.MovieDetailBean
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
        val comingMovieBean = acache.getAsObject("${Constants.COMINGSOON_MOVIE}$start$count") as HotMovieBean?
        if (comingMovieBean != null) {
            liveData.postValue(Resource.success(comingMovieBean))
        }else{
            requestComingSoon(start, count, liveData)
        }
        return liveData
    }

    fun getMovieDetail(id: String): LiveData<Resource<MovieDetailBean>> {
        val liveData = MutableLiveData<Resource<MovieDetailBean>>()
        liveData.value = Resource.loading(null)
        val movieDetailBean = acache.getAsObject(id) as MovieDetailBean?
        if (movieDetailBean != null) {
            liveData.postValue(Resource.success(movieDetailBean))
        } else {
            requestMovieDetail(id, liveData)
        }
        return liveData
    }

    fun getTop250Movie(start: Int, count: Int): LiveData<Resource<HotMovieBean>> {
        val liveData = MutableLiveData<Resource<HotMovieBean>>()
        liveData.value = Resource.loading(null)
        val movieBean = acache.getAsObject("${Constants.TOP_MOVIE}$start$count") as HotMovieBean?
        if (movieBean != null) {
            liveData.postValue(Resource.success(movieBean))
        } else {
            requestTop250(start, count, liveData)
        }
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

    //请求即将上映电影
    private fun requestComingSoon(start:Int,count:Int,liveData:MutableLiveData<Resource<HotMovieBean>>){
        network.getComingSoon(start, count).subscribe(object : DefaultSubscriber<HotMovieBean>() {
            override fun _onNext(entity: HotMovieBean) {
                if (entity.subjects.isEmpty()) {
                    liveData.postValue(Resource.error("没有即将上映的电影数据~", null))
                } else {
                    liveData.postValue(Resource.success(entity))
                    acache.put("${Constants.COMINGSOON_MOVIE}$start$count",entity,24 * 60 * 60)
                }
            }

            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }
        })
    }

    //请求电影详情
    private fun requestMovieDetail(id: String, liveData: MutableLiveData<Resource<MovieDetailBean>>) {
        network.getMovieDetail(id).subscribe(object : DefaultSubscriber<MovieDetailBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: MovieDetailBean) {
                liveData.postValue(Resource.success(entity))
                acache.put(id, entity)
            }
        })
    }

    //请求Top250
    private fun requestTop250(start: Int, count: Int, liveData: MutableLiveData<Resource<HotMovieBean>>) {
        network.getMovieTop250(start, count).subscribe(object : DefaultSubscriber<HotMovieBean>() {
            override fun _onNext(entity: HotMovieBean) {
                liveData.postValue(Resource.success(entity))
                acache.put("${Constants.TOP_MOVIE}$start$count", entity, 7 * 24 * 60 * 60)//缓存一周
            }

            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }
        })
    }

    companion object {
        private var instance: MovieRepository? = null

        fun getInstantce(network: HttpClient, acache: ACache): MovieRepository {
            if (instance == null) {
                synchronized(MovieRepository::class.java) {
                    if (instance == null) {
                        instance = MovieRepository(network, acache)
                    }
                }
            }
            return instance!!
        }
    }

}