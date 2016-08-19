package com.yxh.ryt.callback;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/31.
 */
public  abstract  class AttentionListCallBack extends Callback<Map<String,Object>> {
    @Override
    public Map<String,Object> parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        System.out.println(string);
        Map<String, Object> resultMap = AppApplication.getSingleGson().fromJson(string, new TypeToken<Map<String,Object>>() {
        }.getType());
        return resultMap;
    }
}
