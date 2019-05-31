package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.film.FilmeItemBean
import java.io.Serializable

data class MtimeMovieResultBean(
        val bImg: String,
        val date: String,
        val hasPromo: Boolean,
        val lid: Int,
        val ms: List<FilmeItemBean>,
        val newActivitiesTime: Int,
        val totalComingMovie: Int,
        val voucherMsg: String
):Serializable