package com.yanze.cloudreaderkotlin.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yanze.cloudreaderkotlin.R;

/**
 * Created by Administrator on 2017/11/28.
 */

public class MyDialog extends Dialog {
    private Context context;
    private static MyDialog dialog;
    private ImageView ivProgress;


    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    //显示dialog的方法
    public static MyDialog showDialog(Context context) {
        dialog = new MyDialog(context, R.style.MyDialog);//dialog样式
        dialog.setContentView(R.layout.dialog_layout);//dialog布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return dialog;
    }

    //10秒加载失败后自动关闭加载框并提示
    public void SHOW() {
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 20000);//20秒之后关闭

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && dialog != null) {
//            ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
//            ivProgress.startAnimation(animation);
//        }
    }

    public void show() {
        super.show();
        if (dialog != null) {
            ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
            ivProgress.startAnimation(animation);
        }
    }

    public void dismiss() {
        super.dismiss();
        if (dialog != null && ivProgress != null) {
            ivProgress.clearAnimation();
        }
    }
}
