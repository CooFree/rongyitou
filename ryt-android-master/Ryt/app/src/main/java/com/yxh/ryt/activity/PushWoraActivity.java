package com.yxh.ryt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.IcsLinearLayout;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.DatePicker.DatePickerView;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
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
 * Created by Administrator on 2016/5/11.
 */
public class PushWoraActivity extends BaseActivity {
    @Bind(R.id.pw_iv_work)
    ImageView imageWork;
    @Bind(R.id.pw_et_workName)
    EditText workName;
    @Bind(R.id.pw_et_material)
    EditText material;
    @Bind(R.id.pw_tv_state)
    TextView state;
    @Bind(R.id.pw_tv_year)
    TextView year;
    String filePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushwork);
        ButterKnife.bind(this);/*启用注解绑定*/
        workName.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        material.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    @OnClick(R.id.pw_ib_back)
    public void back(){
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        Uri uri=intent.getParcelableExtra("intent");
        if (uri !=null){
            Bitmap bitmap=getBitmap(uri);
            if (bitmap==null){
                return;
            }
            imageWork.setImageBitmap(bitmap);

        }

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
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
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
                + "/pushWork"+Utils.getImageFormat(filePath1));
        try {
            filePath=file.getPath();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            file = new File(getFilesDir(), "pushWork"+Utils.getImageFormat(filePath1));
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
    @OnClick({R.id.pw_sale,R.id.pw_rl_year,R.id.pw_tv_send} )
    public void isSale(View view){
        switch (view.getId()){
            case R.id.pw_sale:
                new ActionSheetDialog(this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("可售", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        state.setText("可售");
                                    }
                                })
                        .addSheetItem("已售", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        state.setText("已售");
                                    }
                                })
                        .addSheetItem("非卖品", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        state.setText("非卖品");
                                    }
                                })
                        .show();
                break;
            case R.id.pw_rl_year:
                DatePickerView pickerView=new DatePickerView(PushWoraActivity.this, new DatePickerView.DatePickerListener() {
                    @Override
                    public void dateChange(String string) {
                        year.setText(string);
                    }

                    @Override
                    public void finish(String string) {
                        year.setText(string);

                    }
                });
                pickerView.setFromYearAndToYear(1900,2016);
                pickerView.initDate(2016);
                pickerView.show();
                break;
            case R.id.pw_tv_send:
                if ("".equals(year.toString())){
                    ToastUtil.showLong(this,"创作年份不能为空");
                    return;
                }
                if ("".equals(state.toString())){
                    ToastUtil.showLong(this,"是否出售不能为空");
                    return;
                }
                if ("".equals(workName.getText().toString())){
                    ToastUtil.showLong(this,"作品名不能为空");
                    return;
                }
                if ("".equals(material.getText().toString())){
                    ToastUtil.showLong(this,"材质不能为空");
                    return;
                }
                push();
                break;
        }
    }

    private void push() {
        Map<String,File> fileMap=new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(),file);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("name",workName.getText().toString());
        paramsMap.put("material",material.getText().toString());
        String type="-1";
        if ("非卖品".equals(state.getText().toString())){
            type="0";
        }else if ("可售".equals(state.getText().toString())){
            type="1";
        }else if ("已售".equals(state.getText().toString())){
            type="2";
        }
        paramsMap.put("type", type);
        paramsMap.put("createYear",year.getText().toString());
        //paramsMap.put("currentUserId", AppApplication.gUser.getId());
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "saveMasterWork.do", "pictureUrl", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(PushWoraActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(PushWoraActivity.this,"发布作品成功了");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                push();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

}
