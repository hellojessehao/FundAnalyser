package com.jesseandroid.fundanalyser.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @Description: 吐司工具类
 * @author: zhangshihao
 * @date: 2021/2/26
 */
public class ToastUtils {

    private static volatile ToastUtils instance;
    private static Toast mToast;

    private ToastUtils(Context context){
        mToast = new Toast(context.getApplicationContext());
    }

    public static synchronized ToastUtils getInstance(Context context){
        if(context == null){
            return instance;
        }
        if(instance == null){
            synchronized (ToastUtils.class){
                if(instance == null){
                    instance = new ToastUtils(context);
                }
            }
        }
        return instance;
    }

    public void show(String msg){
        mToast.setText(msg);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void show(String msg,int length){
        mToast.setText(msg);
        mToast.setDuration(length);
        mToast.show();
    }

    public void show(int msgId){
        mToast.setText(msgId);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void show(int msgId,int length){
        mToast.setText(msgId);
        mToast.setDuration(length);
        mToast.show();
    }

}
