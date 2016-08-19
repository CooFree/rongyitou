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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.LoginCallBack;
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
import com.yxh.ryt.util.phote.util.Bimp;
import com.yxh.ryt.util.phote.util.ImageItem;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.Artworkdirection;
import com.yxh.ryt.vo.Image;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 *
 */
public class EditProject01Activity extends  BaseActivity {
    private int size;
    private String artWorkId;
    private String currentUserId;
    private Artworkdirection artworkDirection;
    private String description;
    private LoadingUtil loadingUtil;
    private boolean isImage=false;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, EditProject01Activity.class));
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
    List<String> ImageList;
    private  boolean next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_project_01);
        ButterKnife.bind(this);
        artWorkId = getIntent().getStringExtra("artWorkId");
        currentUserId = getIntent().getStringExtra("currentUserId");
        evTitle.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evDes.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evDuration.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evMenoy.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        loadData();
    }
    private void loadData() {
        Map<String,String> paramsMap=new HashMap<>();
        /*paramsMap.put("artWorkId", "imyt7yax314lpzzj");
        paramsMap.put("currentUserId", "imhfp1yr4636pj49");*/
        paramsMap.put("artWorkId", artWorkId);
        //paramsMap.put("currentUserId", currentUserId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg=EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
                ToastUtil.showLong(EditProject01Activity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                ImageList = new ArrayList<String>();
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    final Artwork artwork=AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")),Artwork.class);
                    evTitle.setText(artwork.getTitle());
                    evDes.setText(artwork.getBrief());
                    evDuration.setText(artwork.getDuration()+"");
                    evMenoy.setText(artwork.getInvestGoalMoney()+"");
                    AppApplication.displayImage(artwork.getPicture_url(),ivImage);
                    NetRequestUtil.downloadImage(artwork.getPicture_url(), new BitmapCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(Bitmap response) {
                            if (response != null) {
                                File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator);
                                if (!sampleDir.exists()) {
                                    sampleDir.mkdirs();
                                }
                                if (!sampleDir.isDirectory()) {
                                    sampleDir.delete();
                                    sampleDir.mkdirs();
                                }
                                File mRecordFile = null;
                                try {
                                    mRecordFile = File.createTempFile("" + System.currentTimeMillis(), Utils.getImageFormat(artwork.getPicture_url()), sampleDir); //mp4格式
                                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mRecordFile));
                                    Log.d("xxxxxxxxxxxxxxxxxxxx", Utils.getImageFormatBig(artwork.getPicture_url())+"");
                                    response.compress(Utils.getImageFormatBig(artwork.getPicture_url()), 100, bos);
                                    bos.flush();
                                    bos.close();
                                    filePath=mRecordFile.getPath();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                   description=artwork.getDescription();
                    artworkDirection=AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkdirection")),Artworkdirection.class);
                    List<Image> list = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkAttachmentList")), new TypeToken<List<Image>>() {
                    }.getType());
                    if (list.size() > 9) {
                        for (int i = 0; i < 9; i++) {
                            ImageList.add(list.get(i).getFileName());
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            ImageList.add(list.get(i).getFileName());
                        }
                    }
                    Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
                    for (int i = 0; i < ImageList.size(); i++) {
                        final int finalI = i;
                        NetRequestUtil.downloadImage(ImageList.get(i), new BitmapCallback() {
                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(Bitmap response) {
                                if (response != null) {
                                    File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "image" + File.separator);
                                    if (!sampleDir.exists()) {
                                        sampleDir.mkdirs();
                                    }
                                    if (!sampleDir.isDirectory()) {
                                        sampleDir.delete();
                                        sampleDir.mkdirs();
                                    }
                                    File mRecordFile = null;
                                    try {
                                        mRecordFile = File.createTempFile("" + System.currentTimeMillis(), Utils.getImageFormat(ImageList.get(finalI)), sampleDir); //mp4格式
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mRecordFile));
                                        response.compress(Utils.getImageFormatBig(ImageList.get(finalI)), 100, bos);
                                        bos.flush();
                                        bos.close();
                                        ImageItem imageItem = new ImageItem();
                                        imageItem.setImagePath(mRecordFile.getAbsolutePath());
                                        Bimp.tempSelectBitmap.add(imageItem);
                                        size++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
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
    @OnClick({R.id.ib_top_lf,R.id.btn_next})
    public void next(View v){
        switch (v.getId()){
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.btn_next:
                oneStepRequst();
                break;
        }

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
                                        getExternalStorageDirectory(), "editArtFirst.jpg")));
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
            ToastUtil.showShort(EditProject01Activity.this,"没有选择图片!");
            return;
        }
        if ("".equals(evTitle.getText().toString())){
            ToastUtil.showShort(EditProject01Activity.this,"项目标题不能为空!");
            return;
        }
        if ("".equals(evDes.getText().toString())){
            ToastUtil.showShort(EditProject01Activity.this,"项目简介不能为空!");
            return;
        }
        if (evDes.getText().toString().length()>30){
            ToastUtil.showShort(EditProject01Activity.this,"项目简介不能超过30!");
            return;
        }
        if ("".equals(evDuration.getText().toString())){
            ToastUtil.showShort(EditProject01Activity.this,"创作时长不能为空");
            return;
        }
        if ("".equals(evMenoy.getText().toString())){
            ToastUtil.showShort(EditProject01Activity.this,"融资金额不能为空");
            return;
        }
        loadingUtil = new LoadingUtil(EditProject01Activity.this, EditProject01Activity.this);
        /*View view = LayoutInflater.from(this).inflate(
                R.layout.dilog_withwait, null);

        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        customDialogView = (CustomDialogView) view.findViewById(R.id.view_customdialog);
        redrawCdvRunnable = new RedrawCustomDialogViewThread();
        new Thread(redrawCdvRunnable).start();*/
        loadingUtil.show();
        Map<String,File> fileMap=new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(),file);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("title",evTitle.getText().toString());
        paramsMap.put("duration",evDuration.getText().toString());
        //paramsMap.put("userId",currentUserId);
        //paramsMap.put("userId","imhfp1yr4636pj49");
        paramsMap.put("investGoalMoney",evMenoy.getText().toString());
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
            paramsMap.put("artWorkId",artWorkId);
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
                ToastUtil.showLong(EditProject01Activity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    if (size==ImageList.size()){
                        /*redrawCdvRunnable.setRun(false);*/
                        loadingUtil.dismiss();
                        Intent intent=new Intent(EditProject01Activity.this,EditProject02Activity.class);
                        intent.putExtra("artworkId", (String)response.get("artworkId")+"");
                        intent.putExtra("description",description);
                        if (artworkDirection!=null){
                            intent.putExtra("make_instru",artworkDirection.getMake_instru());
                            intent.putExtra("financing_aq",artworkDirection.getFinancing_aq());
                        }
                        startActivity(intent);
                        finish();
                    }
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
    protected void onDestroy() {
        super.onDestroy();
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
                int height1=Utils.dip2px(EditProject01Activity.this,224);
                int left1=Utils.dip2px(EditProject01Activity.this,14);
                int right1=Utils.dip2px(EditProject01Activity.this,14);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(widthzong,height1);
                params1.setMargins(left1,0,right1,0);
                ivImage.setLayoutParams(params1);
                Bitmap bitmap = getBitmap(data.getData());
                isImage=true;
                /*Bitmap btm2=Bitmap.createScaledBitmap(bitmap, widthzong, height1, false); //自定义
                bitmap.recycle();*/
                int bmpWidth1  = bitmap.getWidth();
                int bmpHeight1  = bitmap.getHeight();
                float scaleWidth1  = (float) widthzong / bmpWidth1;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix1 = new Matrix();
                matrix1.postScale(scaleWidth1, scaleWidth1);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth1, bmpHeight1, matrix1, false);
                //bitmap.recycle();
                ivImage.setImageBitmap(resizeBitmap1);
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/editArtFirst.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                if (bitmap1==null){
                    break;
                }
                isImage=true;
                int height=Utils.dip2px(EditProject01Activity.this,224);
                int left=Utils.dip2px(EditProject01Activity.this,14);
                int right=Utils.dip2px(EditProject01Activity.this,14);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthzong,height);
                params.setMargins(left,0,right,0);
                ivImage.setLayoutParams(params);
                int bmpWidth  = bitmap1.getWidth();
                int bmpHeight  = bitmap1.getHeight();
                float scaleWidth  = (float) widthzong / bmpWidth;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap = Bitmap.createBitmap(bitmap1, 0, 0, bmpWidth, bmpHeight, matrix, false);
                bitmap1.recycle();
                /*Bitmap btm3=Bitmap.createScaledBitmap(bitmap2, widthzong, height, false); //自定义
                bitmap2.recycle();*/
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
                + "/editArtFirst"+Utils.getImageFormat(filePath1));
        try {
            filePath=file.getPath();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            file = new File(getFilesDir(), "editArtFirst"+Utils.getImageFormat(filePath1));
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
    private Bitmap comp(Bitmap response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        response.compress(format, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            response.compress(format, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
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
}
