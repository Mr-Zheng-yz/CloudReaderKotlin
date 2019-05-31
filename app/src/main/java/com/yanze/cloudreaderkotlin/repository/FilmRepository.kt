package com.yanze.cloudreaderkotlin.repository

import com.yanze.cloudreaderkotlin.data.bean.ComingMovieResultBean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.FilmDetailResultMovie
import com.yanze.cloudreaderkotlin.data.bean.MtimeMovieResultBean
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache

class FilmRepository(val network: HttpClient, val acache: ACache) {

    fun getHotFilm(): LiveData<Resource<MtimeMovieResultBean>> {
        val liveData = MutableLiveData<Resource<MtimeMovieResultBean>>()
        liveData.postValue(Resource.loading(null))
        val resultBean = acache.getAsObject(Constants.MTIME_HOT) as MtimeMovieResultBean?
        if (resultBean == null) {
            requestHotFilm(liveData)
        } else {
            liveData.postValue(Resource.success(resultBean))
        }
        return liveData
    }

    fun getComingFilm(): LiveData<Resource<ComingMovieResultBean>> {
        val liveData = MutableLiveData<Resource<ComingMovieResultBean>>()
        liveData.postValue(Resource.loading(null))
        val resultBean = acache.getAsObject(Constants.MTIME_COMING) as ComingMovieResultBean?
        if (resultBean == null) {
            requestComingFilm(liveData)
        } else {
            liveData.postValue(Resource.success(resultBean))
        }
        return liveData
    }

    fun getFilmDetail(movieId: Int): LiveData<Resource<FilmDetailResultMovie>> {
        val liveData = MutableLiveData<Resource<FilmDetailResultMovie>>()
        liveData.postValue(Resource.loading(null))
        val resultBean = acache.getAsObject("${Constants.MTIME_DETAIL}$movieId") as FilmDetailResultMovie?
//        if (resultBean == null) {
        if (true) {
            requestFilmDetail(movieId, liveData)
        } else {
            liveData.postValue(Resource.success(resultBean))
        }
        return liveData
    }

    //请求热映电影
    private fun requestHotFilm(liveData: MutableLiveData<Resource<MtimeMovieResultBean>>) {
        network.getHotFilm().subscribe(object : DefaultSubscriber<MtimeMovieResultBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: MtimeMovieResultBean) {
                if (entity.ms.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put(Constants.MTIME_HOT, entity, 24 * 60 * 60)
                } else {
                    liveData.postValue(Resource.error("还没有热映电影~", null))
                }
            }
        })
    }

    //请求即将上映电影
    private fun requestComingFilm(liveData: MutableLiveData<Resource<ComingMovieResultBean>>) {
        network.getComingFilm().subscribe(object : DefaultSubscriber<ComingMovieResultBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: ComingMovieResultBean) {
                if (entity.moviecomings.isNotEmpty() || entity.attention.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put(Constants.MTIME_COMING, entity, 24 * 60 * 60)
                } else {
                    liveData.postValue(Resource.error("没有即将上映和关注的电影~", null))
                }
            }
        })
    }

    //请求电影详情
    private fun requestFilmDetail(movieId: Int, liveData: MutableLiveData<Resource<FilmDetailResultMovie>>) {
        network.getFilmDetail(movieId = movieId).subscribe(object : DefaultSubscriber<FilmDetailResultMovie>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: FilmDetailResultMovie) {
                if (entity.data != null) {
                    liveData.postValue(Resource.success(entity))
                    acache.put("${Constants.MTIME_DETAIL}$movieId", entity)
                }else{
                    liveData.postValue(Resource.error("加载异常，请重试", null))
                }
            }
        })
    }

    companion object {
        private var instance: FilmRepository? = null

        fun getInstance(network: HttpClient, acache: ACache): FilmRepository {
            if (instance == null) {
                synchronized(FilmRepository::class.java) {
                    if (instance == null) {
                        instance = FilmRepository(network, acache)
                    }
                }
            }
            return instance!!
        }
    }
}