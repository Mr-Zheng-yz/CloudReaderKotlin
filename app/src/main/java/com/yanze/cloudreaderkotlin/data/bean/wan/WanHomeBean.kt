package com.yanze.cloudreaderkotlin.data.bean.wan

import java.io.Serializable

data class WanHomeBean(
        val curPage: Int,
        val datas: List<ArticlesBean>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
) : Serializable