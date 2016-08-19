package com.yxh.ryt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yxh.ryt.Constants;

/**
 * Created by 吴洪杰 on 2016/4/6.
 */
public class WxLoginBroadcastReciver extends BroadcastReceiver {
    private WxLoginCallBack callBack;

    public WxLoginBroadcastReciver(WxLoginCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Constants.WX_LOGIN_ACTION.equals(action)) {
            String wxuserStr = intent.getExtras().getString("wxuser");
            System.out.println("2222222222222222222222222222222"+wxuserStr);
            callBack.response(wxuserStr);
        }
    }
    public interface WxLoginCallBack{
        public void response(String wxUser);
    }
}
