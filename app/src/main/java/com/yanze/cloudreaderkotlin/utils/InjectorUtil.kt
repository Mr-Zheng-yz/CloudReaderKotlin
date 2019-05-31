package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import com.yanze.cloudreaderkotlin.network.HttpClient
import com.yanze.cloudreaderkotlin.network.cache.ACache
import com.yanze.cloudreaderkotlin.repository.*
import com.yanze.cloudreaderkotlin.ui.gank.android.GankViewModelFactory
import com.yanze.cloudreaderkotlin.ui.gank.customer.CsutomViewModelFactory
import com.yanze.cloudreaderkotlin.ui.gank.welfare.WelfareViewModelFactory
import com.yanze.cloudreaderkotlin.ui.movie.MovieViewModelFactory
import com.yanze.cloudreaderkotlin.ui.mtime.MtimeViewModelFactory
import com.yanze.cloudreaderkotlin.ui.search.SearchViewModelFactory
import com.yanze.cloudreaderkotlin.ui.wan.article.ArticleViewModelFactory
import com.yanze.cloudreaderkotlin.ui.wan.home.BannerViewModelFactory
import com.yanze.cloudreaderkotlin.ui.wan.navi.NaviViewModelFactory
import com.yanze.cloudreaderkotlin.ui.wan.tree.TreeViewModelFactory

object InjectorUtil {

    private var movieRepository: MovieRepository? = null
    private var welfareRepository: WelfareRepository? = null
    private var gankRepository: GankRepository? = null
    private var wanRepository: WanRepository? = null
    private var mtimeRepository: FilmRepository? = null

    //==============获取数据仓库===============

    private fun getMovieRepository(acache: ACache): MovieRepository {
        if (movieRepository == null) {
            synchronized(InjectorUtil::class.java) {
                if (movieRepository == null) {
                    movieRepository = MovieRepository.getInstantce(HttpClient.getInstance(), acache)
                }
            }
        }
        return movieRepository!!
    }

    //获取福利仓库
    private fun getWelfareRepository(acache: ACache): WelfareRepository {
        if (welfareRepository == null) {
            synchronized(InjectorUtil::class.java) {
                if (welfareRepository == null) {
                    welfareRepository = WelfareRepository.getInstantce(HttpClient.getInstance(), acache)
                }
            }
        }
        return welfareRepository!!
    }

    //获取干货数据仓库
    private fun getGankRepository(acache: ACache): GankRepository {
        if (gankRepository == null) {
            synchronized(InjectorUtil::class.java) {
                if (gankRepository == null) {
                    gankRepository = GankRepository.getInstance(HttpClient.getInstance(), acache)
                }
            }
        }
        return gankRepository!!
    }

    private fun getWanRepository(acache: ACache): WanRepository {
        if (wanRepository == null) {
            synchronized(InjectorUtil::class.java) {
                if (wanRepository == null) {
                    wanRepository = WanRepository.getInstance(HttpClient.getInstance(), acache)
                }
            }
        }
        return wanRepository!!
    }

    private fun getFilmRepository(acache: ACache): FilmRepository {
        if (mtimeRepository == null) {
            synchronized(InjectorUtil::class.java) {
                if (mtimeRepository == null) {
                    mtimeRepository = FilmRepository.getInstance(HttpClient.getInstance(), acache)
                }
            }
        }
        return mtimeRepository!!
    }

    //==============获取ViewModel创建工厂=============

    fun getMovieFactory(context: Context?) = MovieViewModelFactory(getMovieRepository(ACache.get(context)))

    //获取福利工厂类
    fun getWelfFactory(context: Context?) = WelfareViewModelFactory(getWelfareRepository(ACache.get(context)))

    fun getGankFactory(context: Context?) = GankViewModelFactory(getGankRepository(ACache.get(context)))

    fun getCustomFactory(context: Context?) = CsutomViewModelFactory(getGankRepository(ACache.get(context)))

    //获取导航数据工厂
    fun getNaviJsonFactory(context: Context?) = NaviViewModelFactory(getWanRepository(ACache.get(context)))

    //获取知识体系
    fun getTreeJsonFactory(context: Context?) = TreeViewModelFactory(getWanRepository(ACache.get(context)))

    //获取玩安卓首页
    fun getWanBannerFactory(context: Context?) = BannerViewModelFactory(getWanRepository(ACache.get(context)))

    //文章列表
    fun getArticleViewModelFactory(context: Context?) = ArticleViewModelFactory(getWanRepository(ACache.get(context)))

    //搜索
    fun getSearchViewModelFactory(context: Context?): SearchViewModelFactory {
        val acache = ACache.get(context)
        return SearchViewModelFactory(getWanRepository(acache), getGankRepository(acache))
    }

    //时光网电影
    fun getMTimeViewModelFactory(context: Context?): MtimeViewModelFactory{
        val acache = ACache.get(context)
        return MtimeViewModelFactory(getFilmRepository(acache))
    }
}