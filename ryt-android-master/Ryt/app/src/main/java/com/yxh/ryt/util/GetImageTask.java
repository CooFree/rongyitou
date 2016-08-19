package com.yxh.ryt.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.yxh.ryt.fragment.ChuangZuoXiangQingTab01Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/20.
 */
public class GetImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageCallBack callBack;
    private int location;
    public GetImageTask(ImageCallBack callBack,int location) {
        this.callBack = callBack;
        this.location=location;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        URL myFileUrl = null;
        Bitmap bitmap = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            myFileUrl = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(is != null){
                    is.close();
                }
                if( conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onCancelled() {

        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        callBack.getBitmapAndLc(result,location);
        super.onPostExecute(result);
    }
    public interface ImageCallBack{
        public void getBitmapAndLc(Bitmap result,int location);
    }
}

