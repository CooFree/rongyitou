package com.yxh.ryt.util;

import com.yxh.ryt.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class NetRequestUtil<T> {

    public static <T> void post(final String url, final Map<String, String> t, final Callback<T> callback) {
        System.out.println(AppApplication.getSingleGson().toJson(t));
        new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .postString()
                        .url(url)
                        .content(AppApplication.getSingleGson().toJson(t))
                        .build()
                        .connTimeOut(50000)
                        .readTimeOut(50000)
                        .writeTimeOut(50000)
                        .execute(callback);
            }
        }.start();

    }

    public static <T> void get(final String url, final Map<String, String> paramsMap, final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                GetBuilder getBuilder = OkHttpUtils.get();
                getBuilder.url(url);
                Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    getBuilder.addParams(entry.getKey(), entry.getValue());
                }
                RequestCall build = getBuilder.build();
                build.execute(callback);
            }
        }.start();
    }

    public static <T> void postFile(final String url, final String fileKey, final Map<String, File> fileMap, final Map<String, String> paramsMap, final Map<String, String> headers, final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                PostFormBuilder post = OkHttpUtils.post();
                Iterator<Map.Entry<String, File>> it = fileMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, File> entry = it.next();
                    post.addFile(fileKey, entry.getKey(), entry.getValue());
                }
                post.url(url);
                post.params(paramsMap);
                post.headers(headers);
                RequestCall build = post.build();
                build.connTimeOut(500000);
                build.readTimeOut(500000);
                build.writeTimeOut(500000);
                build.execute(callback);
            }
        }.start();
    }

    public static <T> void postMulFile(final String url, final Map<String, Map<String, File>> fileMap, final Map<String, String> paramsMap, final Map<String, String> headers, final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                PostFormBuilder post = OkHttpUtils.post();
                Iterator<Map.Entry<String, Map<String, File>>> iterator = fileMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Map<String, File>> next = iterator.next();
                    Map<String, File> value = next.getValue();
                    Iterator<Map.Entry<String, File>> iterator1 = value.entrySet().iterator();
                    while (iterator1.hasNext()) {
                        Map.Entry<String, File> next1 = iterator1.next();
                        post.addFile(next.getKey(), next1.getKey(), next1.getValue());
                    }
                }
                post.url(url);
                post.params(paramsMap);
                post.headers(headers);
                RequestCall build = post.build();
                build.connTimeOut(500000);
                build.readTimeOut(500000);
                build.writeTimeOut(500000);
                build.execute(callback);
            }
        }.start();
    }

    //*下载*//*
    public static <T> void download(final String url, final Map<String, String> paramsMap, final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                PostFormBuilder post = OkHttpUtils.post();
                post.url(url)
                        .params(paramsMap)
                        .build();
                RequestCall build = post.build();
                build.connTimeOut(500000);
                build.readTimeOut(500000);
                build.writeTimeOut(500000);
                build.execute(callback);

            }
        }.start();
    }
    public static <T> void downloadImage(final String url,final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                if(url != null ){
                    GetBuilder post = OkHttpUtils.get();
                    post.url(url)
                            .build();
                    RequestCall build = post.build();
                    build.connTimeOut(500000);
                    build.readTimeOut(500000);
                    build.writeTimeOut(500000);
                    build.execute(callback);
                }
            }
        }.start();
    }
}