package com.yanze.cloudreaderkotlin.data.bean.wan

import java.io.Serializable

data class WanBannerBean(
        val desc: String,
        val id: Int,
        val imagePath: String,
        val isVisible: Int,
        val order: Int,
        val title: String,
        val type: Int,
        val url: String
) : Serializable