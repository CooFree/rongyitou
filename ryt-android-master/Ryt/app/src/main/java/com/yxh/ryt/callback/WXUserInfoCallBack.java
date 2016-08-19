package com.yxh.ryt.callback;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/31.
 */
public  abstract  class WXUserInfoCallBack extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        return string;
    }
}
