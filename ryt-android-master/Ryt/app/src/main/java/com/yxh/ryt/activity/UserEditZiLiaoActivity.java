package com.yxh.ryt.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.crop.UCrop;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserEditZiLiaoActivity extends BaseActivity implements View.OnClickListener {

    private boolean flag = false;
    String filePath = "";
    private String username;
    private CircleImageView circleImageView;
    private ImageView imageView;
    private RelativeLayout nickName;
    private RelativeLayout sign_message;
    private RelativeLayout rl_sex;
    private TextView iv_sign;
    private TextView tv_nickname;
    private TextView tv_sex;
    private EditReceiver receiver;
    private Double sex;
    private RelativeLayout address;


    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记
    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri mDestinationUri;

    /**
     * 图片选择的监听回调
     */
    private ActionSheetDialog dialog;
    private AlertDialog mAlertDialog;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserEditZiLiaoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_ziliao);
        circleImageView = (CircleImageView) findViewById(R.id.uez_iv_icon);
        imageView = (ImageView) findViewById(R.id.ib_top_lf);
        nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
        sign_message = (RelativeLayout) findViewById(R.id.rl_sign);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        iv_sign = (TextView) findViewById(R.id.iv_sign);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        address = (RelativeLayout) findViewById(R.id.rl_address);
        //给控件设置内容
        //AppApplication.displayImage(AppApplication.gUser.getPictureUrl(), circleImageView);


        inflatSign();
        //设置点击
        circleImageView.setOnClickListener(this);
        imageView.setOnClickListener(this);
        nickName.setOnClickListener(this);
        sign_message.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        address.setOnClickListener(this);
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage1.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        //注册这两个广播
         receiver = new EditReceiver();
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("android.intent.action.EDIT_NICK_BROADCAST");
        myFilter.addAction("android.intent.action.EDIT_SIGN_BROADCAST");
        AppApplication.getSingleContext().registerReceiver(receiver, myFilter);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:

                if (data == null) {
                    return;
                }
                Bitmap bitmap = getBitmap(data.getData());
//                Bitmap bitmap=getBitmapFromUri(data.getData());
                circleImageView.setImageBitmap(bitmap);
//                saveFile(bitmap);
                flag = true;
                commitHead();
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/editContent.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                *//* Bitmap bitmap2 = Utils.rotaingImageView(filePath, bitmap1);
                bitmap1.recycle();
                circleImageView.setImageBitmap(bitmap2);*//*
                circleImageView.setImageBitmap(bitmap1);
                commitHead();

                break;
