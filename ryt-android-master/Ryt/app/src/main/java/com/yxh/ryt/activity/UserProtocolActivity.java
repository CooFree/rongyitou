package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class UserProtocolActivity extends Activity {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();
    private String id;
    private String name;
    private TextView top;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_protocol);
        back = (ImageButton) findViewById(R.id.ib_top_lf);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = (WebView) findViewById(R.id.acs_wb_all);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/UserProtocol.html");
    }
}
