package com.yxh.ryt.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yxh.ryt.adapter.UserYsjTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.UserJianJieFragment;
import com.yxh.ryt.fragment.UserTouGuoFragment;
import com.yxh.ryt.fragment.UserZanGuoFragment;
import com.yxh.ryt.fragment.YSJHomeFragment;
import com.yxh.ryt.fragment.YSJWorkFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class UserYsjIndexActivity extends BaseActivity implements StickHeaderViewPagerManager.OnListViewScrollListener {
    private String userId;
    private String currentId;
    private boolean homeFlag;
    private IWXAPI api;
    private StickHeaderLayout root;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserYsjIndexActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    private String artworkId;
    RelativeLayout rlUserIndex;
    @Bind(R.id.tv_user_header_gz_num)
    TextView tvUserHeaderGzNum;
    @Bind(R.id.tv_user_header_gz)
    TextView tvUserHeaderGz;
    @Bind(R.id.rs_iv_headPortrait)
    CircleImageView rsIvHeadPortrait;
    @Bind(R.id.tv_user_header_name)
    TextView tvUserHeaderName;
    @Bind(R.id.ll_user_header)
    LinearLayout llUserHeader;
    @Bind(R.id.tv_user_header_fs_num)
    TextView tvUserHeaderFsNum;
    @Bind(R.id.tv_user_header_fs)
    TextView tvUserHeaderFs;
    @Bind(R.id.tv_user_header_txt)
    TextView tvUserHeaderTxt;
    @Bind(R.id.tv_user_header_je_value_01)
    TextView tvUserHeaderJeValue01;
    @Bind(R.id.tv_user_header_je_txt_01)
    TextView tvUserHeaderJeTxt01;
    @Bind(R.id.tv_user_header_je_value_02)
    TextView tvUserHeaderJeValue02;
    @Bind(R.id.tv_user_header_je_txt_02)
    TextView tvUserHeaderJeTxt02;
    @Bind(R.id.tv_user_header_je_value_03)
    TextView tvUserHeaderJeValue03;
    @Bind(R.id.tv_user_header_je_txt_03)
    TextView tvUserHeaderJeTxt03;
    @Bind(R.id.uh1_ll_other)
    LinearLayout other;
    @Bind(R.id.uh1_iv_privateLetter)
    ImageView letter;

    private ImageView attention;
    private TextView top;
    private PushWorkReceiver receiver;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 4;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID); //初始化api
        api.registerApp(Constants.APP_ID); //将APP_ID注册到微信中
        setContentView(R.layout.user_ysj_index);
        ButterKnife.bind(this);
        attention=(ImageView) findViewById(R.id.uh1_iv_attention);
        top=(TextView) findViewById(R.id.tv_top_ct);
        userId = getIntent().getStringExtra("userId");
        currentId = getIntent().getStringExtra("currentId");
        mViewPager = (ViewPager) findViewById(R.id.user_ysj_pager);
         root = (StickHeaderLayout) findViewById(R.id.user_ysj_root);
        manager = new StickHeaderViewPagerManager(root, mViewPager);
        manager.setOnListViewScrollListener(this);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(YSJHomeFragment.newInstance(manager, 0, false, userId, currentId));
        /*manager1 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(UserJianJieFragment.newInstance(manager, 1, false,userId));
        /*manager2 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(YSJWorkFragment.newInstance(manager, 2, false,userId,currentId));
        /*manager3 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(UserTouGuoFragment.newInstance(manager, 3, false, userId, currentId));
       /* manager4 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(UserZanGuoFragment.newInstance(manager, 4, false,userId,currentId));
        UserYsjTabPageIndicatorAdapter pagerAdapter = new UserYsjTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.user_ysj_indicator);
        indicator.setViewPager(mViewPager);
        receiver = new PushWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FW_BROADCAST");
        registerReceiver(receiver, filter);
        other.setVisibility(View.GONE);

    }
    @Override
    public void onResume() {
        super.onResume();
        /*if (AppApplication.gUser!=null && AppApplication.gUser.getId()!=null && !"".equals(AppApplication.gUser.getId())){*/
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("userId", userId);
            //paramsMap.put("currentId", currentId);
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
                }

                @Override
                public void onResponse(Map<String, Object> response) {
                    if (!response.get("resultCode").equals("0")) {
                        ToastUtil.showShort(AppApplication.getSingleContext(), "获取数据失败!");
                        return;
                    }
                    if (response.get("resultCode").equals("0")) {
                        Map<String, Object> pageInfo = (Map<String, Object>) response.get("pageInfo");
                        final User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(pageInfo.get("user")), User.class);
                        if (AppApplication.getSingleGson()!=null &&AppApplication.getSingleGson().toJson(pageInfo.get("followNum"))!=null &&tvUserHeaderFsNum!=null){
                            tvUserHeaderFsNum.setText(AppApplication.getSingleGson().toJson(pageInfo.get("followNum")));
                        }
                        if (AppApplication.getSingleGson()!=null &&AppApplication.getSingleGson().toJson(AppApplication.getSingleGson().toJson(pageInfo.get("num")))!=null&&tvUserHeaderGzNum!=null){
                            tvUserHeaderGzNum.setText(AppApplication.getSingleGson().toJson(pageInfo.get("num")));
                        }
                        boolean followed = (boolean) pageInfo.get("followed");
                        if (followed){
                            attention.setImageResource(R.mipmap.guanzhuhou);
                        }else {
                            attention.setImageResource(R.mipmap.guanzhuqian);
                        }
                        if (user != null) {
                            setLoginedViewValues(user);

                                letter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        letterTrans(user);

                                    }
                                });

                        }
                        root.getStickHeaderView().requestLayout();
                    }

                }
            });
        /*}*//*else {
//            ButterKnife.apply(linearLayouts, ISVISIBLE, 1);
            setLoginViewValues();
            return;
        }*/
    }

    private void letterTrans(User user) {
        Intent intent=new Intent(this,MsgActivity.class);
        intent.putExtra("userId",currentId);
        intent.putExtra("currentName",AppApplication.gUser.getName());
        intent.putExtra("formId", userId);
        intent.putExtra("name", user.getName());
        startActivity(intent);
    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues(User user) {
        top.setText(user.getName());
        AppApplication.displayImage(user.getPictureUrl(), rsIvHeadPortrait);
        tvUserHeaderName.setText(user.getName());
       /* tvUserHeaderFsNum.setText(user.getCount1()+"");
        tvUserHeaderGzNum.setText(user.getCount()+"");*/
        tvUserHeaderTxt.setText(user.getUserBrief()==null?"一句话20字以内":user.getUserBrief().getSigner()+"");
        tvUserHeaderJeValue01.setText("￥"+user.getInvestsMoney());
        tvUserHeaderJeValue02.setText("￥" + user.getRoiMoney());
        tvUserHeaderJeValue03.setText(0 == user.getRate() ? "0%" : user.getRate() * 100 + "%");
        tvUserHeaderJeTxt01.setText("项目总金额");
        tvUserHeaderJeTxt02.setText("项目拍卖总金额");
        tvUserHeaderJeTxt03.setText("拍卖溢价率");

    }
    //未登录成功设置控件元素的值
    private void setLoginViewValues() {
        tvUserHeaderFsNum.setText("0");
        tvUserHeaderGzNum.setText("0");
        tvUserHeaderName.setText("游客");
        tvUserHeaderTxt.setText("一句话20字以内");
        tvUserHeaderJeValue01.setText("￥0");
        tvUserHeaderJeValue02.setText("￥0");
        tvUserHeaderJeValue03.setText("0%");
        tvUserHeaderJeTxt01.setText("项目总金额");
        tvUserHeaderJeTxt02.setText("项目拍卖总金额");
        tvUserHeaderJeTxt03.setText("拍卖溢价率");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unregisterReceiver(receiver);
    }

    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        homeFlag=firstVisibleItem+visibleItemCount==totalItemCount+1;
    }

    @Override
    public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
        if (R.id.fiyh_lstv==view.getId() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&homeFlag){
            Intent intent = new Intent("android.intent.action.HOME_BROADCAST");
            sendBroadcast(intent);
        }else if (R.id.fit_lstv==view.getId() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&homeFlag){
            Intent intent = new Intent("android.intent.action.CAST_BROADCAST");
            sendBroadcast(intent);
        }else if (R.id.fiz_lstv==view.getId() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&homeFlag){
            Intent intent = new Intent("android.intent.action.PRAISE_BROADCAST");
            sendBroadcast(intent);
        }else if (R.id.fiyw_lstv==view.getId() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&homeFlag){
            Intent intent = new Intent("android.intent.action.WORK_BROADCAST");
            sendBroadcast(intent);
        }
    }
    public class PushWorkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            new ActionSheetDialog(UserYsjIndexActivity.this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                            getExternalStorageDirectory(), "pushWork.jpg")));
                                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                                }
                            })
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                /*Bitmap bitmap = getBitmap(data.getData());
//                Bitmap bitmap=getBitmapFromUri(data.getData());
                circleImageView.setImageBitmap(bitmap);*/
