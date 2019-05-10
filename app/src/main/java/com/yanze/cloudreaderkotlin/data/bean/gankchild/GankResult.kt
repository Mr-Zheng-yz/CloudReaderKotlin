package com.yanze.cloudreaderkotlin.data.bean.gankchild

import java.io.Serializable

data class GankResult(
//        val _id: String,
        val createdAt: String,
        val desc: String,
        val publishedAt: String,
        val source: String,
        val type: String,
        val url: String,
        val used: Boolean,
        val who: String?,
        val images: List<String>?
) : Serializable