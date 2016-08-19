package com.yxh.ryt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.custemview.CustomGridView;
import com.yxh.ryt.util.DisplayUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.util.phote.util.Bimp02;
import com.yxh.ryt.util.phote.util.ImageItem;
import com.yxh.ryt.util.phote.util.PublicWay;
import com.yxh.ryt.util.phote.util.PublicWay02;
import com.yxh.ryt.util.phote.util.Res;
import com.yxh.ryt.validations.NickNameValidation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/22.
 */
public class CreateSuccessActivity extends BaseActivity implements View.OnClickListener {
    public static Bitmap bimap ;
    private GridView noScrollgridview_02;
    private GridAdapter02 adapter_02;
    Map<String,File> fileMap3=new HashMap<>();
    private int sum02=3;
    public final int REQUEST_IMAGE_02=2;
    private TextView commit;
    private String artworkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        PublicWay02.activityList.add(this);
        setContentView(R.layout.createsuccess);
        Res.init(this);
        noScrollgridview_02 = (GridView) findViewById(R.id.noScrollgridview_02);
        commit = (TextView) findViewById(R.id.btn_center_login);
        getIntent().getStringExtra("artWorkId");
        noScrollgridview_02.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter_02 = new GridAdapter02(this);
        adapter_02.update();
        noScrollgridview_02.setAdapter(adapter_02);
        noScrollgridview_02.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp02.tempSelectBitmap.size()) {
                    callMulImageSelector02(sum02, REQUEST_IMAGE_02);
                } else {
                    Intent intent = new Intent(CreateSuccessActivity.this,
                            Gallery02Activity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        artworkId = getIntent().getStringExtra("artworkId");
        commit.setOnClickListener(this);
    }
    private void callMulImageSelector02(int sum,int id) {
        PublicWay02.num=sum;
        Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        if(Bimp02.tempSelectBitmap.size()==0){
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum);
        }else{
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum-Bimp02.tempSelectBitmap.size());
        }
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, id);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Utils.setListViewHeightBasedOnChildren01(noScrollgridview_02);
                    adapter_02.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_02){
            if(resultCode == RESULT_OK&&Bimp02.tempSelectBitmap.size()<sum02){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                int i=0;
                for (String s:path){
                    i++;
                    Bitmap bitmap = BitmapFactory.decodeFile(s);
                    Bitmap bitmap1 = compressImage(bitmap, s);
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/createSuccess"+i+Utils.getImageFormat(s));
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        file = new File(getFilesDir(), "createSuccess"+i+Utils.getImageFormat(s));
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
                    fileMap3.put(file.getName(),file);
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp02.tempSelectBitmap.add(takePhoto);
                }
            }
        }
    }
    private Bitmap compressImage(Bitmap image,String s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format=Utils.getImageFormatBig(s);
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
    protected void onRestart() {
        super.onRestart();
        adapter_02.update();
    }

    @Override
    public void onClick(View v) {
        renZhengRequst();
    }
    private void renZhengRequst(){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId",artworkId);
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "artworkComplete.do", "file", fileMap3, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(CreateSuccessActivity.this,"上传成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                renZhengRequst();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    public class GridAdapter02 extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter02(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp02.tempSelectBitmap.size() == sum02){
                return sum02;
            }
            return (Bimp02.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida_01,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp02.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(90),DisplayUtil.dip2px(90));
                params.setMargins(DisplayUtil.dip2px(14),0,DisplayUtil.dip2px(14),0);
                holder.image.setLayoutParams(params);
                if (position == sum02) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                int widthzong = metric.widthPixels; // 屏幕宽度（像素）
                float es = (float) (widthzong-2*DisplayUtil.dip2px(20)) / (float)Bimp02.tempSelectBitmap.get(position).getBitmap().getWidth();
                int height = (int) (Bimp02.tempSelectBitmap.get(position).getBitmap().getHeight() * es);
                int width= (int) (Bimp02.tempSelectBitmap.get(position).getBitmap().getWidth()*es);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
                params.setMargins(DisplayUtil.dip2px(14),0,DisplayUtil.dip2px(14),0);
                holder.image.setLayoutParams(params);
                holder.image.setImageBitmap(Bimp02.tempSelectBitmap.get(position).getBitmap());
                /*holder.image.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.image.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;*/
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp02.max == Bimp02.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp02.max += 1;
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
}
