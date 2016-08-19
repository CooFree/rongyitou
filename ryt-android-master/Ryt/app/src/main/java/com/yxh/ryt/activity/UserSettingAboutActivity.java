package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.yxh.ryt.R;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserSettingAboutActivity extends BaseActivity{

    private ImageButton imageButton;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserSettingAboutActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_about);
        imageButton = (ImageButton) findViewById(R.id.usa_ib_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /*@OnClick(R.id.usa_ib_back)
    public void back() {
        finish();
    }*/
}
