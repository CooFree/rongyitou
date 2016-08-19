package com.yxh.ryt.activity;

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
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class NewAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText reciever;
    private EditText phone;
    private EditText local;
    private EditText detail;
    private ImageButton selected;
    private TextView save;
    private int unDefult;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        reciever = (EditText) findViewById(R.id.et_reciever);
        phone = (EditText) findViewById(R.id.et_phone);
        local = (EditText) findViewById(R.id.et_local);
        detail = (EditText) findViewById(R.id.et_detail);
        selected = (ImageButton) findViewById(R.id.ib_selected);
        save = (TextView) findViewById(R.id.tv_save);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        selected.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        unDefult = 1;
        reciever.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        phone.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        local.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        detail.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.ib_selected:
                if (unDefult == 1) {
                    selected.setBackground(getResources().getDrawable(R.mipmap.yixuanze));
                    unDefult = 2;
                } else {
                    selected.setBackground(getResources().getDrawable(R.mipmap.weixuanze));
                    unDefult = 1;
                }
                break;
            case R.id.tv_save:
                saveNewAddress();
                break;
            default:
                break;

        }
    }

    private void saveNewAddress() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("addressId", "");
        paramsMap.put("status", unDefult + "");
        paramsMap.put("consignee", reciever.getText().toString());
        paramsMap.put("details", detail.getText().toString());
        paramsMap.put("phone", phone.getText().toString());
        paramsMap.put("provinceStr", local.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "saveAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showShort(getApplicationContext(), "保存失败");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showShort(getApplicationContext(), "保存成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                saveNewAddress();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
}
