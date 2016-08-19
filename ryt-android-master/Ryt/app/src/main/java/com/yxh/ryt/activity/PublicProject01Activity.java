package com.yxh.ryt.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CustomDialogView;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.LoadingUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/11.
 */
public class PublicProject01Activity extends  BaseActivity {

    private LoadingUtil loadingUtil;
    private boolean isImage=false;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PublicProject01Activity.class));
    }
    public final int REQUEST_IMAGE=0;
    @Bind(R.id.btn_next)
    TextView btnNext;
    @Bind(R.id.ev_title)
    EditText evTitle;
    @Bind(R.id.ev_des)
    EditText evDes;
    @Bind(R.id.ev_duration)
    EditText evDuration;
    @Bind(R.id.ev_menoy)
    EditText evMenoy;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    String filePath="";
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_project_01);
        ButterKnife.bind(this);
        evTitle.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evDes.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evDuration.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evMenoy.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    @OnClick(R.id.btn_next)
    public void next(View v){
        oneStepRequst();
    }
    @OnClick(R.id.ib_top_lf)
    public void back(View v){
        finish();
    }
    @OnClick(R.id.iv_image)
    public void addImage(View v){
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                        getExternalStorageDirectory(), "pushArtFirst.jpg")));
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

    //艺术家发布项目第一步接口一网络请求
    private void oneStepRequst() {
        if (!isImage){
            ToastUtil.showShort(PublicProject01Activity.this,"没有选择图片!");
            return;
        }
        if ("".equals(evTitle.getText().toString())){
            ToastUtil.showShort(PublicProject01Activity.this,"项目标题不能为空!");
            return;
        }
        if ("".equals(evDes.getText().toString())){
            ToastUtil.showShort(PublicProject01Activity.this,"项目简介不能为空!");
            return;
        }
        if (evDes.getText().toString().length()>30){
            ToastUtil.showShort(PublicProject01Activity.this,"项目简介不能超过30!");
            return;
        }
        if ("".equals(evDuration.getText().toString())){
            ToastUtil.showShort(PublicProject01Activity.this,"创作时长不能为空");
            return;
        }
        if ("".equals(evMenoy.getText().toString())){
            ToastUtil.showShort(PublicProject01Activity.this,"融资金额不能为空");
            return;
        }

        loadingUtil = new LoadingUtil(PublicProject01Activity.this, PublicProject01Activity.this);
        loadingUtil.show();
        Map<String,File> fileMap=new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(),file);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("title",evTitle.getText().toString());
        paramsMap.put("duration",evDuration.getText().toString());
        //paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("investGoalMoney",evMenoy.getText().toString());
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("brief",evDes.getText().toString());
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "initNewArtWork.do", "picture_url", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(PublicProject01Activity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
               if ("0".equals(response.get("resultCode"))){
                   loadingUtil.dismiss();
                   Intent intent=new Intent(PublicProject01Activity.this,PublicProject02Activity.class);
                   intent.putExtra("artworkId", (String)response.get("artworkId")+"");
                   startActivity(intent);
                   finish();
               }else if ("000000".equals(response.get("resultCode"))){
                   SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                       @Override
                       public void getCode(String code) {
                           if ("0".equals(code)){
                               oneStepRequst();
                           }
                       }
                   });
                   sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
               }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int widthzong = metric.widthPixels; // 屏幕宽度（像素）
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                int height1=Utils.dip2px(PublicProject01Activity.this,224);
                int left1=Utils.dip2px(PublicProject01Activity.this,0);
                int right1=Utils.dip2px(PublicProject01Activity.this,0);
                int top1=Utils.dip2px(PublicProject01Activity.this,10);
                int bottom1=Utils.dip2px(PublicProject01Activity.this,0);

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(widthzong,height1);
                params1.setMargins(left1,top1,right1,bottom1);
                ivImage.setLayoutParams(params1);
                Bitmap bitmap = getBitmap(data.getData());
                isImage=true;
                int bmpWidth1  = bitmap.getWidth();
                int bmpHeight1  = bitmap.getHeight();
                float scaleWidth1  = (float) widthzong / bmpWidth1;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix1 = new Matrix();
                matrix1.postScale(scaleWidth1, scaleWidth1);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth1, bmpHeight1, matrix1, false);
                bitmap.recycle();
                ivImage.setImageBitmap(resizeBitmap1);
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/pushArtFirst.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                if (bitmap1==null){
                    break;
                }
                int height=Utils.dip2px(PublicProject01Activity.this,224);
                int left=Utils.dip2px(PublicProject01Activity.this,0);
                int right=Utils.dip2px(PublicProject01Activity.this,0);
                int top=Utils.dip2px(PublicProject01Activity.this,10);
                int bottom=Utils.dip2px(PublicProject01Activity.this,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthzong,height);
                params.setMargins(left,top,right,bottom);
                isImage=true;
                ivImage.setLayoutParams(params);
                int bmpWidth  = bitmap1.getWidth();
                int bmpHeight  = bitmap1.getHeight();
                float scaleWidth  = (float) widthzong / bmpWidth;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap = Bitmap.createBitmap(bitmap1, 0, 0, bmpWidth, bmpHeight, matrix, false);
                ivImage.setImageBitmap(resizeBitmap);
                break;
            default:
                break;
        }
    }
    public Bitmap getBitmap(Uri data){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
       /* if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){*/
        String filePath1 = GetPathFromUri4kitkat.getPath(data);
        /*}else{
            filePath= ImageUtils.getRealPathByUriOld(data);
        }*/
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath1, options);
        if (bm==null){
            return null;
        }
        Bitmap bitmap1=compressImage(bm);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/pushArtFirst"+Utils.getImageFormat(filePath1));
        try {
            filePath=file.getPath();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            file = new File(getFilesDir(), "pushArtFirst"+Utils.getImageFormat(filePath1));
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return bitmap1;
    }
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format=Bitmap.CompressFormat.JPEG;
        image.compress(format, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        int length = baos.toByteArray().length;
        while ( baos.toByteArray().length / 1024>300) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
