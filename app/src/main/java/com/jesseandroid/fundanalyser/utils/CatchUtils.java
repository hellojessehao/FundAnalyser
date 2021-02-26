package com.jesseandroid.fundanalyser.utils;

import com.jesseandroid.fundanalyser.network.NetThread;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @Description: 抓取数据工具类
 * @author: zhangshihao
 * @date: 2021/2/25
 */
public class CatchUtils {

    private static volatile CatchUtils instance;
    private final String UA_PC = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    private final int timeout = 30*1000;

    private CatchUtils(){}

    public static synchronized CatchUtils getInstance(){
        if(instance == null){
            synchronized (CatchUtils.class){
                if(instance == null){
                    instance = new CatchUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取网址的Document
     * @param url 网址
     * @return document
     */
    public Document catchDocument(String url){
        try{
            Connection connection = Jsoup.connect(url);
            connection.userAgent(UA_PC);
            connection.timeout(timeout);
            connection.method(Connection.Method.POST);
            return connection.post();
        }catch (IOException ie){
            LogUtils.e(ie.toString());
        }
        return null;
    }

}
