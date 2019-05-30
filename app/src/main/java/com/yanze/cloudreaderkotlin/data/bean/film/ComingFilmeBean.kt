package com.yanze.cloudreaderkotlin.data.bean.film

import java.io.Serializable

data class ComingFilmeBean(
        val actor1: String,
        val actor2: String,
        val director: String,
        val id: Int,
        val image: String,
        val isFilter: Boolean,
        val isTicket: Boolean,
        val isVideo: Boolean,
        val locationName: String,
        val rDay: Int,
        val rMonth: Int,
        val rYear: Int,
        val releaseDate: String,
        val title: String,
        val type: String,
        val videoCount: Int,
        val videos: List<VideoBean>,
        val wantedCount: Int
) : Serializable {
    data class VideoBean(
            val hightUrl: String,
            val image: String,
            val length: Int,
            val title: String,
            val url: String,
            val videoId: Int
    ) : Serializable
}