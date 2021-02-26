package com.jesseandroid.fundanalyser.utils;


import android.util.Log;

/**
 * @Description: 日志打印工具类
 * @author: zhangshihao
 * @date: 2021/2/25
 */
public class LogUtils {

    private static final String TAG = "zsh";

    public static void d(String msg){
        Log.d(TAG,msg);
    }

    public static void i(String msg){
        Log.i(TAG,msg);
    }

    public static void e(String msg){
        Log.e(TAG,msg);
    }

}
