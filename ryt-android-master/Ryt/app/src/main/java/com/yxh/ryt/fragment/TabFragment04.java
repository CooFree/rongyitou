package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.AuctionOrderActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.MyProjectActivity;
import com.yxh.ryt.activity.PublicProject01Activity;
import com.yxh.ryt.activity.UserEditZiLiaoActivity;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.activity.UserQianBaoActivity;
import com.yxh.ryt.activity.UserSettingActivity;
import com.yxh.ryt.activity.UserYiJianActivity;
import com.yxh.ryt.activity.YsjRzActivity;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.User;

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
public class TabFragment04 extends BaseFragment {
    @Bind(R.id.uc_iv_headImage)
    CircleImageView rsIvHeadPortrait;
    @Bind(R.id.uc_tv_name)
    TextView tvUserHeaderName;
    @Bind(R.id.btn_lf)
    TextView btnLf;
    @Bind(R.id.tv_top_ct)
    TextView topName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center, container,false);
        ButterKnife.bind(this, view);
        if (btnLf!=null){
            btnLf.setVisibility(View.GONE);
        }
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public TabFragment04() {
    }

    //头像点击事件
    @OnClick(R.id.rl_user_headImage)
    void userHeaderClick() {
        if ("".equals(AppApplication.gUser.getId())) {
            LoginActivity.openActivity(getActivity());
            return;
        } else {
            UserEditZiLiaoActivity.openActivity(getActivity());
            return;
        }
    }
    //拍卖订单
    @OnClick(R.id.rl_auction)
    public void order(){
        if (!"".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), AuctionOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", AppApplication.gUser.getId() + "");
            getActivity().startActivity(intent);
        }else {
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    //设置点击事件
    @OnClick(R.id.user_setting)
    void userSettingClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            UserSettingActivity.openActivity(getActivity());
        }
    }
    //钱包点击事件
    @OnClick(R.id.rl_02)
    void userQbClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            UserQianBaoActivity.openActivity(getActivity());
        }
    }
    @OnClick(R.id.rl_user_project)
    void userProjectClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            Intent intent=new Intent(getActivity(),MyProjectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }
    //意见点击事件
    @OnClick(R.id.rl_yijian)
    void userYiJianClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            UserYiJianActivity.openActivity(getActivity());
        }
    }
    //我的主页点击事件
    @OnClick(R.id.rl_user_index)
    public void userIndexClick() {
        if ("".equals(AppApplication.gUser.getId())) {
            LoginActivity.openActivity(getActivity());
            return;
        }
        if (AppApplication.gUser != null&&"master".equals(AppApplication.gUser.getMaster1())) {
            Intent intent=new Intent(AppApplication.getSingleContext(),ArtistIndexActivity.class);
            intent.putExtra("userId", AppApplication.gUser.getId());
            intent.putExtra("name",tvUserHeaderName.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else if (AppApplication.gUser != null&&"".equals(AppApplication.gUser.getMaster1())){
            Intent intent=new Intent(AppApplication.getSingleContext(),UserIndexActivity.class);
            intent.putExtra("userId", AppApplication.gUser.getId());
            intent.putExtra("name",tvUserHeaderName.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.gUser!=null && AppApplication.gUser.getId()!=null && !"".equals(AppApplication.gUser.getId())){
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("userId", AppApplication.gUser.getId());
            //paramsMap.put("currentId", AppApplication.gUser.getId());
            paramsMap.put("pageIndex", "1");
            paramsMap.put("pageSize", "20");
            paramsMap.put("timestamp", System.currentTimeMillis() + "");
            try {
                paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
            NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RegisterCallBack() {
                @Override
                public void onError(Call call, Exception e) {
                    System.out.println("失败了");
                    ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
                }

                @Override
                public void onResponse(Map<String, Object> response) {
                    /*if (!response.get("resultCode").equals("0")) {
                        ToastUtil.showShort(AppApplication.getSingleContext(), "失败!");
                        return;
                    }*/
                    if (response.get("resultCode").equals("0")) {
                        Map<String, Object> pageInfo = (Map<String, Object>) response.get("pageInfo");
                        User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(pageInfo.get("user")), User.class);
                       /* Double sa = (Double) pageInfo.get("followNum");
                        int a =(int)sa;
                        tvUserHeaderGzNum.setText(a+"");*/
                        if (user != null) {
                            if ("2".equals(user.getType()) &&null!=btnLf) {
                                btnLf.setVisibility(View.VISIBLE);
                                btnLf.setText("申请为艺术家");
                                setLoginedViewValues(1, user);
                                btnLf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        YsjRzActivity.openActivity(getActivity());
                                    }
                                });
                                AppApplication.gUser.setMaster1("");
                            } else if ("1".equals(user.getType()) &&null!=btnLf) {
                                btnLf.setVisibility(View.VISIBLE);
                                btnLf.setText("发起项目");
                                setLoginedViewValues(2, user);
                                btnLf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PublicProject01Activity.openActivity(getActivity());
                                    }
                                });
                                AppApplication.gUser.setMaster1("master");
                            }else if ("0".equals(user.getType()) &&null!=btnLf) {
                                btnLf.setVisibility(View.VISIBLE);
                                btnLf.setTextColor(Color.rgb(128,128,128));
                                btnLf.setText("申请为艺术家中");
                                btnLf.setEnabled(false);
                                setLoginedViewValues(2, user);
                                AppApplication.gUser.setMaster1("");
                            }
                        }
                    }
                }
            });
        }else {
            btnLf.setVisibility(View.GONE);
            rsIvHeadPortrait.setImageResource(R.mipmap.jibenziliao_touxiang);
            //AppApplication.displayImage(AppApplication.gUser.getPictureUrl(),rsIvHeadPortrait);
            tvUserHeaderName.setText("游客");
            topName.setText("游客");
        }

    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues(int type,User user) {
        AppApplication.displayImage(user.getPictureUrl(),rsIvHeadPortrait);
        tvUserHeaderName.setText(user.getName()+"");
        topName.setText(user.getName()+"");
    }


    @Override
    protected void lazyLoad() {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
