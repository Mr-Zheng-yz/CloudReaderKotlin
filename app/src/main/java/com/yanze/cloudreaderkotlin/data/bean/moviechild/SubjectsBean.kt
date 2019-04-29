package com.yanze.cloudreaderkotlin.data.bean.moviechild

import java.io.Serializable

/**
 * 豆瓣热映item详情
 */
data class SubjectsBean(
        var rating: RatingBean,
        var title: String,
        var collect_count: Int,
        var original_title: String,
        var subtype: String,
        var year: String,
        var images: ImageBean,
        var alt: String,
        var id: String,
        var genres: List<String>,
        var casts: List<PersonBean>,
        var directors: List<PersonBean>
): Serializable