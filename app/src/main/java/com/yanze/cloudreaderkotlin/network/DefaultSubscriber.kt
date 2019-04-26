package com.yanze.cloudreaderkotlin.network

import com.baize.fireeyekotlin.utils.log.L
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

abstract class DefaultSubscriber<T> : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        _onNext(t)
    }

    override fun onError(e: Throwable) {
        val errorMessage: String = if (e is HttpException) {
            "HTTP错误"
        } else if (e is ConnectException || e is UnknownHostException) {
            "网络错误"
        } else if (e is InterruptedIOException) {
            "链接超时"
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {
            "解析错误"
        } else {
            e.message!!
        }
        L.e(msg = "LocalizeMsg:${e.localizedMessage} && message:${e.message}")
        _onError(errorMessage)
    }

    abstract fun _onError(errMsg: String)

    abstract fun _onNext(entity: T)
}