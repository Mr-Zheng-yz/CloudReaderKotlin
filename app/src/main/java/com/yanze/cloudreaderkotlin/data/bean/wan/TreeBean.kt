package com.yanze.cloudreaderkotlin.data.bean.wan

import java.io.Serializable

data class TreeBean(
        val children: List<TreeChildren>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
) : Serializable {
    data class TreeChildren(
//            val children: List<Any>,
            val courseId: Int,
            val id: Int,
            val name: String,
            val order: Int,
            val parentChapterId: Int,
            val userControlSetTop: Boolean,
            val visible: Int
    ) : Serializable
}