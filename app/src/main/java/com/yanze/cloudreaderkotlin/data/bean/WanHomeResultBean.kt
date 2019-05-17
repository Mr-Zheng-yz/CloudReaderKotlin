package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.wan.WanHomeBean
import java.io.Serializable

data class WanHomeResultBean(
        val data: WanHomeBean,
        val errorCode: Int,
        val errorMsg: String
):Serializable