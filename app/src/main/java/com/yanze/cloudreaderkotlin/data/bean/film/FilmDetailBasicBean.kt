package com.yanze.cloudreaderkotlin.data.bean.film

import java.io.Serializable

data class FilmDetailBasicBean(
        val basic: FimDetailBean,
        val boxOffice: BoxOfficeBean,
        val playState: String
): Serializable {
    /**
     * 票房
     * "movieId": 213190,
    "ranking": 53,
    "todayBox": 0,
    "todayBoxDes": "",
    "todayBoxDesUnit": "",
    "totalBox": 1618171340,
    "totalBoxDes": "1618.17",
    "totalBoxUnit": "累计票房(万)"
     */
    data class BoxOfficeBean(
            val movieId: Int,
            val ranking: Int,
            val todayBox: Int,
            val todayBoxDes: String,
            val todayBoxDesUnit: String,
            val totalBox: Int,
            val totalBoxDes: String,
            val totalBoxUnit: String
    ):Serializable
}