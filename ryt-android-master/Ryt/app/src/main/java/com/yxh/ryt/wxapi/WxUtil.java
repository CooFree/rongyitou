package com.yxh.ryt.wxapi;

import android.content.Context;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxh.ryt.Constants;

public class WxUtil {

	public static void wxpay(String order_no, String appid, String partnerid,
			String prepayid, String package1, String noncestr,
			String timestamp, String sign) {
		AppApplication.req.appId = appid;
		AppApplication.req.partnerId = partnerid;
		AppApplication.req.prepayId = prepayid;
		AppApplication.req.packageValue = package1;
		AppApplication.req.nonceStr = noncestr;
		AppApplication.req.timeStamp = timestamp;
		AppApplication.req.extData = order_no;
		AppApplication.req.sign = sign;
		AppApplication.api.sendReq(AppApplication.req);
	}

	public static void wxlogin() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "ryt";
		AppApplication.api.sendReq(req);
	}

	public static boolean regAndCheckWx(Context context) {
		if (AppApplication.api == null) {
			AppApplication.api = WXAPIFactory.createWXAPI(context,
					Constants.APP_ID, true);
		}
		if (!AppApplication.api.isWXAppInstalled()) {
			// 提醒用户没有按照微信
			Toast.makeText(context, "请先安装微信客户端！", Toast.LENGTH_SHORT).show();
			return false;
		}
		AppApplication.api.registerApp(Constants.APP_ID);
		boolean isPaySupported = AppApplication.api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (!isPaySupported)
			Toast.makeText(context, "微信客户端版本不支持微信支付！", Toast.LENGTH_SHORT)
					.show();
		return isPaySupported;
	}

}
