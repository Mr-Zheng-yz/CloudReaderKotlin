package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.moviechild.SubjectsBean
import java.io.Serializable

data class HotMovieBean(
        var count: Int,
        var start: Int,
        var total: Int,
        var title: String,
        var subjects: List<SubjectsBean>
) : Serializable