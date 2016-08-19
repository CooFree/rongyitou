package com.yxh.ryt.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.activity.BaseActivity;
import com.yxh.ryt.callback.WXAccessTokenCallBack;
import com.yxh.ryt.callback.WXUserInfoCallBack;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppApplication.api == null) {
            AppApplication.api = WXAPIFactory.createWXAPI(this,
                    Constants.APP_ID, true);
        }
        AppApplication.api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        if (resp != null) {
            AppApplication.resp = resp;
        }
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (AppApplication.resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                    String wxCode = ((SendAuth.Resp) AppApplication.resp).code;
                    getAccessTokenByCode(wxCode);
                }
                finish();
                Intent intent = new Intent("android.intent.action.WX_Login_BROADCAST");
                AppApplication.getSingleContext().sendBroadcast(intent);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //result = "取消登陆";
               // Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //result = "登陆被拒绝";
               // Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                //result = "登陆返回";
               // Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }

    }

    //获取用户信息
    public void getUserInfo(String accessToken, String openId) {
        String url = Constants.WX_BASE_PATH + "userinfo";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("access_token", accessToken);
        paramsMap.put("openid", openId);
        NetRequestUtil.get(url, paramsMap, new WXUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (response != null && !response.contains("errcode")) {
                    Intent intent = new Intent(Constants.WX_LOGIN_ACTION);
                    intent.putExtra("wxuser", response);
                    WXEntryActivity.this.sendBroadcast(intent);
                } else {
                    ToastUtil.show(WXEntryActivity.this, "请求发生异常！", Toast.LENGTH_SHORT);
                }
                finish();
            }
        });
    }

    //通过code获取access_token
    public void getAccessTokenByCode(String code) {
        String url = Constants.WX_BASE_PATH + "oauth2/access_token";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("appid", Constants.APP_ID);
        paramsMap.put("secret", Constants.APP_SECRET);
        paramsMap.put("code", code);
        paramsMap.put("grant_type", "authorization_code");
        NetRequestUtil.get(url, paramsMap, new WXAccessTokenCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response != null && !response.containsKey("errcode")) {
                    getUserInfo((String) response.get("access_token"), (String) response.get("openid"));
                } else {
                    ToastUtil.show(WXEntryActivity.this, "请求发生异常！", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AppApplication.api.handleIntent(intent, this);
    }

}
