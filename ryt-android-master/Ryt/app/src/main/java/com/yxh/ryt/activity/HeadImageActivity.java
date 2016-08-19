package com.yxh.ryt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.custemview.ScaleScreenImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/9.
 */
public class HeadImageActivity extends BaseActivity {
    private Bitmap bitmap;
    @Bind(R.id.ahi_ssi_head)
    ScaleScreenImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headimage);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null)
        {
            String url = intent.getStringExtra("url");
            AppApplication.displayImage(url,imageView);

        }
    }
    @OnClick(R.id.ahi_ll_all)
    void finishActivity(){
        finish();
    }
}
