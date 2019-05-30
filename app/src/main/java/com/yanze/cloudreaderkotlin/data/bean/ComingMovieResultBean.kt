package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.film.ComingFilmeBean
import java.io.Serializable

data class ComingMovieResultBean(
        val attention: List<ComingFilmeBean>,
        val moviecomings: List<ComingFilmeBean>
):Serializable