//
            default:
                break;
        }
    }*/

    //上传头像
    public void commitHead() {
        Map<String, File> fileMap = new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(), file);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "editPicUrl.do", "headPortrait", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(UserEditZiLiaoActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    getUser(response);
                    ToastUtil.showLong(UserEditZiLiaoActivity.this,"修改图片成功!");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                commitHead();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    public Bitmap getBitmap(Uri data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
       /* if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){*/
        String filePath1 = GetPathFromUri4kitkat.getPath(data);
        /*}else{
            filePath= ImageUtils.getRealPathByUriOld(data);
        }*/
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath1, options);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/editContent"+Utils.getImageFormat(filePath1));
        try {
            filePath=file.getPath();
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            file = new File(getFilesDir(), "upLoad"+Utils.getImageFormat(filePath1));
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return bm;
    }
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            filePath = GetPathFromUri4kitkat.getPath(uri);
            System.out.println(filePath);
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getUser(Map<String, Object> response) {
        User user = new User();
        user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
        if (user!=null){
            SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_username", user.getUsername() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_name", user.getName() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_sex", user.getSex() + "");
        }
        if (user.getUserBrief()!=null){
            SPUtil.put(AppApplication.getSingleContext(), "current_userBrief", user.getUserBrief().getSigner() + "");
        }
        if (user.getMaster() != null) {
            SPUtil.put(AppApplication.getSingleContext(), "current_master", "master");
        } else {
            SPUtil.put(AppApplication.getSingleContext(), "current_master", "");
        }
        SPUtil.put(AppApplication.getSingleContext(), "current_pictureUrl", user.getPictureUrl() + "");
        AppApplication.gUser = user;
        if (user.getMaster() != null) {
            AppApplication.gUser.setMaster1("master");
        } else {
            AppApplication.gUser.setMaster1("");
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uez_iv_icon:
                // 设置图片点击监
                dialog= new ActionSheetDialog(this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        takePhoto();
                                    }
                                })
                        .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        pickFromGallery();
                                    }
                                })
                ;
                dialog.show();
                break;
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.rl_nickName:
                startActivity(new Intent(this, EditNicknameActivity.class));
                break;
            case R.id.rl_sign:
                startActivity(new Intent(this, EditSignActivity.class));
                break;
            case R.id.rl_sex:
                new ActionSheetDialog(UserEditZiLiaoActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("男", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_sex.setText("男");
                                        postSex("1");
                                    }
                                })
                        .addSheetItem("女", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_sex.setText("女");
                                        postSex("2");
                                    }
                                })
                        .addSheetItem("保密", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_sex.setText("保密");
                                        postSex("0");
                                    }
                                })
                        .show();
                break;
            case R.id.rl_address:
                startActivity(new Intent(this,ReceiverAdressActivity.class));
                break;
            default:
                break;
        }
    }
    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "选择图片时需要读取权限",
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    "拍照时需要存储权限",
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
    }
    @SuppressLint("NewApi")
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showAlertDialog("权限需求", rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    }, "确定", null, "取消");
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }
    /**
     * 显示指定标题和信息的对话框
     *
     * @param title                         - 标题
     * @param message                       - 信息
     * @param onPositiveButtonClickListener - 肯定按钮监听
     * @param positiveText                  - 肯定按钮信息
     * @param onNegativeButtonClickListener - 否定按钮监听
     * @param negativeText                  - 否定按钮信息
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(temp));

                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withTargetActivity(CropActivity.class)
                .start(this);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri ) {
            Bitmap bitmap = getBitmap(resultUri);
            circleImageView.setImageBitmap(bitmap);
            commitHead();
        } else {
            Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }
    public void postSex(String sex) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("type", "14");
        paramsMap.put("content", sex);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "editProfile.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Map<String, Object> response) {
                //getUser(response);
            }
        });
    }

    public class EditReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
                if(action.equals("android.intent.action.EDIT_NICK_BROADCAST")){
                    String name = intent.getExtras().getString("nick");
                    tv_nickname.setText(name);
                }else if (action.equals("android.intent.action.EDIT_SIGN_BROADCAST")){
                    String signer = intent.getExtras().getString("signer");
                    iv_sign.setText(signer);
                }
            }

    }

    //填充签名,性别
    public void inflatSign(){
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
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Map<String, Map<String,String>>> map1 = (Map<String, Map<String, Map<String,String>>>) response.get("pageInfo");
                Map<String, Map<String,String>> map2 =null;
                if (map1!=null){
                    map2 =  map1.get("user");
                }
                Map<String,String> map3 =null;
                if (map2!=null){
                    map3=map2.get("userBrief");
                }
                String sign =null;
                if (map3==null){
                    sign = "";
                }else {
                    sign = map3.get("signer");
                }
                Map<String,Map<String,Double>> map11 = (Map<String, Map<String, Double>>) response.get("pageInfo");
                Map<String,Double> map22 = null;
                if (map11 != null) {
                    map22 = map11.get("user");
                }

                if (map22 !=null){
                    sex = map22.get("sex");
                }
                Map<String,Map<String,String>> map111 = (Map<String, Map<String, String>>) response.get("pageInfo");
                Map<String,String> map222 = null;
                if (map111 != null) {
                    map222 = map111.get("user");
                }
                String name = "";
                if (map222 != null){
                    name = map222.get("name");
                }
                String pictureUrl = "";
                if (map222 != null){
                    pictureUrl = map222.get("pictureUrl");
                }
                //AppApplication.getSingleGson().toJson(response.get("followsNum"));
                /*Map<String,Map<String,Double>> map11 = (Map<String, Map<String, Double>>) response.get("pageInfo");
                Map<String,Double> map22 = map11.get("user");
                Double sex = map22.get("sex");
                Map<String,Map<String,String>> map111 = (Map<String, Map<String, String>>) response.get("pageInfo");
                Map<String,String> map222 = map111.get("user");
                String name = map222.get("name");*/
                /*User user = new User();
                user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfo")), User.class);*/
                if (sign == null) {
                    iv_sign.setText("");
                }else{
                iv_sign.setText(sign);
                }
                if (sex != null) {
                    tv_sex.setText(changSex(sex));
                }else{
                    tv_sex.setText(changSex(3.0));
                }
                tv_nickname.setText(name);
                AppApplication.displayImage(AppApplication.gUser.getPictureUrl(), circleImageView);
            }
        });
    }

    public String changSex(Double s) {
        if(s == 2.0) {
            return "女";
        }else if(s == 1.0){
            return "男";
        }else{
            return "保密";
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        inflatSign();
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            AppApplication.getSingleContext().unregisterReceiver(receiver);
        }
    }
}