//                saveFile(bitmap);
                Intent intent=new Intent(this,PushWoraActivity.class);
                intent.putExtra("intent",data.getData());
                startActivity(intent);
                break;
            case CAMERA_REQUEST_CODE:
                if (data==null){
                    return;
                }
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/pushWork.jpg");
                Intent intent1=new Intent(this,PushWoraActivity.class);
                intent1.putExtra("intent",Uri.fromFile(picture));
                startActivity(intent1);
                break;
            case CROP_REQUEST_CODE:
//                if (data == null) {
//                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
//                    return;
//                }
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 4;  //这里表示原来图片的1/4
//                    photo.
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//                    BitmapFactory.decodeStream(isBm, null, options);
//                    final int i = ImageUtils.calculateInSampleSize(new ImageUtils.ImageSize(options.outWidth, options.outHeight), new ImageUtils.ImageSize(100, 100));
//                }
//                break;
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
    @OnClick({R.id.ib_top_lf,R.id.ll_header_gz,R.id.ll_header_fs,R.id.ib_top_rt})
    public void dianji(View view){
        switch (view.getId()){
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.ib_top_rt:
                showShareDialog();
                break;
            case R.id.ll_header_gz:
                if (AppApplication.gUser!=null && !"".equals(AppApplication.gUser.getId())){
                    Intent intent=new Intent(this, AttentionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", currentId);
                    intent.putExtra("otherUserId",userId);
                    intent.putExtra("flag","1");

                    startActivity(intent);
                }else {
                    Intent intent=new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.ll_header_fs:
                if (AppApplication.gUser!=null && !"".equals(AppApplication.gUser.getId())){
                    Intent intent=new Intent(this, FansActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", currentId);
                    intent.putExtra("otherUserId",userId);
                    intent.putExtra("flag","1");
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }

    }
}

