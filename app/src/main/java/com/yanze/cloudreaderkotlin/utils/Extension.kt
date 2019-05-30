package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.yanze.cloudreaderkotlin.App

/**
 * 写自己的一些扩展方法。
 * 在jetpack中可以先看看Android KTX库中有没有实现，如果没有再自己来写
 */

fun Context.showToast(msg: String):Toast{
    val toast = Toast.makeText(App.instrance, msg, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
//    DensityUtil.setViewMargin(toast.view.findViewById(android.R.id.message),true,30,30,60,60)
    toast.show()
    return toast
}

