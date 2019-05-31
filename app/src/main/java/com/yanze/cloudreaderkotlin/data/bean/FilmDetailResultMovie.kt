package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.film.FilmDetailBasicBean
import java.io.Serializable

data class FilmDetailResultMovie(
        val data: FilmDetailBasicBean?,
        val code: String,
        val msg: String
):Serializable