package com.yxh.ryt.activity;

import android.app.AlertDialog;
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

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.CustomDialogView;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.LoadingUtil;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
public class PublicProject02Activity extends  BaseActivity {
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
    @Bind(R.id.ev_zhizuo_shuoming)
    EditText evZhizuo;
    @Bind(R.id.ev_jiehuo)
    EditText evJieHuo;
    private LoadingUtil loadingUtil;

    //艺术家发布项目第一步接口一网络请求
    private void twoStepRequst() {
        /*if (!isImage){
            ToastUtil.showShort(PublicProject02Activity.this,"没有选择图片!");
            return;
        }*/
        if ("".equals(evShuoming.getText().toString())){
            ToastUtil.showShort(PublicProject02Activity.this,"项目说明不能为空!");
            return;
        }
        if ("".equals(evZhizuo.getText().toString())){
            ToastUtil.showShort(PublicProject02Activity.this,"制作说明不能为空!");
            return;
        }
        if ("".equals(evJieHuo.getText().toString())){
            ToastUtil.showShort(PublicProject02Activity.this,"融资解惑不能为空!");
            return;
        }

        loadingUtil = new LoadingUtil(PublicProject02Activity.this, PublicProject02Activity.this);
        loadingUtil.show();
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId",artworkId);
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("description",evShuoming.getText().toString());
        paramsMap.put("make_instru",evZhizuo.getText().toString());
        paramsMap.put("financing_aq",evJieHuo.getText().toString());
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        System.out.println(paramsMap.toString());
        NetRequestUtil.postFile(Constants.BASE_PATH + "initNewArtWork2.do", "file", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(PublicProject02Activity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    loadingUtil.dismiss();
                    ToastUtil.showLong(PublicProject02Activity.this,"项目发布成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                twoStepRequst();
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

    @OnClick(R.id.tv_done)
    public void doneClick(View v){
        twoStepRequst();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        setContentView(R.layout.public_project_02);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        artworkId = getIntent().getStringExtra("artworkId");
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
                    Intent intent = new Intent(PublicProject02Activity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        evShuoming.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evZhizuo.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        evJieHuo.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
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
        private static final int TAKE_PICTURE = 0x000001;

    public  void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            if(!file.isDirectory()){
                file.delete();
                file.mkdirs();
            }
        } catch (Exception e) {

        }
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
                    File file = null;
                    makeRootDirectory(Environment.getExternalStorageDirectory().getAbsoluteFile()
                            + File.separator+ File.separator );
                    file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                            + File.separator+ File.separator,"pushArtSecond"+i+Utils.getImageFormat(s));

                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        file = new File(getFilesDir(), "pushArtSecond"+i+Utils.getImageFormat(s));
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
    private Bitmap comp(Bitmap response,String s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format=Utils.getImageFormatBig(s);
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

}
