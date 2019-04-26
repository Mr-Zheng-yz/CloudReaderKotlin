package com.yanze.cloudreaderkotlin.data

data class Resource<T>(val state: Int, val data: T?, val message: String?) {

    companion object {
        const val SUCCESS = 0
        const val ERROR = 1
        const val LOADING = 2

        fun <T> success(data: T?) = Resource(SUCCESS, data, null)

        fun <T> error(msg: String?, data: T?) = Resource(ERROR, data, msg)

        fun <T> loading(data: T?) = Resource(LOADING, data, null)

    }
}