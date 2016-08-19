package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.AuctionProtocolActivity;
import com.yxh.ryt.activity.CreateSummaryActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.PayPageActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.LoadingUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Create;
import com.yxh.ryt.vo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class AuctionSummaryFragment extends BaseFragment{
	private WebView webView;
	private JsInterface jsInterface = new JsInterface();
	private String id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_createprogress, container, false);
		webView = (WebView) contextView.findViewById(R.id.acs_wb_all);
		webView.getSettings().setJavaScriptEnabled(true);
		return contextView;
	}


	public AuctionSummaryFragment() {
	}

	public AuctionSummaryFragment(String id) {
		this.id = id;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	protected void lazyLoad() {

	}

	@Override
	public void onResume() {
		super.onResume();
		webView.addJavascriptInterface(new JavaInterfaceDemo(), "demo1");
		CookieManager manager=CookieManager.getInstance();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			manager.setAcceptThirdPartyCookies(webView, true);
		}
		webView.loadUrl("file:///android_asset/auction.html");

	}
	class JavaInterfaceDemo {
		@JavascriptInterface
		public void clickOnAndroid1(String id) {
			loadData(id);
		}
		@JavascriptInterface
		public String fetchParamObject1() {
			return "{\"artWorkId\":\""+id+"\",\"currentUserId\":\""+AppApplication.gUser.getId()+"\",\"username\": \""+AppApplication.gUser.getUsername()+"\",\"password\": \""+AppApplication.gUser.getPassword()+"\" }";
		}
		@JavascriptInterface
		public void auctionProtocol() {
			Intent intent=new Intent(getActivity(),AuctionProtocolActivity.class);
			startActivity(intent);
		}
		@JavascriptInterface
		public void  finalPayment( String price, String action, String artWorkId) {
			Map<String,String> paramsMap=new HashMap<>();
			//paramsMap.put("userId", AppApplication.gUser.getId());
			//paramsMap.put("userId", "imhfp1yr4636pj49");
			paramsMap.put("money", price);
			paramsMap.put("action", action);
			paramsMap.put("type", "1");
			paramsMap.put("artWorkId", artWorkId);
			paramsMap.put("timestamp", System.currentTimeMillis() + "");
			try {
				AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
				paramsMap.put("signmsg", AppApplication.signmsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NetRequestUtil.post(Constants.BASE_PATH + "pay/main.do", paramsMap, new AttentionListCallBack() {
				@Override
				public void onError(Call call, Exception e) {
					e.printStackTrace();
					System.out.println("失败了");
				}

				@Override
				public void onResponse(Map<String, Object> response) {
					String url = response.get("url").toString();
					Intent intent=new Intent(getActivity(),PayPageActivity.class);
					intent.putExtra("url",url);
					startActivity(intent);
				}
			});
		}
		@JavascriptInterface
		public void comment(String artworkId,String currentUserId,String messageId,String fatherCommentId,String name) {
			if ("".equals(AppApplication.gUser.getId())) {
				Intent intent2 = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent2);
			} else {
				Intent intent = new Intent(getActivity(), ProjectCommentReply.class);
				intent.putExtra("fatherCommentId", fatherCommentId);
				intent.putExtra("messageId", messageId);
				intent.putExtra("flag", 0);
				intent.putExtra("name", name);
				intent.putExtra("artworkId", artworkId);
				intent.putExtra("currentUserId", AppApplication.gUser.getId());
				startActivity(intent);
			}
		}
	}
	private void loadData(final String id) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId", id);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "user.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))){
					Map<Object,Object> data= (Map<Object, Object>) response.get("data");
					User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(data.get("user")), User.class);
					if (user.getMaster()!=null){
						Intent intent =new Intent(getActivity(),ArtistIndexActivity.class);
						intent.putExtra("userId", id);
						intent.putExtra("name",user.getName());
						startActivity(intent);
					}else {
						Intent intent =new Intent(getActivity(),UserIndexActivity.class);
						intent.putExtra("userId", id);
						intent.putExtra("name", user.getName());
						startActivity(intent);
					}
				}else if ("000000".equals(response.get("resultCode"))){
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								loadData(id);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}
			}
		});
	}
}