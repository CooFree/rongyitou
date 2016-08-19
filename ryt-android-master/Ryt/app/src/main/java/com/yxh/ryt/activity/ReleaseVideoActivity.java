package com.yxh.ryt.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.RelesaseVideoCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.NickNameValidation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ReleaseVideoActivity extends  BaseActivity {

    private String file;//视频路径
    @Bind(R.id.rv_vv_video)
    VideoView videoView;//视频播放控件
    @Bind(R.id.rv_et_content)
    EditText content;
    private String artWorkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.releasevideo);
        ButterKnife.bind(this);//启用注解绑定
        content.requestFocus();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Bundle bundle = getIntent().getExtras();
        file = bundle.getString("text");//获得拍摄的短视频保存地址
        artWorkId = bundle.getString("artWorkId");
        setValue();
        content.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    private void setValue() {

        videoView.setVideoPath(file);
        videoView.start();
        videoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {// TODO Auto-generated method stub
                        mp.start();
                        mp.setLooping(true);
                    }
                });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(file);
                videoView.start();
            }
        });
        /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(content,InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/


    }


    @OnClick(R.id.rv_tv_push)
    public  void push(){
        pushVedio();
    }

    private void pushVedio() {
        Map<String,File> fileMap=new HashMap<>();
        File file1 = new File(file);
        fileMap.put(file1.getName(), file1);
        String s = content.getText().toString();
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId",artWorkId);
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("type","1");
        paramsMap.put("content",content.getText().toString());
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "releaseArtworkDynamic.do", "video", fileMap, paramsMap, headers, new RelesaseVideoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Log.d("XXXXXXXXXXXXXXXXXXXXX", "失败了啊");
                ToastUtil.showLong(ReleaseVideoActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(ReleaseVideoActivity.this,"视频上传成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                push();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    @OnClick(R.id.rv_ib_cancel)
    public void cancel(){
        finish();
    }
    @OnClick(R.id.rv_vv_video)
    public void ddddddd(View v) {
        Intent intent = new Intent(this, RecordVedioFullActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("text", file);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.rv_rl_video)
    public void click(View v) {
        Intent intent = new Intent(this, RecordVedioFullActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("text", file);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
