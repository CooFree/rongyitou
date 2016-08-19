package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 *
 */
public class UserChongZhiActivity extends BaseActivity{
    private String remainMoney;
    @Bind(R.id.ucz_tv_remainMoney)
    TextView tv_remainMoney;
    @Bind(R.id.ucz_et_money)
    EditText edMoney;
    @Bind(R.id.ucz_ll_money)
    LinearLayout llMoney;
    @Bind(R.id.ucz_tv_money)
    TextView tvMoney;
    @Bind(R.id.ucz_ll_all)
    LinearLayout all;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserChongZhiActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chongzhi);
        ButterKnife.bind(this);
        remainMoney = getIntent().getStringExtra("remainMoney");
        tv_remainMoney.setText("￥"+remainMoney);
        edMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    edMoney.setTextColor(Color.rgb(0,0,0));
                    edMoney.setHintTextColor(Color.rgb(153,153,153));
                    tvMoney.setTextColor(Color.rgb(153,153,153));
                    llMoney.setBackgroundResource(R.drawable.edit_focus);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }else {
                    edMoney.setHintTextColor(Color.rgb(238,238,238));
                    tvMoney.setTextColor(Color.rgb(238,238,238));
                    llMoney.setBackgroundResource(R.drawable.edit_nofocus);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        all.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                all.setFocusable(true);
                all.setFocusableInTouchMode(true);
                all.requestFocus();
                return false;
            }
        });
        edMoney.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    @OnClick({R.id.ucz_ll_recharge,R.id.ib_top_lf})
    public void back(View view) {
        switch (view.getId()){
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.ucz_ll_recharge:
                investMoney();
                break;
        }
    }
    private void investMoney() {
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("money",edMoney.getText().toString());
        paramsMap.put("action", "account");
        paramsMap.put("type", "1");
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
                ToastUtil.showLong(UserChongZhiActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                investMoney();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }else {
                    String url = response.get("url").toString();
                    Intent intent=new Intent(UserChongZhiActivity.this,PayPageActivity.class);
                    intent.putExtra("url",url);
                    UserChongZhiActivity.this.startActivity(intent);
                    finish();
                }
            }
        });
    }
}
