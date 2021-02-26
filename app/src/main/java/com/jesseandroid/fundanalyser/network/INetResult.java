package com.jesseandroid.fundanalyser.network;

/**
 * @Description: 获取网络请求结果接口
 * @author: zhangshihao
 * @date: 2021/2/26
 */
public interface INetResult<T> {

    void onSuccess(T result);

    void onFailed(String msg);

    void onError(Throwable throwable);

}
