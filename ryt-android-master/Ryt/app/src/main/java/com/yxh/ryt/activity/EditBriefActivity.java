package com.yxh.ryt.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.fragment.UserJianJieFragment;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class EditBriefActivity extends BaseActivity implements View.OnClickListener {

    private TextView back;
    private TextView save;
    private EditText et_brief;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief);
        String content = getIntent().getStringExtra("content");
        back = (TextView) findViewById(R.id.tv_back);
        save = (TextView) findViewById(R.id.tv_save);
        et_brief = (EditText) findViewById(R.id.et_brief);
        et_brief.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        if (content!=null){
            et_brief.setText(content);
        }
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }
/*
    public void setMainListener(OnMainListener mainListener) {
        this.mainListener = mainListener;
    }

    private OnMainListener mainListener;

// 绑定接口

    @Override

    public void onAttachFragment(Fragment fragment) {

        try {

            mainListener = (OnMainListener)mUserJianJieFragment;

        } catch (Exception e) {

            throw new ClassCastException(this.toString() + " must implementOnMainListener");

        }

        super.onAttachFragment(fragment);

    }
    // 接口

    public interface OnMainListener {
         void onMainAction();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_save:
                saveBrief();
                break;
        }
    }

    private void saveBrief() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", "2");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("content", et_brief.getText().toString());
        NetRequestUtil.post(Constants.BASE_PATH + "saveUserBrief.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.show(getApplicationContext(),"保存失败,请重试!",0);
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.show(getApplicationContext(),"保存成功",0);
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                saveBrief();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
}
