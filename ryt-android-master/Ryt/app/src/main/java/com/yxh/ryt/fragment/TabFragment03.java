package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.CommentActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.NotificationActivity;
import com.yxh.ryt.activity.PrivateLetterActivity;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.custemview.BadgeView;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016-4-4.
 */
@SuppressLint("ValidFragment")
public class TabFragment03 extends  BaseFragment {

    @Bind(R.id.ft3_ci_notification)
    CircleImageView circleNotification;
    @Bind(R.id.ft3_ci_comment)
    CircleImageView circleComment;
    @Bind(R.id.ft3_ci_privateLetter)
    CircleImageView circlePrivateLetter;
    private BadgeView bvNotification,bvComment,bvPrivateLetter;
    int count=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_03, container,false);
        ButterKnife.bind(this, view);
        bvPrivateLetter = new BadgeView(getActivity(),circlePrivateLetter);
        bvNotification = new BadgeView(getActivity(),circleNotification);
        bvComment = new BadgeView(getActivity(),circleComment);
        loadData();
        return view;
    }

    public TabFragment03() {
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void lazyLoad() {

    }
    /*public void updateWatchedStatus(String group, final Class cls){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId", "ieatht97wfw30hfd");
        paramsMap.put("group", group);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "updateWatchedStatus.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")){
                    Intent intent=new Intent(getActivity(), cls);
                    getActivity().startActivity(intent);
                }
            }
        });
    }*/
    private void loadData() {
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "informationList.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
               // ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    String noticeNum = AppApplication.getSingleGson().toJson(response.get("noticeNum"));
                    String commentNum = AppApplication.getSingleGson().toJson(response.get("commentNum"));
                    String messageNum = AppApplication.getSingleGson().toJson(response.get("messageNum"));
                    if ( !"0".equals(noticeNum)){
                        bvNotification.setText(noticeNum);
                        bvNotification.setTextColor(Color.WHITE);
                        bvNotification.setTextSize(7);
                        bvNotification.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认
                        bvNotification.show();
                    }else {
                        bvNotification.setVisibility(View.GONE);
                    }
                    if ( !"0".equals(commentNum)){
                        bvComment = new BadgeView(getActivity(),circleComment);
                        bvComment.setText(commentNum);
                        bvComment.setTextColor(Color.WHITE);
                        bvComment.setTextSize(7);
                        bvComment.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认
                        bvComment.show();
                    }else {
                        bvComment.setVisibility(View.GONE);
                    }
                    if (!"0".equals(messageNum)){
                        bvPrivateLetter.setText(messageNum);
                        bvPrivateLetter.setTextColor(Color.WHITE);
                        bvPrivateLetter.setTextSize(7);
                        bvPrivateLetter.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认
                        bvPrivateLetter.show();
                    }else {
                        bvPrivateLetter.setVisibility(View.GONE);
                    }
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                loadData();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    @OnClick({ R.id.ft3_ll_notification, R.id.ft3_ll_comment, R.id.ft3_ll_privateLetter })
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            //通知
            case R.id.ft3_ll_notification:
                if ("".equals(AppApplication.gUser.getId())){
                    intent=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else {
                    intent=new Intent(getActivity(), NotificationActivity.class);
                    getActivity().startActivity(intent);
                    bvNotification.setVisibility(View.GONE);
                }
                break;
            //评论
            case R.id.ft3_ll_comment:
                if ("".equals(AppApplication.gUser.getId())){
                    intent=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), CommentActivity.class);
                    getActivity().startActivity(intent);
                    bvComment.setVisibility(View.GONE);
                }
                break;
            //私信
            case R.id.ft3_ll_privateLetter:
                if ("".equals(AppApplication.gUser.getId())){
                    intent=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), PrivateLetterActivity.class);
                    getActivity().startActivity(intent);
                    bvPrivateLetter.setVisibility(View.GONE);
                }
                break;
        }
    }
}
