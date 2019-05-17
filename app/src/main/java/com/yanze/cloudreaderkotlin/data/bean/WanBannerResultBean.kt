package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.wan.WanBannerBean
import java.io.Serializable

data class WanBannerResultBean(
        val data: List<WanBannerBean>,
        val errorCode: Int,
        val errorMsg: String
) : Serializable