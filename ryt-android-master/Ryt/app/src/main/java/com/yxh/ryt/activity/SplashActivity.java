package com.yxh.ryt.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.UpdataCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.ToastUtil;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


public class SplashActivity extends BaseActivity {

    private RelativeLayout mRoot;
    protected static final int UPDATE_DIALOG = 100;
    protected static final int URL_ERROR = 101;
    protected static final int NET_ERROR = 102;
    protected static final int JSON_ERROR = 103;
    protected static final int NO_UPDATE = 104;
    private static final int INSTALL_CODE = 100012;
    private TextView tvVersion;
    private String desc;
    private String downloadUrl;
    private SharedPreferences mSp;
    private String versionCode;
    private Map<String, String> paramsMap = new HashMap<>();

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case NO_UPDATE:
                    enterHome();
                    finish();
                    break;
                case URL_ERROR:
                    ToastUtil.showShort(getApplicationContext(), "网络异常");
                    enterHome();
                    break;
                case NET_ERROR:
                    ToastUtil.showShort(getApplicationContext(), "网络异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.showShort(getApplicationContext(), "数据异常");
                    enterHome();
                    break;
                default:
                    break;
            }
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        setShare();
        startAnimation();
       /* mSp = getSharedPreferences("config", Context.MODE_PRIVATE);
        // 根据保存的状态 判断是否更新
        boolean isUpdate = mSp.getBoolean(Constants.AUTO_UPDATE, true);
        if (isUpdate) {
            checkVersion();
        } else {
            handler.sendEmptyMessageDelayed(NO_UPDATE, 2000);
        }*/

    }

    private void setShare() {
        AppApplication.gUser.setMaster1((String) SPUtil.get(AppApplication.getSingleContext(), "current_master", ""));
        AppApplication.gUser.setId((String) SPUtil.get(AppApplication.getSingleContext(), "current_id", ""));
        AppApplication.gUser.setName((String) SPUtil.get(AppApplication.getSingleContext(), "current_name", ""));
        AppApplication.gUser.setUsername((String) SPUtil.get(AppApplication.getSingleContext(), "current_username", ""));
        AppApplication.gUser.setSex((String) SPUtil.get(AppApplication.getSingleContext(), "current_sex", ""));
        AppApplication.gUser.setPictureUrl((String) SPUtil.get(AppApplication.getSingleContext(), "current_pictureUrl", ""));
        AppApplication.gUser.setLoginState((String) SPUtil.get(AppApplication.getSingleContext(), "current_loginState", ""));
        AppApplication.gUser.setPassword((String) SPUtil.get(AppApplication.getSingleContext(), "current_password", ""));
    }

    private void checkVersion() {
        String version_id = "V" + getVersionCode();
        String version_mini = getVersionName();
        String version_code = version_id + "_" + version_mini;
        paramsMap = new HashMap<>();
        paramsMap.put("platform", "android");
        paramsMap.put("version_id", version_id);
        paramsMap.put("version_mini", version_mini);
        paramsMap.put("version_code", version_code);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "upgrade.do", paramsMap, new UpdataCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                enterHome();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Message msg = Message.obtain();
                //Log.w("YZJ",response.get("resultCode").toString());
                if ("100013".equals(response.get("resultCode"))) {
                    Map<String, String> map = (Map<String, String>) response.get("version_info");

                    downloadUrl = map.get("apk_url");
                    versionCode = map.get("version_code");
                    desc = map.get("resultMsg");
                    if (getVersionCode().equals(versionCode)) {
                        msg.what = NO_UPDATE;
                    } else {
                        msg.what = UPDATE_DIALOG;
                    }
                } else {
                   enterHome();
                }

                handler.sendMessage(msg);
            }
        });

    }

    protected void enterHome() {
       /* startActivity(new Intent(SplashActivity.this, IndexActivity.class));*/
         boolean isfirstenter = (boolean) SPUtil.get(getApplicationContext(), Constants.ISFIRSTENTER, true);
            if (isfirstenter) {
                //跳转到引导界面
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                //将登录标志位设置为false，下次登录时不在显示首次登录界面
                SPUtil.put(getApplicationContext(),Constants.ISFIRSTENTER, false);
            } else {
                //跳转到首页
                startActivity(new Intent(SplashActivity.this, IndexActivity.class));

            }
        finish();
    }

    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本更新提醒");// 设置标题
        builder.setMessage(desc);// 设置提醒内容
        builder.setPositiveButton("立刻升级",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk(downloadUrl);
                    }
                });
        builder.setNegativeButton("稍后再说",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                });
        // 监听用户主动消失掉dialog
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }


    protected void downloadApk(String url) {
        // 判断sd卡是否已挂载
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            enterHome();
            return;
        }
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        String target = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/ryt.apk";
     /*   String version_id = getVersionCode().substring(0, getVersionCode().indexOf("_"));
        String version_mini = getVersionCode().substring(getVersionCode().indexOf("_") + 1);
        paramsMap = new HashMap<>();
        paramsMap.put("platform", "android");
        paramsMap.put("version_id", version_id);
        paramsMap.put("version_mini", version_mini);
        paramsMap.put("version_code", getVersionCode());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("YYYYYYYY", versionCode);*/
        NetRequestUtil.download(url, paramsMap, new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "ryt.apk") {

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(File response) {
                ToastUtil.show(getApplicationContext(), "下载成功", 0);
                pd.dismiss();
                installApk(response);

            }

            @Override
            public void inProgress(float currentBytes, long allBytes) {
                int progress = (int) (currentBytes * 100 / allBytes);
                Log.e("Download", progress + "");
                pd.setMax(100);
                pd.setProgress(progress);

            }

        });
    }

    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }



    private String getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 安装apk 隐式调用系统安装界面
     *
     * @param file
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivityForResult(intent, INSTALL_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_CODE) {
            enterHome();
        }
    }



    private void initView() {
        mRoot = (RelativeLayout) findViewById(R.id.rel_splash_root);
    }

    private void startAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);


        AnimationSet animationSet = new AnimationSet(true);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //执行动画
        mRoot.startAnimation(animationSet);
        animationSet.setAnimationListener(animationListener);
    }

    /**
     * 动画监听
     */
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        //动画开始的时候调用
        @Override
        public void onAnimationStart(Animation animation) {

        }

        //动画重复执行调用
        @Override
        public void onAnimationRepeat(Animation animation) {


        }

        //动画结束调用的方法
        @Override
        public void onAnimationEnd(Animation animation) {
            //跳转界面
            //判断，如果是第一次打开，跳转引导页面，如果不是第一次打开，跳转到首页
            checkVersion();
           /* boolean isfirstenter = (boolean) SPUtil.get(getApplicationContext(), Constants.ISFIRSTENTER, true);
            if (isfirstenter) {
                //跳转到引导界面
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            } else {
                //跳转到首页
                startActivity(new Intent(SplashActivity.this, IndexActivity.class));

            }*/

           // finish();
        }
    };

}
