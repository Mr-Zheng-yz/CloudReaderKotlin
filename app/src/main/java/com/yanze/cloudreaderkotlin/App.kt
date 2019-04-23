package com.yanze.cloudreaderkotlin

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instrance = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instrance: Context
    }

}