package com.yanze.cloudreaderkotlin.ui.wan.home

import androidx.lifecycle.ViewModel
import com.yanze.cloudreaderkotlin.data.bean.WanBannerResultBean
import com.yanze.cloudreaderkotlin.repository.WanRepository

class BannerViewModel(private val repository: WanRepository) : ViewModel() {

    var page: Int = 0
    private var cid: Int? = null

    fun setData(cid: Int?) {
        this.cid = cid
    }

    fun getHomeData() = repository.getWanHomeData(page, cid)

    fun getHomeBanner() = repository.getWanAndroidBanner()

    fun getImagesByBannerBean(bannerBean: WanBannerResultBean): List<String> {
        val images = ArrayList<String>()
        bannerBean.data.forEach {
            images.add(it.imagePath)
        }
        return images
    }

    fun getTitlesByBannerBean(bannerBean: WanBannerResultBean): MutableList<String> {
        val title = ArrayList<String>()
        bannerBean.data.forEach {
            title.add(it.title)
        }
        return title
    }

    fun <T> handleBannerData(bannerBean: WanBannerResultBean, action: (T) -> String): MutableList<String> {
        val element = ArrayList<String>()
        bannerBean.data.forEach {
            element.add(action(it as T))
        }
        return element
    }

    fun reset() {
        page = 0
    }

    fun handleNextPage() {
        this.page += 1
    }

}