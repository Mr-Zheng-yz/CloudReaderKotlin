package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.moviechild.ImageBean
import com.yanze.cloudreaderkotlin.data.bean.moviechild.PersonBean
import com.yanze.cloudreaderkotlin.data.bean.moviechild.RatingBean
import java.io.Serializable

data class MovieDetailBean(
        val aka: List<String>,
        val alt: String,
        val casts: List<PersonBean>,
        val collect_count: Int,
        val comments_count: Int,
        val countries: List<String>,
        val current_season: Any,
        val directors: List<PersonBean>,
        val do_count: Any,
        val douban_site: String,
        val episodes_count: Any,
        val genres: List<String>,
        val id: String,
        val images: ImageBean,
        val mobile_url: String,
        val original_title: String,
        val rating: RatingBean,
        val ratings_count: Int,
        val reviews_count: Int,
        val schedule_url: String,
        val seasons_count: Any,
        val share_url: String,
        val subtype: String,
        val summary: String,
        val title: String,
        val wish_count: Int,
        val year: String
) : Serializable