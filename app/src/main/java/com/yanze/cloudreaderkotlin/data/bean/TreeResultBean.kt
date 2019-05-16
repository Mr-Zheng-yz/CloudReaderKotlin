package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.wan.TreeBean
import java.io.Serializable

data class TreeResultBean(
        val data: List<TreeBean>,
        val errorCode: Int,
        val errorMsg: String
):Serializable