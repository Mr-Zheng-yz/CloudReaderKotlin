package com.yanze.cloudreaderkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yanze.cloudreaderkotlin.app.Constants
import com.yanze.cloudreaderkotlin.data.Resource
import com.yanze.cloudreaderkotlin.data.bean.NaviJsonBean
import com.yanze.cloudreaderkotlin.data.bean.TreeResultBean
import com.yanze.cloudreaderkotlin.data.bean.WanBannerResultBean
import com.yanze.cloudreaderkotlin.data.bean.WanHomeResultBean
import com.yanze.cloudreaderkotlin.data.bean.search.SearchHotTagResult
import com.yanze.cloudreaderkotlin.network.DefaultSubscriber
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache

/**
 * 为减少服务器压力，每个接口都加了缓存
 *
 */
class WanRepository private constructor(private var network: HttpClient, private val acache: ACache) {

    //====================================先拿缓存，没有缓存数据再去请求接口====================================

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

    //获取知识体系
    fun getTreeJson(): LiveData<Resource<TreeResultBean>> {
        val liveData = MutableLiveData<Resource<TreeResultBean>>()
        liveData.postValue(Resource.loading(null))
        val treeResultBean = acache.getAsObject(Constants.TREE_JSON) as TreeResultBean?
        if (treeResultBean != null) {
            liveData.postValue(Resource.success(treeResultBean))
        } else {
            requestTreeJson(liveData)
        }
        return liveData
    }

    //获取玩安卓首页数据
    fun getWanHomeData(page: Int, cid: Int?): LiveData<Resource<WanHomeResultBean>> {
        val liveData = MutableLiveData<Resource<WanHomeResultBean>>()
        liveData.postValue(Resource.loading(null))
        val homeResultBean = acache.getAsObject("${Constants.WAN_ANDROID_ARTICLE}$page") as WanHomeResultBean?
        if (homeResultBean != null) {
            liveData.postValue(Resource.success(homeResultBean))
        } else {
            requestWanHomeData(page, cid, liveData)
        }
        return liveData
    }

    //获取玩安卓首页Banner
    fun getWanAndroidBanner(): LiveData<Resource<WanBannerResultBean>> {
        val liveData = MutableLiveData<Resource<WanBannerResultBean>>()
        liveData.postValue(Resource.loading(null))
        val bannerResultBean = acache.getAsObject(Constants.WAN_ANDROID_BANNER) as WanBannerResultBean?
        if (bannerResultBean != null) {
            liveData.postValue(Resource.success(bannerResultBean))
        } else {
            requestWanBanner(liveData)
        }
        return liveData
    }

    fun getHotkey(): LiveData<Resource<SearchHotTagResult>> {
        val liveData = MutableLiveData<Resource<SearchHotTagResult>>()
        liveData.postValue(Resource.loading(null))
        val resultBean = acache.getAsObject(Constants.SEARCH_HOT_KEY) as SearchHotTagResult?
        if (resultBean == null) {
            requestHotkey(liveData)
        } else {
            liveData.postValue(Resource.success(resultBean))
        }
        return liveData
    }

    fun searchByKey(page: Int, keyWord: String): LiveData<Resource<WanHomeResultBean>> {
        val liveData = MutableLiveData<Resource<WanHomeResultBean>>()
        liveData.postValue(Resource.loading(null))
        val resultBean = acache.getAsObject("${Constants.WAN_SEARCH}$keyWord$page") as WanHomeResultBean?
        if (resultBean == null) {
            requestSearchKey(page, keyWord, liveData)
        } else {
            liveData.postValue(Resource.success(resultBean))
        }
        return liveData
    }

    //====================================真正的网络请求===========================================

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

    //请求知识体系数据
    private fun requestTreeJson(liveData: MutableLiveData<Resource<TreeResultBean>>) {
        network.getTreeJson().subscribe(object : DefaultSubscriber<TreeResultBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: TreeResultBean) {
                if (entity.data.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put(Constants.TREE_JSON, entity)
                } else {
                    liveData.postValue(Resource.error("数据为空~", null))
                }
            }
        })
    }

    //请求玩安卓首页数据
    private fun requestWanHomeData(page: Int, cid: Int?, liveData: MutableLiveData<Resource<WanHomeResultBean>>) {
        network.getWanHome(page, cid).subscribe(object : DefaultSubscriber<WanHomeResultBean>() {
            override fun _onNext(entity: WanHomeResultBean) {
                if (entity.data.datas.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put("${Constants.WAN_ANDROID_ARTICLE}$page", entity, 24 * 60 * 60) //缓存一天
                } else {
                    liveData.postValue(Resource.error("数据为空~", null))
                }
            }

            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }
        })
    }

    //请求玩安卓Banner
    private fun requestWanBanner(liveData: MutableLiveData<Resource<WanBannerResultBean>>) {
        network.getWanAndroidBanner().subscribe(object : DefaultSubscriber<WanBannerResultBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: WanBannerResultBean) {
                if (entity.data.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put(Constants.WAN_ANDROID_BANNER, entity, 24 * 60 * 60)
                } else {
                    liveData.postValue(Resource.error("轮播图为空~", null))
                }
            }
        })
    }

    //请求搜索热词
    private fun requestHotkey(liveData: MutableLiveData<Resource<SearchHotTagResult>>) {
        network.getHotkey().subscribe(object : DefaultSubscriber<SearchHotTagResult>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: SearchHotTagResult) {
                if (entity.data.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put(Constants.SEARCH_HOT_KEY, entity, 24 * 60 * 60)
                } else {
                    liveData.postValue(Resource.error("数据为空~", null))
                }
            }
        })
    }

    //请求搜索接口
    private fun requestSearchKey(page: Int, keyWord: String, liveData: MutableLiveData<Resource<WanHomeResultBean>>) {
        network.searchWan(page, keyWord).subscribe(object : DefaultSubscriber<WanHomeResultBean>() {
            override fun _onError(errMsg: String) {
                liveData.postValue(Resource.error(errMsg, null))
            }

            override fun _onNext(entity: WanHomeResultBean) {
                if (entity.data.datas.isNotEmpty()) {
                    liveData.postValue(Resource.success(entity))
                    acache.put("${Constants.WAN_SEARCH}$keyWord$page", entity, 24 * 60 * 60)
                } else {
                    liveData.postValue(Resource.error("没有更多数据了~", null))
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