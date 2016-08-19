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
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.phote.util.Bimp;
import com.yxh.ryt.util.phote.util.ImageItem;
import com.yxh.ryt.util.phote.util.PublicWay;
import com.yxh.ryt.util.phote.util.Res;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/11.
 */
public class PublicDongtaiImageActivity extends  BaseActivity {
    public final int REQUEST_IMAGE=0;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    Map<String,File> fileMap=new HashMap<>();
    public static Bitmap bimap ;
    String artworkId="";
    @Bind(R.id.tv_done)
    TextView tvDone;
    @Bind(R.id.ev_shuoming)
    EditText evShuoming;
    //艺术家发布项目第一步接口一网络请求
    private void publicImageRequst() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId",artworkId);
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("content",evShuoming.getText().toString());
        paramsMap.put("type","0");
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        System.out.println(paramsMap.toString());
        NetRequestUtil.postFile(Constants.BASE_PATH + "releaseArtworkDynamic.do", "file", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(PublicDongtaiImageActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")) {
                    ToastUtil.show(PublicDongtaiImageActivity.this, "图片发布成功", Toast.LENGTH_SHORT);
                    PublicDongtaiImageActivity.this.finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                publicImageRequst();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    @OnClick(R.id.tv_done)
    public void doneClick(View v){
        publicImageRequst();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        setContentView(R.layout.public_dongtai_image);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
       artworkId = getIntent().getStringExtra("artWorkId");
                noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
                    // 是否显示调用相机拍照
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    if(Bimp.tempSelectBitmap.size()==0){
                        // 最大图片选择数量
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                    }else{
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9-Bimp.tempSelectBitmap.size());
                    }
                    // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    Intent intent = new Intent(PublicDongtaiImageActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        evShuoming.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == 9){
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
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
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Utils.setListViewHeightBasedOnChildren(noScrollgridview);
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
        }
        protected void onRestart() {
            adapter.update();
            super.onRestart();
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
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK&&Bimp.tempSelectBitmap.size()<9){
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                int i=0;
                for (String s:path){
                    i++;
                    Bitmap bitmap = BitmapFactory.decodeFile(s);
                    Bitmap bitmap1 = compressImage(bitmap, s);
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/dynamicImage"+i+Utils.getImageFormat(s));
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        file = new File(getFilesDir(), "dynamicImage"+i+Utils.getImageFormat(s));
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
                    fileMap.put(file.getName(),file);
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
            }
        }
        }

    }
