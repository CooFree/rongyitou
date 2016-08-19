package com.yxh.ryt.util;

import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.vo.Artwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/8.
 */
public class SessionLogin {
    private CodeCallBack codeCallBack;

    public SessionLogin(CodeCallBack codeCallBack) {
        this.codeCallBack = codeCallBack;
    }

    public void resultCodeCallback(String loginState){
        Map<String,String> paramsMap=new HashMap<>();
        if ("1".equals(loginState)){
            paramsMap.put("username",AppApplication.gUser.getUsername());
            paramsMap.put("password", AppApplication.gUser.getPassword());
        }else if ("2".equals(loginState)){
            paramsMap.put("unionid", (String) SPUtil.get(AppApplication.getSingleContext(), "current_unionid", ""));
        }
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg=EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "j_spring_security_check", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("444444失败了");
            }
            @Override
            public void onResponse(Map<String, Object> response) {
                if (response!=null){
                    codeCallBack.getCode(response.get("resultCode")+"");
                }
            }
        });
    }

    public interface CodeCallBack{
        public void getCode(String code);
    }
}
