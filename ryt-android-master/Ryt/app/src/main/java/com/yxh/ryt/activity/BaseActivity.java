package com.yxh.ryt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.yxh.ryt.R;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends FragmentActivity {

    protected InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(this, R.layout.notification_layout, R.id.noti_image, R.id.noti_title, R.id.noti_text);
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void closeSoftInputMethod() {
        try {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
