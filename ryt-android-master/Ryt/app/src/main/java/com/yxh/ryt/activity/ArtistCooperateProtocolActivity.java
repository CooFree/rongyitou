package com.yxh.ryt.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.yxh.ryt.R;

/**
 * Created by Administrator on 2016/6/28.
 */
public class ArtistCooperateProtocolActivity extends BaseActivity {
    private WebView wv;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocol);
        wv = (WebView) findViewById(R.id.pl_wb);
        back = (ImageView) findViewById(R.id.pl_top_lf);
        wv.loadUrl("file:///android_asset/ArtistCooperateProtocol.html");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
