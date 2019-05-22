package com.yanze.cloudreaderkotlin.data.bean.wan

import java.io.Serializable

data class ArticlesBean(
        val apkLink: String,
        val author: String,
        val chapterId: Int,
        val chapterName: String?,
        var collect: Boolean = false,
        val courseId: Int,
        val desc: String,
        val envelopePic: String?,
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
        val tags: List<TagsBean>?,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int,
        var isShowImage : Boolean = true

) : Serializable