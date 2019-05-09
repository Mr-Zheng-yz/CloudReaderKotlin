package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.gankchild.GankResult
import java.io.Serializable

data class GankIoDataBean(
        val error: Boolean,
        val results: List<GankResult>?
) : Serializable