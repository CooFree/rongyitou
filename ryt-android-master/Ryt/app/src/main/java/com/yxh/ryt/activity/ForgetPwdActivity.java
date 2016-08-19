package com.yxh.ryt.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.obsever.Smsobserver;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.validations.VerifyCodeValidation;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ForgetPwdActivity extends BaseActivity {
    @Bind(R.id.fp_et_phone)
    EditText eTPhone;
    @Bind(R.id.fp_et_password)
    EditText eTPassword;
    @Bind(R.id.fp_et_verifyCode)
    EditText eTVerfyCode;
    @Bind(R.id.fp_bt_verifyCode)
    TextView sendCode;
    @Bind(R.id.fp_bt_commit)
    Button commit;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private Smsobserver smsObserver;
    private boolean isPhone;
    private boolean isVcode;
    private boolean isPassword;
    private Map<String,String> paramsMap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int time = (Integer) msg.obj;
                    if (time > 0) {
                        sendCode.setText("重新获取验证码（" + time + ")");
                    } else {
                        sendCode.setText("重新获取验证码");
                        sendCode.setEnabled(true);
                    }
                    break;
                case 1:
                    String code = (String) msg.obj;
                    Log.d("++++++++++++++++", code);
                    eTVerfyCode.setText(code);
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpasswrod);
        ButterKnife.bind(this);/*启用注解绑定*/
        smsBackfill();
        commit.setEnabled(false);
        clickable();
        event();
        eTPhone.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        eTPassword.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        eTVerfyCode.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }

    private void clickable() {
        eTPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isPhone = true;
                    dianji(isPhone, isVcode, isPassword);
                } else {
                    isPhone = false;
                    dianji(isPhone, isVcode, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eTVerfyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isVcode = true;
                    dianji(isPhone, isVcode, isPassword);
                } else {
                    isVcode = false;
                    dianji(isPhone, isVcode, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eTPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isPassword = true;
                    dianji(isPhone, isVcode, isPassword);
                } else {
                    isPassword = false;
                    dianji(isPhone, isVcode, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void dianji(boolean isPhone, boolean isVcode, boolean isPassword){
        if (isPassword && isPhone && isVcode){
            commit.setEnabled(true);
            commit.setBackgroundResource(R.mipmap.anniu_kedianji);
        }else {
            commit.setEnabled(false);
            commit.setBackgroundResource(R.mipmap.anniu_bukedianji);
        }
    }
    private void event() {
        eTVerfyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AppApplication.getSingleEditTextValidator()
                            .add(new ValidationModel(eTPhone, new UserNameValidation()))
                            .execute();
                    //表单没有检验通过直接退出方法
                    if (!AppApplication.getSingleEditTextValidator().validate()) {
                        return;
                    }
                    verifyCode();
                }
            }
        });
    }

    private void verifyCode() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", eTPhone.getText().toString());
        paramsMap.put("code", eTVerfyCode.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "verifyCode.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
                ToastUtil.showLong(ForgetPwdActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")) {
                    ToastUtil.showShort(AppApplication.getSingleContext(), "验证码验证成功!");
                } else {
                    ToastUtil.showShort(AppApplication.getSingleContext(), "验证码验证失败!");
                    eTVerfyCode.setText("");
                }
            }
        });
    }

    private void smsBackfill() {
        smsObserver = new Smsobserver(handler, ForgetPwdActivity.this, new Smsobserver.SmsCallBack() {
            @Override
            public void response(String code) {
                Message message = Message.obtain();
                message.obj = code;
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);
    }
    @OnClick({R.id.fp_ib_back,R.id.fp_bt_commit})
    public void commit(View view){
        switch (view.getId()){
            case R.id.fp_ib_back:
                finish();
                break;
            case R.id.fp_bt_commit:
                AppApplication.getSingleEditTextValidator()
                        .add(new ValidationModel(eTPhone, new UserNameValidation()))
                        .add(new ValidationModel(eTPassword,new PasswordValidation()))
                        .add(new ValidationModel(eTVerfyCode,new VerifyCodeValidation()))
                        .execute();
                //表单没有检验通过直接退出方法
                if(!AppApplication.getSingleEditTextValidator().validate()){
                    return;
                }
                paramsMap=new HashMap<>();
                paramsMap.put("username", eTPhone.getText().toString());
                paramsMap.put("password", eTPassword.getText().toString());
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NetRequestUtil.post(Constants.BASE_PATH + "retrievePassword.do", paramsMap, new RegisterCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("失败了");
                        ToastUtil.showLong(ForgetPwdActivity.this,"网络连接超时,稍后重试!");
                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        if (response.get("resultCode").equals("0")) {
                            ToastUtil.showShort(AppApplication.getSingleContext(), "修改密码成功!");
                            Intent intent=new Intent(ForgetPwdActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.showShort(AppApplication.getSingleContext(), "修改密码失败!");
                        }
                    }
                });
                break;
        }

    }

    @OnClick(R.id.fp_bt_verifyCode)
    public void sendCode(){
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(eTPhone, new UserNameValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        paramsMap=new HashMap<>();
        paramsMap.put("username", eTPhone.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "checkUserName.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
                ToastUtil.showLong(ForgetPwdActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")) {
                    ToastUtil.showShort(AppApplication.getSingleContext(), "该手机号未注册!");
                    Intent intent=new Intent(ForgetPwdActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    ForgetPwdActivity.this.finish();
                } else {
                    NetRequestUtil.post(Constants.BASE_PATH + "sendCode.do", paramsMap, new RegisterCallBack() {
                        @Override
                        public void onError(Call call, Exception e) {
                            System.out.println("失败了");
                            ToastUtil.showLong(ForgetPwdActivity.this,"网络连接超时,稍后重试!");
                        }

                        @Override
                        public void onResponse(Map<String, Object> response) {
                            if ("0".equals(response.get("resultCode"))){
                                ToastUtil.showShort(AppApplication.getSingleContext(), "验证码发送成功!");
                                startThrad();
                                sendCode.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }
    private void startThrad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 59; i >= 0; i--) {
                    try {
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.obj = i;
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
