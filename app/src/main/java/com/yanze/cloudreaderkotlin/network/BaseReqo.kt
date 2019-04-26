package com.yanze.cloudreaderkotlin.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseReqo {

    fun <T> transform(observable: Observable<T>): Observable<T> {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

//    companion object {
//        @JvmStatic
//        fun <T> transform(observable: Observable<T>): Observable<T> {
//            return observable
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//        }
//    }
}