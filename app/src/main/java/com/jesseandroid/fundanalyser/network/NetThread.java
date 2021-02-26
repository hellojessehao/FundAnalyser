package com.jesseandroid.fundanalyser.network;

/**
 * @Description: 用于网络请求的线程
 * @author: zhangshihao
 * @date: 2021/2/25
 */
public abstract class NetThread<T> extends Thread{

    @Override
    public void run() {
        super.run();
        doNetRequest();
    }

    protected void doNetRequest(T bean){
    }

}
