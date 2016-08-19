package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.ChuangZuoXiangQingTab01Fragment;
import com.yxh.ryt.fragment.ChuangZuoXiangQingTab02Fragment;
import com.yxh.ryt.fragment.ChuangZuoXiangQingTab03Fragment;
import com.yxh.ryt.fragment.ChuangZuoXiangQingTab04Fragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class ChuangZuoXQActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.cl_01_tv_prc)
    ImageView cl01TvPrc;
    @Bind(R.id.cl_01_tv_title)
    TextView cl01TvTitle;
    @Bind(R.id.tv_financing)
    TextView tvFinancing;
    @Bind(R.id.cl_01_civ_headPortrait)
    CircleImageView cl01CivHeadPortrait;
    @Bind(R.id.cl_01_tv_name)
    TextView cl01TvName;
    @Bind(R.id.cl_01_tv_zhicheng)
    TextView cl01TvZhicheng;
    @Bind(R.id.cl_01_ll_zhicheng)
    LinearLayout cl01LlZhicheng;
    @Bind(R.id.cl_01_tv_brief)
    TextView cl01TvBrief;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ChuangZuoXQActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    private String artworkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuangzuo_xiangqing);
        linearLayout1 = (LinearLayout) findViewById(R.id.rl_tab_01);
        linearLayout2 = (LinearLayout) findViewById(R.id.rl_tab_02);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent != null) artworkId = intent.getStringExtra("id");
        mViewPager = (ViewPager) findViewById(R.id.v_scroll);
        StickHeaderLayout shl_root = (StickHeaderLayout) findViewById(R.id.shl_root);
        manager = new StickHeaderViewPagerManager(shl_root, mViewPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(ChuangZuoXiangQingTab01Fragment.newInstance(manager, 0, false));
        mFragmentList.add(ChuangZuoXiangQingTab02Fragment.newInstance(manager, 1, false));
        mFragmentList.add(ChuangZuoXiangQingTab03Fragment.newInstance(manager, 2, false));
        mFragmentList.add(ChuangZuoXiangQingTab04Fragment.newInstance(manager, 3, false));
        RongZiXqTabPageIndicatorAdapter pagerAdapter = new RongZiXqTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        LoadData(0, 1);
        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tab_01:
                startActivity(new Intent(this, CommentActivity.class));
                break;
            case R.id.rl_tab_02:
                /*Intent intent=new Intent(this,InvestActivity.class);
                intent.getIntExtra("",)*/
                break;
            default:
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        LoadData(0, 1);
    }

    private void LoadData(int tabtype, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artworkId+"");
        //paramsMap.put("currentUserId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(paramsMap.toString() + "====");
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(ChuangZuoXQActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")), Artwork.class);
                    cl01TvTitle.setText(artwork.getTitle());
                    cl01TvBrief.setText(artwork.getBrief());
                    cl01TvName.setText(artwork.getAuthor().getName());
                    cl01LlZhicheng.setVisibility(View.GONE);
                    if (artwork.getAuthor() != null) {
                        if (artwork.getAuthor().getMaster() != null) {
                            if (artwork.getAuthor().getMaster().getTitle() != null && !"".equals(artwork.getAuthor().getMaster().getTitle())) {
                                cl01LlZhicheng.setVisibility(View.VISIBLE);
                                cl01TvZhicheng.setText(artwork.getAuthor().getMaster().getTitle());
                            }
                        }
                    }
                    AppApplication.displayImage(artwork.getPicture_url(), cl01TvPrc);
                    EventBus.getDefault().post(object);
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                LoadData(0, 1);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }

            }
        });
    }
    @Subscribe
    public void onEventMainThread(Artwork artwork){
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.ib_top_lf)
    public void back(){
        finish();
    }
}

