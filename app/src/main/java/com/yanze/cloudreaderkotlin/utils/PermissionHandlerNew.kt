package com.yanze.cloudreaderkotlin.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * // 存储权限
 * Manifest.permission.WRITE_EXTERNAL_STORAGE
 * // 相机
 * Manifest.permission.CAMERA
 * // 获得经纬度1
 * Manifest.permission.ACCESS_FINE_LOCATION
 * // 获得经纬度2
 * Manifest.permission.ACCESS_COARSE_LOCATION
 * // 获取手机权限 为了获得uuid：
 * Manifest.permission.READ_PHONE_STATE
 * // 应用列表
 * Manifest.permission.GET_PACKAGE_SIZE
 */
class PermissionHandlerNew {

    companion object {

        private const val PERMISSION_CODE = 10010
        private const val PERMISSION_CODE_ONE = 10011

        /**
         * 检查相册权限
         */
        fun isHandlePermission(activity: Activity, permission: String):Boolean {
            return if (!isPermission(activity, permission)) {
                //有权限
                true
            }else{
                val permissionCheck = ContextCompat.checkSelfPermission(activity, permission)
                //没有权限if
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_CODE_ONE) //请求权限
                }
                false
            }
        }

        /**
         * 检查权限
         */
        private fun isPermission(activity: Activity, permission: String, permission2: String? = null): Boolean {
            val permissionCheck = ContextCompat.checkSelfPermission(activity, permission)
            if (permission2 == null) { //单个权限
                return permissionCheck == PackageManager.PERMISSION_GRANTED
            }
            //两个权限
            val permissionCheck2 = ContextCompat.checkSelfPermission(activity, permission2)
            //有一个没有授权就作为没有权限处理
            return permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED
        }


    }


}