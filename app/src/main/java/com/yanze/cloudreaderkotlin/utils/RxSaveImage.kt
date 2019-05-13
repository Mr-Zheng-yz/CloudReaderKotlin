package com.yanze.cloudreaderkotlin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.baize.fireeyekotlin.utils.log.L
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

/**
 * 下载图片，重复下载提示已存在
 */
object RxSaveImage {

    @SuppressLint("CheckResult")
    private fun saveImageAndPathObservable(context: Activity, url: String, title: String): Observable<String> {
        return Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                //检查图片是否已存在
                val appDir = File(Environment.getExternalStorageDirectory(), "云阅相册")
                if (appDir.exists()) {
                    val file = File(appDir, getFileName(url, title))
                    if (file.exists()) {
                        emitter.onError(Exception("图片已存在"))
                    }
                }
                if (!appDir.exists()) {
                    //没有目录创建目录
                    appDir.mkdir()
                }
                val file = File(appDir, getFileName(url, title)) //图片保存目标文件

                try {
                    //下载
                    var fileDo = Glide.with(context)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get()
                    if (fileDo != null) {
                        //复制图片（将Glide中下载到缓存的图片copy到图库）
                        copyFileTo(fileDo.absolutePath, file.path)

                        //通知图库更新
                        val uri = Uri.fromFile(file)
                        val scannerIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri)
                        context.sendBroadcast(scannerIntent)
                    } else {
                        emitter.onError(Exception("无法下载到图片"))
                    }
                } catch (e: Exception) {
                    emitter.onError(e)
                }
                emitter.onNext("")
                emitter.onComplete()
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun saveImageToGallery(context: Activity, imageUrl: String, imageTitle: String) {
        context.showToast("开始下载图片")
        saveImageAndPathObservable(context, imageUrl, imageTitle)
                .subscribe({
                    val appDir = File(Environment.getExternalStorageDirectory(), "云阅相册")
                    context.showToast("已保存至${appDir.absolutePath}")
                }, {
                    context.showToast("${it.message}")
                })
    }

    fun copyFileTo(oldPath: String, newPath: String) {
        try {
            val fis = FileInputStream(oldPath)
            val fos = FileOutputStream(newPath)
            fis.copyTo(fos)
            fos.flush()
            fis.close()
            fos.close()
        } catch (e: Exception) {
            L.e(msg = "${e.message}")
        }
    }

    private fun getFileName(imageUrl: String, mImageTitle: String): String {
        return when {
            imageUrl.contains(".gif") -> {
                "${mImageTitle.replace("/", "-")}.gif"
            }
            imageUrl.contains(".png") -> {
                "${mImageTitle.replace("/", "-")}.png"
            }
            imageUrl.contains(".jpeg") -> {
                "${mImageTitle.replace("/", "-")}.jpeg"
            }
            else -> {
                "${mImageTitle.replace("/", "-")}.jpg"
            }
        }
//        return when {
//            imageUrl.contains(".gif") -> "${handleStr(imageUrl)}.gif"
//            imageUrl.contains(".png") -> "${handleStr(imageUrl)}.png"
//            imageUrl.contains(".jpeg") -> "${handleStr(imageUrl)}.jpeg"
//            else -> "${handleStr(imageUrl)}.jpg"
//        }
    }

    //将图片链接转变为唯一命名
    private fun handleStr(imageUrl: String): String {
        if (imageUrl.contains("https://")) {
            val tageImg1 = imageUrl.replace("https://", "")
            return tageImg1.replace(".", "-")
        } else {
            val tageImg1 = imageUrl.replace("http://", "")
            return tageImg1.replace(".", "-")
        }
    }

}