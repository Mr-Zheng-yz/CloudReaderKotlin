package com.yanze.cloudreaderkotlin.data.bean.moviechild

import java.io.Serializable

data class RatingBean(var max: Int
                      , var average: Double
                      , var stars: String
                      , var min: Int): Serializable