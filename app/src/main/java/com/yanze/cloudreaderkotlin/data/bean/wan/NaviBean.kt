package com.yanze.cloudreaderkotlin.data.bean.wan

import java.io.Serializable

/**
 * Wan Android 导航数据
 */
data class NaviBean(
        var selected: Boolean,
        val articles: List<ArticleBean>,
        val cid: Int,
        val name: String) : Serializable {
    data class ArticleBean(
            val apkLink: String,
            val author: String,
            val chapterId: Int,
            val chapterName: String,
            val collect: Boolean,
            val courseId: Int,
            val desc: String,
            val envelopePic: String,
            val fresh: Boolean,
            val id: Int,
            val link: String,
            val niceDate: String,
            val origin: String,
            val prefix: String,
            val projectLink: String,
            val publishTime: Long,
            val superChapterId: Int,
            val superChapterName: String,
            val tags: List<Any>,
            val title: String,
            val type: Int,
            val userId: Int,
            val visible: Int,
            val zan: Int
    ) : Serializable
}