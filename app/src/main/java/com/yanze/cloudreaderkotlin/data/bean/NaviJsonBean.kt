package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.wan.NaviBean
import java.io.Serializable

data class NaviJsonBean(
        val data: List<NaviBean>,
        val errorCode: Int,
        val errorMsg: String) : Serializable {
}