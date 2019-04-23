package com.yanze.cloudreaderkotlin.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.yanze.cloudreaderkotlin.App

fun Context.showToast(msg: String):Toast{
    val toast = Toast.makeText(App.instrance, msg, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return toast
}