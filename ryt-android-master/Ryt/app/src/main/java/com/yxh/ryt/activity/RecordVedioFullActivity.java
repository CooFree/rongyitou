package com.yxh.ryt.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/15.
 */
public class RecordVedioFullActivity extends BaseActivity {
    private String file;
    @Bind(R.id.arvf_vv_full)
    VideoView videoView;//视频播放控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvedio_full);
         ButterKnife.bind(this);//*启用注解绑定
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        file = bundle.getString("text");//获得拍摄的短视频保存地址
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
    }
    @OnClick(R.id.arvf_rl_full)
    public void onclick(){
        finish();
    }
}
