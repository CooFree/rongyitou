package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.NotifaicationCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserYiJianActivity extends BaseActivity{

    private EditText suggestion;
    private TextView tv_send;
    private EditText et_email;
    private ImageView imageButton;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserYiJianActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_yijian);
        suggestion = (EditText) findViewById(R.id.usyij_et_content);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_send = (TextView) findViewById(R.id.usyj_tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSuggestion(suggestion);
            }
        });
        imageButton = (ImageView) findViewById(R.id.usyj_ib_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        suggestion.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        et_email.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    /*public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }*/
    private void sendSuggestion(final TextView tv) {
        if (suggestion.equals("")) {
            ToastUtil.show(getApplicationContext(),"请填写意见",0);
        } else {
            Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId","ieatht97wfw30hfd");
        paramsMap.put("content",tv.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
            paramsMap.put("email",et_email.getText().toString());
        NetRequestUtil.post(Constants.BASE_PATH + "feedBack.do", paramsMap, new NotifaicationCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(UserYiJianActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.show(getApplicationContext(),"感谢您的反馈",1);
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                sendSuggestion(tv);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    }

}
