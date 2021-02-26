package com.jesseandroid.fundanalyser.network;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.jesseandroid.fundanalyser.utils.LogUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @Description: 用于网络请求的线程
 * @author: zhangshihao
 * @date: 2021/2/25
 */
public class NetThread<T> extends Thread implements Runnable{

    /**
     * 是否正在运行
     */
    private boolean isRunning = false;
    /**
     * 请求网址
     */
    private String url;
    /**
     * 请求方式 GET POST 等
     */
    private Connection.Method method = Connection.Method.GET;
    /**
     * 请求超时时长
     */
    private int timeout = 30*1000;
    /**
     * 电脑UA
     */
    private final String UA_PC = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    /**
     * 线程通信Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    setSuccess((T)msg.obj);
                    break;
                case FAILED:
                    setFailed((String) msg.obj);
                    LogUtils.e((String) msg.obj);
                    break;
                case ERROR:
                    setError((Throwable) msg.obj);
                    LogUtils.e(msg.obj.toString());
                    break;
            }
        }
    };
    /**
     * 回调接口对象列表
     */
    private List<INetResult<T>> netResultList = new ArrayList<>();
    /**
     * 操作执行结果识别码
     */
    private final int SUCCESS = 0;
    private final int FAILED = 1;
    private final int ERROR = 2;
    /**
     * 实现DoubleCheck单例模式
     */
    private static volatile NetThread instance;

    private NetThread(){}

    public static synchronized NetThread getInstance(){
        if(instance == null){
            synchronized (NetThread.class){
                if(instance == null){
                    instance = new NetThread();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        if(isRunning){
            LogUtils.e("NetRunnable is running");
            return;
        }
        try{
            Connection connection = Jsoup.connect(url);
            connection.timeout(timeout);
            connection.method(method);
            //仿浏览器
            connection.userAgent(UA_PC);
            Map<String,String> headers = new HashMap<>();
            headers.put("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9," +
                            "image/avif,image/webp,image/apng,*/*;q=0.8," +
                            "application/signed-exchange;v=b3;q=0.9");
            headers.put("Accept-Encoding","gzip, deflate");
            headers.put("Accept-Language","zh-CN,zh;q=0.9");
            headers.put("Cache-Control","max-age=0");
            headers.put("Connection","keep-alive");
            headers.put("Cookie","td_cookie=18446744072873324785; st_si=29162940984575; " +
                    "st_asi=delete; EMFUND1=null; EMFUND2=null; EMFUND3=null; EMFUND4=null;" +
                    " EMFUND5=null; EMFUND6=null; EMFUND7=null; EMFUND8=null; EMFUND0=null;" +
                    " qgqp_b_id=4bb3c5f764f9fd4856f9d39fd24f5c8a; ASP.NET_SessionId=wnes2h0boupkptc4iyozw5nm;" +
                    " td_cookie=18446744072880523311; EMFUND9=02-25 16:42:10@#$%u534E%u5B89%u6838%u5FC3%u4F18%u9009%u6DF7%u5408@%23%24040011;" +
                    " _adsame_fullscreen_16928=1; st_pvi=17668553122893; st_sp=2021-01-19%2011%3A20%3A44;" +
                    " st_inirUrl=http%3A%2F%2Ffund.eastmoney.com%2Fa%2F202101191780725556.html; st_sn=19;" +
                    " st_psi=20210226155611881-0-9481213735");
            connection.headers(headers);

            Document document = connection.post();
            Message message = Message.obtain();
            if(document == null){
                message.what = FAILED;
                message.obj = "document is null!";
                handler.sendMessage(message);
            }else{
                message.what = SUCCESS;
                message.obj = document;
                handler.sendMessage(message);
            }
        }catch (IOException ie){
            Message msg = Message.obtain();
            msg.what = ERROR;
            msg.obj = ie;
            handler.sendMessage(msg);
        }finally {
            isRunning = false;
        }
    }

    private void setSuccess(T result){
        for(INetResult<T> netResult : netResultList){
            netResult.onSuccess(result);
        }
    }

    private void setFailed(String msg){
        for(INetResult<T> netResult : netResultList){
            netResult.onFailed(msg);
        }
    }

    private void setError(Throwable throwable){
        for(INetResult<T> netResult : netResultList){
            netResult.onError(throwable);
        }
    }

    public NetThread<T> addResultListener(INetResult<T> resultListener){
        if(resultListener == null || netResultList.contains(resultListener)){
            return this;
        }
        netResultList.add(resultListener);
        return this;
    }

    public void removeResultListener(INetResult<T> resultListener){
        if(resultListener == null || !netResultList.contains(resultListener)){
            return;
        }
        netResultList.remove(resultListener);
    }

    public String getUrl() {
        return url;
    }

    public NetThread<T> setUrl(String url) {
        this.url = url;
        return this;
    }

    public Connection.Method getMethod() {
        return method;
    }

    public NetThread<T> setMethod(Connection.Method method) {
        this.method = method;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public NetThread<T> setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
