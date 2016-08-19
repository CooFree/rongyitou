package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.receiver.WxLoginBroadcastReciver;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.EditTextValidator;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.vo.User;
import com.yxh.ryt.vo.WxUser;
import com.yxh.ryt.wxapi.WxUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/30.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.btn_center_login)
    Button btnLogin;
    @Bind(R.id.et_center_one)
    EditText etUsername;
    @Bind(R.id.et_center_two)
    EditText etPassword;
    @Bind(R.id.ib_top_rt)
    ImageButton ibRight;
    @Bind(R.id.iv_center_wx)
    ImageView ivWxLogin;
    @Bind(R.id.tv_center_forget)
    TextView tvForget;
    private  EditTextValidator editTextValidator;
    WxLoginBroadcastReciver mReciver;
    private boolean isPhone;
    private boolean isPassword;
    private String guide;
    private WxUser wxUser1;
    private String callBackStr;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);/*启用注解绑定*/
        btnLogin.setEnabled(false);
        clickable();
        guide = getIntent().getStringExtra("guide");
        callBackStr = getIntent().getStringExtra("callBackStr");
        etUsername.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        etPassword.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }

    private void clickable() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    isPhone = true;
                    dianji(isPhone, isPassword);
                } else {
                    isPhone = false;
                    dianji(isPhone, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && s.length() < 17) {
                    isPassword = true;
                    dianji(isPhone, isPassword);
                } else {
                    isPassword = false;
                    dianji(isPhone, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void dianji(boolean isPhone, boolean isPassword){
        if (isPassword && isPhone){
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundResource(R.mipmap.anniu_kedianji);
        }else {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.mipmap.anniu_bukedianji);
        }
    }
    /*返回按钮事件触发*/
    @OnClick(R.id.ib_top_rt)
    public void closeClick(){
        finish();
    }
    /*注册按钮事件触发*/
    @OnClick(R.id.tv_center_reg)
    public void regClick(){
        finish();
        Intent intent=new Intent(this,RegisterActivity.class);
        intent.putExtra("guide",guide);
        startActivity(intent);
    }
    /*忘记密码按钮事件触发*/
    @OnClick(R.id.tv_center_forget)
    public void forgetClick(){
        Intent intent=new Intent(this,ForgetPwdActivity.class);
        startActivity(intent);
    }
    /*按钮事件触发*/
    @OnClick(R.id.iv_center_wx)
    public void wxLoginClick(){
        if(WxUtil.regAndCheckWx(LoginActivity.this)){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.WX_LOGIN_ACTION);
            mReciver = new WxLoginBroadcastReciver(new WxLoginBroadcastReciver.WxLoginCallBack() {
                @Override
                public void response(String wxUser) {
                    wxUser1=AppApplication.getSingleGson().fromJson(wxUser, WxUser.class);
                    Map<String,String> paramsMap=new HashMap<>();
                    paramsMap.put("nickname",wxUser1.getNickname());
                    paramsMap.put("headimgurl",wxUser1.getHeadimgurl());
                    paramsMap.put("unionid",wxUser1.getUnionid());
                    paramsMap.put("timestamp", System.currentTimeMillis() + "");
                    try {
                        AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
                        paramsMap.put("signmsg", AppApplication.signmsg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    NetRequestUtil.post(Constants.BASE_PATH + "j_spring_security_check", paramsMap, new RZCommentCallBack() {
                        @Override
                        public void onError(Call call, Exception e) {
                            e.printStackTrace();
                            System.out.println("444444失败了");
                        }
                        @Override
                        public void onResponse(Map<String, Object> response) {
                            if ("0".equals(response.get("resultCode"))) {
                                User user = new User();
                                user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
                                getUser(user,2);
                                Map<String, String> paramsMap = new HashMap<>();
                                //paramsMap.put("id", user.getId());
                                paramsMap.put("cid", JPushInterface.getRegistrationID(LoginActivity.this));
                                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                                try {
                                    AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
                                    paramsMap.put("signmsg", AppApplication.signmsg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                NetRequestUtil.post(Constants.BASE_PATH + "wxBinding.do", paramsMap, new LoginCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        System.out.println("失败了");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        if ("guide".equals(guide)){
                                            Intent intent=new Intent(LoginActivity.this,IndexActivity.class);
                                            startActivity(intent);
                                            //ToastUtil.showLong(LoginActivity.this,"成功");
                                            finish();
                                        }else {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.H5_LOGINSUCCESS_BROADCAST");
                                            intent.putExtra("callBackStr",callBackStr);
                                            LoginActivity.this.sendBroadcast(intent);
                                            finish();
                                        }
                                        /*Intent intent = new Intent();
                                        intent.setAction("android.intent.action.LOGIN_SUC_BROADCAST");
                                        LoginActivity.this.sendBroadcast(intent);*/
                                    }
                                });

                            }
                        }
                    });
                }


            });
            registerReceiver(mReciver, intentFilter);
            WxUtil.wxlogin();
        }
    }

    /*登录按钮点击事件触发*/
    @OnClick(R.id.btn_center_login)
    public void loginClick(){
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(etUsername,new UserNameValidation()))
                .add(new ValidationModel(etPassword,new PasswordValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        LoginRequst();/*调用登录具体实现*/
    }

    //登录具体实现
    private void LoginRequst() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("username",etUsername.getText().toString());
        paramsMap.put("password", etPassword.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg=EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "j_spring_security_check", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response == null || Integer.valueOf((String) response.get("resultCode")) > 0) {
                    ToastUtil.showShort(LoginActivity.this, "登录失败");
                    return;
                }
                User user = new User();
                user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
                getUser(user,1);
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("username", etUsername.getText().toString());
                paramsMap.put("password", etPassword.getText().toString());
                paramsMap.put("cid", JPushInterface.getRegistrationID(LoginActivity.this));
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
                    paramsMap.put("signmsg", AppApplication.signmsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NetRequestUtil.post(Constants.BASE_PATH + "userBinding.do", paramsMap, new LoginCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("失败了");
                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        syncCookie(Constants.BASE_PATH+"j_spring_security_check", OkHttpUtils.getInstance().getCookieStore().getCookies().get(0)+"");
                        if ("guide".equals(guide)){
                            Intent intent=new Intent(LoginActivity.this,IndexActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.H5_LOGINSUCCESS_BROADCAST");
                            intent.putExtra("callBackStr",callBackStr);
                            LoginActivity.this.sendBroadcast(intent);
                            finish();
                        }
                        /*Intent intent = new Intent();
                        intent.setAction("android.intent.action.LOGIN_SUC_BROADCAST");
                        LoginActivity.this.sendBroadcast(intent);*/
                    }
                });
            }
        });
    }

    private void getUser(User user, int i) {
        if (user.getMaster()!=null){
            user.setMaster1("master");
        }else {
            user.setMaster1("");
        }
        if (i==1){
            SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_username", user.getUsername()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_password", etPassword.getText().toString()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_name", user.getName()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_sex", user.getSex()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_master", user.getMaster1()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_pictureUrl", user.getPictureUrl()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_loginState", "1");
            user.setLoginState("1");
            AppApplication.gUser = user;
            AppApplication.gUser.setPassword(etPassword.getText().toString());
        }else if (i==2){
            SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_unionid", wxUser1.getUnionid() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_master", user.getMaster1()+"");
            SPUtil.put(AppApplication.getSingleContext(), "current_loginState", "2");
            user.setLoginState("2");
            AppApplication.gUser = user;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReciver!=null){
            unregisterReceiver(mReciver);
        }

    }
    /**
     * 将cookie同步到WebView
     * @param url WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public  boolean syncCookie(String url,String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        return TextUtils.isEmpty(newCookie)?false:true;
    }

}
