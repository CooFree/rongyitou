package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RZTitlePageIndicatorAdapter;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.RZDetailFragment;
import com.yxh.ryt.fragment.RZInvestFragment;
import com.yxh.ryt.fragment.RZProjectFragment;
import com.yxh.ryt.fragment.WorksFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class FinanceSummaryActivity extends BaseActivity implements View.OnClickListener {
    List<BaseFragment> rZFragments=new ArrayList<>();
    FragmentPagerAdapter rZAdapter;
    private ImageView back;
    private TextView title;
    private ImageView share;
    IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID); //初始化api
        api.registerApp(Constants.APP_ID); //将APP_ID注册到微信中
        setContentView(R.layout.activity_financesummary);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        title = ((TextView) findViewById(R.id.afs_tv_title));
        share = (ImageView) findViewById(R.id.ib_top_rt);
        LinearLayout invest = (LinearLayout) findViewById(R.id.rzp_ll_invest);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        Intent intent = this.getIntent();
        final String artWorkId = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String userId=intent.getStringExtra("userId");
        final RZProjectFragment rzProjectFragment=new RZProjectFragment(artWorkId);
        rZFragments.add(rzProjectFragment);
        rZFragments.add(new RZDetailFragment(artWorkId));
        rZFragments.add(new RZInvestFragment(artWorkId));
        rZFragments.add(new WorksFragment(userId));
        title.setText(name);
        rZAdapter = new RZTitlePageIndicatorAdapter(this.getSupportFragmentManager(),rZFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(rZAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator mindicator = (TabPageIndicator) findViewById(R.id.indicator);
        mindicator.setViewPager(pager);
        invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(FinanceSummaryActivity.this, LoginActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent1 = new Intent(FinanceSummaryActivity.this, InvestActivity.class);
                    intent1.putExtra("allMoney", rzProjectFragment.getRemainMoney());
                    intent1.putExtra("artWorkId", artWorkId);
                    startActivity(intent1);
                }
            }
        });

    }

    public FinanceSummaryActivity() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            //分享
            case R.id.ib_top_rt:
                showShareDialog();
                break;
            default:
                break;
        }
    }
    private void showShareDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_share_weixin_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(this, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.view_share_weixin:
                        // 分享到微信
                        shareWx(0);
                        break;

                    case R.id.view_share_pengyou:
                        // 分享到朋友圈
                        shareWx(1);
                        break;

                    case R.id.share_cancel_btn:
                        // 取消
                        break;

                }

                dialog.dismiss();
            }

        };
        ViewGroup mViewWeixin = (ViewGroup) view.findViewById(R.id.view_share_weixin);
        ViewGroup mViewPengyou = (ViewGroup) view.findViewById(R.id.view_share_pengyou);
        Button mBtnCancel = (Button) view.findViewById(R.id.share_cancel_btn);
        //mBtnCancel.setTextColor(R.none_color);
        mViewWeixin.setOnClickListener(listener);
        mViewPengyou.setOnClickListener(listener);
        mBtnCancel.setOnClickListener(listener);

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }
    private void shareWx(final int flag) {
        /*WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "title";
        msg.description = getResources().getString(
                R.string.app_name);
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo_qq);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req reqShare = new SendMessageToWX.Req();
        reqShare.transaction = String.valueOf(System.currentTimeMillis());
        reqShare.message = msg;
        reqShare.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(reqShare);*/
       /* Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "toShareView.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Log.d("xxxxxxxxxxxxxxxx","1");
                String share_Url = (String) response.get("url");*/
                /*WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = share_Url;
                WXMediaMessage msg = new WXMediaMessage(webpage);

                msg.title = "title";
                msg.description = getResources().getString(
                        R.string.app_name);
                Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ryt_logo);
                msg.setThumbImage(thumb);
                SendMessageToWX.Req reqShare = new SendMessageToWX.Req();
                reqShare.transaction = String.valueOf(System.currentTimeMillis());
                reqShare.message = msg;
                reqShare.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

                api.sendReq(reqShare);*/


        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://ryt.efeiyi.com/app/shareView.do";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "融艺投App";
        msg.description = "面向艺术家与大众进行艺术交流与投资的应用";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo108);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req reqShare = new SendMessageToWX.Req();
        reqShare.transaction = String.valueOf(System.currentTimeMillis());
        reqShare.message = msg;
        reqShare.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(reqShare);
        /*    }
        });*/

    }

}
