package com.yanze.cloudreaderkotlin.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceCreate {
    // gankio、豆瓣、（轮播图）
    const val API_GANKIO = "https://gank.io/api/"
    const val API_DOUBAN = "Https://api.douban.com/"
    const val API_TING = "https://tingapi.ting.baidu.com/v1/restserver/"
    const val API_FIR = "http://api.fir.im/apps/"
    const val API_WAN_ANDROID = "https://www.wanandroid.com/"
    const val API_QSBK = "http://m2.qiushibaike.com/"
    const val API_MTIME = "https://api-m.mtime.cn/"
    const val API_MTIME_TICKET = "https://ticket-api-m.mtime.cn/"

    private var gankHttps: Any? = null
    private var doubanHttps: Any? = null
    private var dongtingHttps: Any? = null
    private var firHttps: Any? = null
    private var wanAndroidHttps: Any? = null
    private var qsbkHttps: Any? = null
    private var mtimeHttps: Any? = null

    @Suppress("UNCHECKED_CAST")
    fun <T> create(service: Class<T>, apiUrl: String): T {
        when (apiUrl) {
            (API_GANKIO) -> {
                if (gankHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (gankHttps == null) {
                            gankHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return gankHttps as T
            }
            (API_DOUBAN) -> {
                if (doubanHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (doubanHttps == null) {
                            doubanHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return doubanHttps as T
            }
            (API_TING) -> {
                if (dongtingHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (dongtingHttps == null) {
                            dongtingHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return dongtingHttps as T
            }
            (API_FIR) -> {
                if (firHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (firHttps == null) {
                            firHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return firHttps as T
            }
            (API_WAN_ANDROID) -> {
                if (wanAndroidHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (wanAndroidHttps == null) {
                            wanAndroidHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return wanAndroidHttps as T
            }
            (API_QSBK) -> {
                if (qsbkHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (qsbkHttps == null) {
                            qsbkHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return qsbkHttps as T
            }
            API_MTIME, API_MTIME_TICKET -> {
                if (mtimeHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (mtimeHttps == null) {
                            mtimeHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return mtimeHttps as T
            }
            else -> {
                if (qsbkHttps == null) {
                    synchronized(ServiceCreate::class.java) {
                        if (qsbkHttps == null) {
                            qsbkHttps = getBuilder(apiUrl).create(service)
                        }
                    }
                }
                return qsbkHttps as T
            }
        }
    }

    //=================构建Retrofit===================

    private val httpClient = OkHttpClient.Builder()

    private fun getBuilder(apiUrl: String): Retrofit {
        val builder = Retrofit.Builder()
        builder.client(httpClient.build())
        builder.baseUrl(apiUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        return builder.build()
    }

}