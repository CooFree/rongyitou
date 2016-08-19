package com.yxh.ryt.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.RongZiXiangQingTab01Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab02Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab03Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab04Fragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.Master;
import com.yxh.ryt.vo.User;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class RongZiXQActivity extends BaseActivity {
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
    @Bind(R.id.iv_tab_01)
    ImageView dianzan;
    @Bind(R.id.rzxq_tv_zan)
    TextView zan;
    @Bind(R.id.tv_top_ct)
    TextView topTitle;
    @Bind(R.id.progressBar1)
    ProgressBar bar;
    @Bind(R.id.tv_pb_value)
    TextView prValue;
    @Bind(R.id.fli_ll_tv_investGoalMoney)
    TextView goalMoney;
    @Bind(R.id.fli_ll_tv_remainingTime)
    TextView remainTime;
    @Bind(R.id.fli_ll_tv_investGoalPeople)
    TextView goalPeople;
    private StickHeaderLayout shl_root;
    private LinearLayout touziren_ll;
    private SQLiteDatabase database;
    private boolean isPraise1;
    IWXAPI api;
    private List<User> users;
    private int widthzong;
    private int width;
    private int height;
    private int right;
    private int count;
    private int time = 0;
    private int remainMoney;
    private String share_Url;
    private boolean flag=true;
    protected static final int PRAISE_SUC = 100;
    private String userId;
    private Master master;
    private HashMap<ImageView, String> userImageViewMap;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, RongZiXQActivity.class));
    }

    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    private String artworkId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PRAISE_SUC:
                    int a = Integer.parseInt(zan.getText().toString());
                    a++;
                    zan.setText(a+"");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rongzi_xiangqing);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID); //初始化api
        api.registerApp(Constants.APP_ID); //将APP_ID注册到微信中
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent != null) artworkId = intent.getStringExtra("id");
        mViewPager = (ViewPager) findViewById(R.id.v_scroll);
        shl_root = (StickHeaderLayout) findViewById(R.id.shl_root);
        touziren_ll = ((LinearLayout) findViewById(R.id.rzxq_ll_touziren));
        manager = new StickHeaderViewPagerManager(shl_root, mViewPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(RongZiXiangQingTab01Fragment.newInstance(manager, 0, false));
        mFragmentList.add(RongZiXiangQingTab02Fragment.newInstance(manager, 1, false));
        mFragmentList.add(RongZiXiangQingTab03Fragment.newInstance(manager, 2, false, artworkId));
        mFragmentList.add(RongZiXiangQingTab04Fragment.newInstance(manager, 3, false, artworkId));
        RongZiXqTabPageIndicatorAdapter pagerAdapter = new RongZiXqTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        widthzong = metric.widthPixels;
        //图片宽高
        width = Utils.dip2px(RongZiXQActivity.this, 24);
        height = Utils.dip2px(RongZiXQActivity.this, 24);
        //图片padding
        right = Utils.dip2px(RongZiXQActivity.this, 10);
        //一行能容纳的图片个数
        count = (widthzong - (Utils.dip2px(RongZiXQActivity.this, 20) * 2)) / (width + right);
        /*database= AppApplication.getDBHelper().getWritableDatabase();
        Cursor cursor = database.query("rzxq_praise", null, "project_workID=? AND current_id=?", new String[]{artworkId,AppApplication.gUser.getId()+""}, null, null, null);*/
        /*while (cursor.moveToNext()) {
            isPraise = cursor.getString(cursor.getColumnIndex("isPraise"));
        }
        if ("1".equals(isPraise)){
            dianzan.setEnabled(false);
            dianzan.setImageResource(R.mipmap.dianzanhou);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData(0, 1);
    }

    @OnClick({R.id.ll_comment, R.id.ib_top_lf, R.id.ib_top_rt, R.id.iv_tab_01, R.id.rzxq_tv_invest})
    public void comment(View view) {
        switch (view.getId()) {
            //评论
            case R.id.ll_comment:
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(RongZiXQActivity.this, LoginActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent = new Intent(this, ProjectCommentReply.class);
                    intent.putExtra("fatherCommentId", "");
                    intent.putExtra("messageId", "");
                    intent.putExtra("name", topTitle.getText().toString());
                    intent.putExtra("flag", 2);
                    intent.putExtra("artworkId", artworkId);
                    intent.putExtra("currentUserId", AppApplication.gUser.getId());
                    startActivity(intent);
                }
                break;
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.ib_top_rt:
                showShareDialog();
                break;

            case R.id.rzxq_tv_invest:
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(RongZiXQActivity.this, LoginActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent1 = new Intent(this, InvestActivity.class);
                    intent1.putExtra("allMoney", remainMoney);
                    intent1.putExtra("artWorkId", artworkId);
                    startActivity(intent1);
                }
                break;
            //点赞
            case R.id.iv_tab_01:
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(RongZiXQActivity.this, LoginActivity.class);
                    startActivity(intent2);
                } else {
                    if (!isPraise1) {
                        AnimationSet animationSet = new AnimationSet(true);
                        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setDuration(200);
                        animationSet.addAnimation(scaleAnimation);
                        animationSet.setFillAfter(true);
                        animationSet.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                AnimationSet animationSet = new AnimationSet(true);
                                ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                scaleAnimation.setDuration(200);
                                animationSet.addAnimation(scaleAnimation);
                                animationSet.setFillAfter(true);
                                dianzan.startAnimation(animationSet);
                                dianzan.setEnabled(false);
                                praise(artworkId, AppApplication.gUser.getId() + "");
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        dianzan.setImageResource(R.mipmap.dianzanhou);
                        dianzan.startAnimation(animationSet);
                    }
                }
                break;
        }

    }

    private void praise(String artworkId, String s) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artworkId + "");
        paramsMap.put("action ", "1");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showLong(RongZiXQActivity.this, "点赞成功");
                    Message msg = Message.obtain();
                    msg.what = PRAISE_SUC;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private void LoadData(int tabtype, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artworkId + "");
        /*if (!"".equals(AppApplication.gUser.getId())) {
            paramsMap.put("currentUserId", AppApplication.gUser.getId());
        }*/ /*else {
            paramsMap.put("currentUserId", AppApplication.gUser.getId());
        }*/
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
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Object> object = (Map<String, Object>) response.get("object");
                if (object != null) {
                    users = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("investPeople")), new TypeToken<List<User>>() {
                    }.getType());
                    if (users != null && users.size() > 0 && flag) {
                        isFirst(users);
                        flag=false;
                    }
                    isPraise1 = Boolean.parseBoolean(AppApplication.getSingleGson().toJson(object.get("isPraise")));
                    Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")), Artwork.class);
                    topTitle.setText(artwork.getTitle());
                    cl01TvTitle.setText(artwork.getTitle());
                    cl01TvBrief.setText(artwork.getBrief());
                    tvFinancing.setText(artwork.getInvestsMoney() + "元");
                    double value = artwork.getInvestsMoney().doubleValue() / artwork.getInvestGoalMoney().doubleValue();
                    bar.setProgress((int) (value * 100));
                    prValue.setText((int) (value * 100) + "%");
                    goalMoney.setText(artwork.getInvestGoalMoney() + "");
                    remainTime.setText(artwork.getInvestRestTime());
                    goalPeople.setText(artwork.getInvestNum()+"");
                    if (artwork.getAuthor() != null) {
                        cl01TvName.setText(artwork.getAuthor().getName());
                        AppApplication.displayImage(artwork.getAuthor().getPictureUrl(), cl01CivHeadPortrait);
                        if (artwork.getAuthor().getMaster() != null) {
                            if (artwork.getAuthor().getMaster() != null) {
                                cl01TvZhicheng.setText(artwork.getAuthor().getMaster().getTitle());
                            }
                        }
                    }
                    if (isPraise1) {
                        dianzan.setImageResource(R.mipmap.dianzanhou);
                        dianzan.setEnabled(false);
                    }
                    remainMoney = artwork.getInvestGoalMoney().subtract(artwork.getInvestsMoney()).intValue();
                    zan.setText(artwork.getPraiseNUm() + "");
                    AppApplication.displayImage(artwork.getPicture_url(), cl01TvPrc);
                    EventBus.getDefault().post(object);
                }

            }
        });
    }

    private void isFirst(final List<User> users) {
        if(userImageViewMap == null) {
            userImageViewMap = new HashMap<ImageView, String>();
        }
        if (users.size() > count) {
            final LinearLayout linearLayout = new LinearLayout(RongZiXQActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < count - 1; j++) {
                final int  temp=j;
                final CircleImageView imageView = new CircleImageView(RongZiXQActivity.this);
                imageView.setTag(R.id.tag_first,users.get(j).getId());
                imageView.setTag(R.id.tag_second,users.get(j).getMaster());
                // 获取LayoutParams，给view对象设置宽度，高度
                final LayoutParams params = new LayoutParams(width, height);
                params.setMargins(0, 0, right, 0);
                NetRequestUtil.downloadImage(users.get(j).getPictureUrl(), new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setLayoutParams(params);
                        linearLayout.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (users.get(temp).getMaster()!=null){
                                    Intent intent=new Intent(RongZiXQActivity.this, ArtistIndexActivity.class);
                                    intent.putExtra("userId",users.get(temp).getId());
                                    intent.putExtra("name",users.get(temp).getName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    RongZiXQActivity.this.startActivity(intent);
                                }else {
                                    Intent intent=new Intent(RongZiXQActivity.this, UserIndexActivity.class);
                                    intent.putExtra("userId",users.get(temp).getId());
                                    intent.putExtra("name",users.get(temp).getName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    RongZiXQActivity.this.startActivity(intent);
                            }
                        }});
                        if (time == count - 2) {
                            CircleImageView imageView = new CircleImageView(RongZiXQActivity.this);
                            // 获取LayoutParams，给view对象设置宽度，高度
                            LayoutParams params = new LayoutParams(width, height);
                            params.setMargins(0, 0, right, 0);
                            imageView.setImageResource(R.mipmap.rongzixiangqing_zhankai);
                            imageView.setLayoutParams(params);
                            linearLayout.addView(imageView);
                            spread(imageView, users);
                        }

                        time++;
                    }
                });

            }
            touziren_ll.addView(linearLayout);
            time = 0;
        } else {
            final LinearLayout linearLayout = new LinearLayout(RongZiXQActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < users.size(); j++) {
                User user = users.get(j);

                final CircleImageView imageView = new CircleImageView(RongZiXQActivity.this);
                imageView.setTag(R.id.tag_first,users.get(j).getId());
                imageView.setTag(R.id.tag_second,users.get(j).getMaster());
                userImageViewMap.put(imageView, user.getId());
                // 获取LayoutParams，给view对象设置宽度，高度
                final LayoutParams params = new LayoutParams(width, height);
                params.setMargins(0, 0, right, 0);
                final int  temp=j;
                NetRequestUtil.downloadImage(users.get(j).getPictureUrl(), new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setLayoutParams(params);
                        linearLayout.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (users.get(temp).getMaster()!=null){
                                    Intent intent=new Intent(RongZiXQActivity.this, ArtistIndexActivity.class);
                                    intent.putExtra("userId",users.get(temp).getId());
                                    intent.putExtra("name",users.get(temp).getName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    RongZiXQActivity.this.startActivity(intent);
                                }else {
                                    Intent intent=new Intent(RongZiXQActivity.this, UserIndexActivity.class);
                                    intent.putExtra("userId",users.get(temp).getId());
                                    intent.putExtra("name",users.get(temp).getName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    RongZiXQActivity.this.startActivity(intent);
                                }
                            }});
                    }
                });

            }
            touziren_ll.addView(linearLayout);
        }
    }

    private void spread(final CircleImageView imageView, final List<User> users) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touziren_ll.removeAllViews();
                int xunHuan = 0;
                for (int i = 0; i <= ((users.size()) / count); i++) {
                    final LinearLayout linearLayout = new LinearLayout(RongZiXQActivity.this);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    final LayoutParams params = new LayoutParams(width, height);
                    params.setMargins(0, 0, right, right);
                    if (xunHuan < ((users.size()) / count)) {
                        for (int j = 0; j < count; j++) {
                            final int ciShu=i * count + j;
                            NetRequestUtil.downloadImage(users.get(i * count + j).getPictureUrl(), new BitmapCallback() {
                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(Bitmap response) {
                                    CircleImageView imageView = new CircleImageView(RongZiXQActivity.this);
                                    imageView.setImageBitmap(response);
                                    imageView.setLayoutParams(params);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (users.get(ciShu).getMaster()!=null){
                                                Intent intent=new Intent(RongZiXQActivity.this, ArtistIndexActivity.class);
                                                intent.putExtra("userId",users.get(ciShu).getId());
                                                intent.putExtra("name",users.get(ciShu).getName());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                RongZiXQActivity.this.startActivity(intent);
                                            }else {
                                                Intent intent=new Intent(RongZiXQActivity.this, UserIndexActivity.class);
                                                intent.putExtra("userId",users.get(ciShu).getId());
                                                intent.putExtra("name",users.get(ciShu).getName());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                RongZiXQActivity.this.startActivity(intent);
                                            }
                                        }
                                    });
                                    linearLayout.addView(imageView);
                                }
                            });
                        }
                    } else if (xunHuan == ((users.size()) / count)) {
                        time = 0;
                        for (int j = 0; j < ((users.size()) - (count * (xunHuan))); j++) {
                            final int degree = ((users.size()) - (count * (xunHuan)));
                            final int ciShu=i * count + j;
                            NetRequestUtil.downloadImage(users.get(i * count + j).getPictureUrl(), new BitmapCallback() {
                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(Bitmap response) {
                                    LayoutParams params = new LayoutParams(width, height);
                                    params.setMargins(0, 0, right, 0);
                                    CircleImageView imageView = new CircleImageView(RongZiXQActivity.this);
                                    imageView.setImageBitmap(response);
                                    imageView.setLayoutParams(params);
                                    linearLayout.addView(imageView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (users.get(ciShu).getMaster()!=null){
                                                Intent intent=new Intent(RongZiXQActivity.this, ArtistIndexActivity.class);
                                                intent.putExtra("userId",users.get(ciShu).getId());
                                                intent.putExtra("name",users.get(ciShu).getName());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                RongZiXQActivity.this.startActivity(intent);
                                            }else {
                                                Intent intent=new Intent(RongZiXQActivity.this, UserIndexActivity.class);
                                                intent.putExtra("userId",users.get(ciShu).getId());
                                                intent.putExtra("name",users.get(ciShu).getName());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                RongZiXQActivity.this.startActivity(intent);
                                            }
                                        }
                                    });
                                    if (time == degree - 1) {
                                        CircleImageView imageView1 = new CircleImageView(RongZiXQActivity.this);
                                        // 获取LayoutParams，给view对象设置宽度，高度
                                        imageView1.setImageResource(R.mipmap.rongzixiangqing_shouqianniu);
                                        imageView1.setLayoutParams(params);
                                        linearLayout.addView(imageView1);
                                        retract(imageView1, users);
                                    }
                                    time++;
                                }
                            });

                        }
                    }
                    xunHuan++;
                    touziren_ll.addView(linearLayout);
                    time = 0;
                }
            }
        });
    }

    private void retract(CircleImageView imageView, final List<User> users) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touziren_ll.removeAllViews();
                isFirst(users);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Artwork artwork) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        params.width = LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }

    private void shareWx(final int flag) {
       /* WXWebpageObject webpage = new WXWebpageObject();
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
    /*if(!api.isWXAppInstalled()) {
        Toast.makeText(WXEntryActivity.this, "您还未安装微信客户端",
                Toast.LENGTH_SHORT).show();
        return;
    }*/

        /*Map<String, String> paramsMap = new HashMap<>();
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
                Log.d("xxxxxxxxxxxxxxxxxxx","1");
                 String share_Url = (String) response.get("url");*/
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
        /*    }
        });*/

        /*webpage.webpageUrl = share_Url;
        Log.d("xxxxxxxxxxxxxxxxxxxxxxxxx","0");
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
        reqShare.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(reqShare);*/
    /*//创建一个用于封装待分享文本的WXTextObject对象

    WXTextObject textObject =new WXTextObject();

    textObject.text= text; //text为String类型

//创建WXMediaMessage对象，该对象用于Android客户端向微信发送数据

    WXMediaMessage msg =new WXMediaMessage();

    msg.mediaObject= textObject;

    msg.description= text;//text为String类型，设置描述，可省略*/


//    api.handleIntent(intent, this);
    }


}




