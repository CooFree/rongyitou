package com.yxh.ryt.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by Administrator on 2016/6/14.
 */
public class CollectMoneyActivity extends BaseActivity {
    private String remainMoney;
    @Bind(R.id.cm_tv_money)
    TextView money;
    @Bind(R.id.cm_tv_account)
    EditText account;
    @Bind(R.id.cm_tv_name)
    EditText name;
    @Bind(R.id.cm_tv_amount)
    EditText amount;
    @Bind(R.id.cm_tv_commit)
    TextView commit;
    @Bind(R.id.iv_back)
    ImageView back;
    private boolean isAccount;
    private boolean isAmount;
    private boolean isName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collectmoney);
        ButterKnife.bind(this);/*启用注解绑定*/
        remainMoney = getIntent().getStringExtra("remainMoney");
        money.setText("¥ "+remainMoney);
        commit.setEnabled(false);
        clickable();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        account.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        name.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        amount.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    private void clickable() {
        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isAccount = true;
                    dianji(isAccount, isName, isAmount);
                } else {
                    isAccount = false;
                    dianji(isAccount, isName, isAmount);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isName = true;
                    dianji(isAccount, isName, isAmount);
                } else {
                    isName = false;
                    dianji(isAccount, isName, isAmount);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isAmount = true;
                    dianji(isAccount, isName, isAmount);
                } else {
                    isAmount = false;
                    dianji(isAccount, isName, isAmount);
                }
                if (s.length()>0){
                    if (Integer.valueOf(s.toString())>Integer.valueOf(remainMoney)){
                        ToastUtil.showLong(CollectMoneyActivity.this,"提现金额不能大于"+remainMoney+"元");
                        amount.setText("");
                    }
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
    @OnClick(R.id.cm_tv_commit)
    public void commit(){
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("money", amount.getText().toString());
        paramsMap.put("name", name.getText().toString());
        paramsMap.put("number", account.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "getMoney.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(CollectMoneyActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(CollectMoneyActivity.this,"提现成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                commit();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
}
