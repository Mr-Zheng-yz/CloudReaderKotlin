package com.yanze.cloudreaderkotlin.data.bean.film

import java.io.Serializable

data class FilmDetailBasicBean(
        val basic: FilmDetailBean,
        val boxOffice: BoxOfficeBean?,
        val playState: String,
        val related: RelatedBean?
) : Serializable {
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
            val todayBox: String,
            val todayBoxDes: String,
            val todayBoxDesUnit: String,
            val totalBox: String,
            val totalBoxDes: String,
            val totalBoxUnit: String
    ) : Serializable

    data class RelatedBean(var goodsCount: Int,
                           var relatedUrl: String,
                           var type: Int
//                           var goodsList: GoodsListBean?
    ) : Serializable {

        /**
         * background :
         * goodsId : 107042
         * goodsTip : 自营
         * goodsUrl : https://mall-wv.mtime.cn/#!/commerce/item/107042/
         * iconText :
         * image : http://img5.mtime.cn/mg/2018/04/18/184415.72879699_294X294X4.jpg
         * longName : 复仇者联盟3 经典漫画复古马克杯
         * marketPrice : 0
         * marketPriceFormat : 0.00
         * minSalePrice : 6900
         * minSalePriceFormat : 69.00
         * name : 复联3 经典漫画复古马克杯
         */
//        data class GoodsListBean(var background: String,
//                                 var goodsId: Int,
//                                 var goodsTip: String,
//                                 var goodsUrl: String,
//                                 var iconText: String,
//                                 var image: String,
//                                 var longName: String,
//                                 var marketPriceFormat: String,
//                                 var minSalePriceFormat: String,
//                                 var name: String) : Serializable
    }
}