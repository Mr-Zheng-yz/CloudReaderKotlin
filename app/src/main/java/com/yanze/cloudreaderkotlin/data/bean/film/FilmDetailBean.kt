package com.yanze.cloudreaderkotlin.data.bean.film

import java.io.Serializable

/**
 * 详情，剧照，演员等...
 */
data class FilmDetailBean(
        val actors: List<ActorBean>,
        val bigImage: String,
        val commentSpecial: String,
        val director: Director,
        val hotRanking: Int,
        val img: String,
        val is3D: Boolean,
        val isDMAX: Boolean,
        val isEggHunt: Boolean,
        val isFilter: Boolean,
        val isIMAX: Boolean,
        val isIMAX3D: Boolean,
        val isTicket: Boolean,
        val message: String,
        val mins: String, //时长
        val movieId: Int,
        val name: String, //电影名
        val nameEn: String,
        val overallRating: Double, //评分
        val personCount: Int, //评分人数
        val releaseArea: String, //中国
        val releaseDate: String,
        val sensitiveStatus: Boolean,
        val showCinemaCount: Int,
        val showDay: Int,
        val showtimeCount: Int,
        val stageImg: StageImgBean, //剧照
        val story: String,
        val style: StyleBean,
        val totalNominateAward: Int,
        val totalWinAward: Int,
        val type: List<String>, //类型
        val url: String,
        val video: VideoBean? //预告片
) : Serializable {
    data class StyleBean(
            val isLeadPage: Int,
            val leadImg: String,
            val leadUrl: String
    ) : Serializable

    /**
     * "count": 30,
     * "hightUrl": "https://vfx.mtime.cn/Video/2019/05/23/mp4/190523092929709049.mp4",
     *"img": "http://img5.mtime.cn/mg/2019/05/23/092947.59377737_235X132X4.jpg",
     *"title": "哥斯拉2：怪兽之王 终极预告",
     *"url": "https://vfx.mtime.cn/Video/2019/05/23/mp4/190523092929709049_480.mp4",
     *"videoId": 74838,
     *"videoSourceType": 1
     */
    data class VideoBean(
            val count: Int,
            val hightUrl: String,
            val img: String,
            val title: String,
            val url: String,
            val videoId: Int,
            val videoSourceType: Int
    ) : Serializable

    /**
     * 剧照Bean
     */
    data class StageImgBean(
            val list: List<StageBean>
    ) : Serializable {
        data class StageBean(
                val imgId: Int,
                val imgUrl: String
        ) : Serializable
    }

    /**
     * 演员
     * "actorId": 922348,
    "img": "http://img31.mtime.cn/ph/2016/08/06/111744.53726121_1280X720X2.jpg",
    "name": "维拉·法梅加",
    "nameEn": "Vera Farmiga",
    "roleImg": "",
    "roleName": "艾玛"
     */
    data class ActorBean(
            val actorId: Int,
            val img: String,
            val name: String,
            val nameEn: String,
            val roleImg: String,
            val roleName: String
    ) : Serializable

    /**
     * 导演
     *
     * "directorId": 905046,
    "img": "http://img31.mtime.cn/ph/2014/02/22/194327.81724556_1280X720X2.jpg",
    "name": "迈克尔·道赫蒂",
    "nameEn": "Michael Dougherty"
     */
    data class Director(
            val directorId: Int,
            val img: String,
            val name: String,
            val nameEn: String
    ) : Serializable
}