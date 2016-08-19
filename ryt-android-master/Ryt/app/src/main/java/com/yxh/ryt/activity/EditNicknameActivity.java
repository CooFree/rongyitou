package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class EditNicknameActivity extends Activity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_save;
    private EditText nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        nickName = (EditText) findViewById(R.id.et_nickName);
        nickName.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                saveEditNickname();
                break;
            default:
                break;
        }
    }

    private void saveEditNickname() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("type", "11");
        paramsMap.put("content", nickName.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "editProfile.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showLong(EditNicknameActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.EDIT_NICK_BROADCAST");
                    intent.putExtra("nick", nickName.getText().toString());
                    EditNicknameActivity.this.sendBroadcast(intent);
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                saveEditNickname();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    private void getUser(Map<String, Object> response) {
        User user = new User();
        user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
        SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
        SPUtil.put(AppApplication.getSingleContext(), "current_username", user.getUsername()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_name", user.getName()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_sex", user.getSex() + "");
        if (user.getMaster()!=null){
            SPUtil.put(AppApplication.getSingleContext(), "current_master","master");
        }else {
            SPUtil.put(AppApplication.getSingleContext(), "current_master","");
        }
        SPUtil.put(AppApplication.getSingleContext(), "current_pictureUrl", user.getPictureUrl()+"");
        AppApplication.gUser = user;
        if (user.getMaster()!=null){
            AppApplication.gUser.setMaster1("master");
        }else {
            AppApplication.gUser.setMaster1("");
        }
        System.out.print(AppApplication.gUser.toString());
    }
}
