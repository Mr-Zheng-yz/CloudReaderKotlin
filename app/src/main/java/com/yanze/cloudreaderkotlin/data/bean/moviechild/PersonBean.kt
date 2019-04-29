package com.yanze.cloudreaderkotlin.data.bean.moviechild

import java.io.Serializable

data class PersonBean(
        var alt:String,
        var type:String,//导演或演员
        var avatars:ImageBean,
        var name:String,
        var id:String): Serializable {
}