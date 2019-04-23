package com.yanze.cloudreaderkotlin.data.bean

import com.yanze.cloudreaderkotlin.data.bean.moviechild.ImageBean
import com.yanze.cloudreaderkotlin.data.bean.moviechild.PersonBean
import com.yanze.cloudreaderkotlin.data.bean.moviechild.RatingBean

data class MovieDetailBean(
        var rating:RatingBean,
        var reviews_count:Int,
        var wish_count:Int,
        var douban_site:String,
        var year:String,
        var images:ImageBean,
        var alt:String,
        var id:String,
        var mobile_url:String,
        var title:String,
        var do_count:Int,
        var share_url:Int,
        var seasons_count:Int,
        var schedule_url:String,
        var episodes_count:Int,
        var collect_count:Int,
        var current_season:String,
        var original_title:String,
        var summary:String,
        var subtype:String,
        var comments_count:Int,
        var ratings_count:Int,
        var countries:List<String>,
        var genres:List<String>,
        var casts:List<PersonBean>,
        var directors:List<PersonBean>,
        var aka:List<String>
)