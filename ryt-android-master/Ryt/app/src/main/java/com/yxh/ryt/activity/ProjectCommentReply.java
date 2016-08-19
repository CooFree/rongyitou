package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.ContentValidation;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ProjectCommentReply extends BaseActivity {
    @Bind(R.id.pcr_tv_title)
    TextView title;
    @Bind(R.id.pcr_et_content)
    EditText content;
    private String fatherCommentId;
    private String artworkId;
    private String messageId;
    private String currentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_comment_reply);
        ButterKnife.bind(this);/*启用注解绑定*/
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Intent intent=getIntent();
        int flag=intent.getIntExtra("flag", -1);
        if (flag==0){
            String name=intent.getStringExtra("name");
            title.setText("回复"+name+"评论");
        }else if (flag==1){
            title.setText("评论");
        }else if(flag==2){
            String name=intent.getStringExtra("name");
            title.setText(name);
        }
        content.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        fatherCommentId = intent.getStringExtra("fatherCommentId");
        artworkId = intent.getStringExtra("artworkId");
        messageId = intent.getStringExtra("messageId");
        currentUserId = intent.getStringExtra("currentUserId");
    }
    @OnClick(R.id.pcr_ib_back)
    public void back(){
        finish();
    }
    @OnClick(R.id.pcr_ib_push)
    public void push(){
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(content, new ContentValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        sendReply();
    }

    private void sendReply() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId",artworkId+"");
        //paramsMap.put("currentUserId", currentUserId);
        if(!"".equals(messageId)){
            paramsMap.put("messageId", messageId);
        }
        paramsMap.put("content", content.getText().toString());
        if ( !"".equals(fatherCommentId)){
            paramsMap.put("fatherCommentId", fatherCommentId);
        }
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkComment.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(ProjectCommentReply.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")) {
                    ToastUtil.show(ProjectCommentReply.this, "评论成功", Toast.LENGTH_SHORT);
                    ProjectCommentReply.this.finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                sendReply();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
}
