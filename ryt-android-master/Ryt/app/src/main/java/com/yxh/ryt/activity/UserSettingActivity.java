package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.util.DataCleanManager;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class UserSettingActivity extends BaseActivity {
    @Bind(R.id.rl_about)
    RelativeLayout rlAbout;
    @Bind(R.id.rl_hc)
    RelativeLayout rlHc;
    @Bind(R.id.btn_out)
    Button btnOut;
    @Bind(R.id.go3)
    TextView huanCun;
    /*private SettingReceiver receiver;*/
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    huanCun.setText("0");
                    ToastUtil.showLong(UserSettingActivity.this, "清除缓存成功");
            }
        }
    };

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserSettingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册这两个广播
       /* receiver = new SettingReceiver();
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("android.intent.action.LOGIN_SUC_BROADCAST");
        AppApplication.getSingleContext().registerReceiver(receiver, myFilter);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String allCache = DataCleanManager.getCacheSize(getExternalCacheDir());
            huanCun.setText(allCache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_about, R.id.btn_out, R.id.rl_hc})
    void itemClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about:
                UserSettingAboutActivity.openActivity(this);
                break;
            case R.id.btn_out:
                AppApplication.gUser.setId("");
                AppApplication.gUser.setLoginState("");
                AppApplication.gUser.setUsername("");
                AppApplication.gUser.setPassword("");
                AppApplication.gUser.setName("");
                AppApplication.gUser.setSex("");
                AppApplication.gUser.setMaster1("");
                AppApplication.gUser.setPictureUrl("");
                SPUtil.clear(AppApplication.getSingleContext());
                SPUtil.put(getApplicationContext(), Constants.ISFIRSTENTER, false);
                btnOut.setVisibility(View.GONE);
                OkHttpUtils.getInstance().getCookieStore().getCookies().clear();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_hc:
                if ("0B".equals(huanCun.getText().toString())) {
                    ToastUtil.showLong(UserSettingActivity.this, "不需要清除缓存");
                    break;
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataCleanManager.cleanInternalCache(getApplicationContext());
                        DataCleanManager.cleanExternalCache(getApplicationContext());
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                });
                thread.start();
                break;
        }
    }

   /* public class SettingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.LOGIN_SUC_BROADCAST")) {
                if (btnOut!=null){
                    btnOut.setVisibility(View.VISIBLE);
                }
            }
        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        /*try {
            AppApplication.getSingleContext().unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @OnClick(R.id.us_ib_back)
    public void back() {
        finish();
    }
}
