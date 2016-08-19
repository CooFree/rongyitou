package com.yxh.ryt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yxh.ryt.R;
import com.yxh.ryt.util.JsInterface;

public class AuctionProtocolActivity extends Activity {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();
    private String id;
    private String name;
    private TextView top;
    private ImageButton back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_protocol);
        back = (ImageButton) findViewById(R.id.ib_top_lf);
        title = (TextView) findViewById(R.id.tv_top_ct);
        title.setText("融艺投竞拍协议");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = (WebView) findViewById(R.id.acs_wb_all);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/AuctionProtocol.html");
    }
}
