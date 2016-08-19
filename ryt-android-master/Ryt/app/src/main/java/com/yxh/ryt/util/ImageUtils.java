package com.yxh.ryt.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yxh.ryt.AppApplication;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by zhy on 15/8/11.
 */
public class ImageUtils
{
    public static ImageSize getImageSize(InputStream imageStream)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static class ImageSize
    {
        int width;
        int height;

        public ImageSize()
        {
        }

        public ImageSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString()
        {
            return "ImageSize{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize)
    {
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;

        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;

        if (width > reqWidth && height > reqHeight)
        {
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    public static int computeImageSampleSize(ImageSize srcSize, ImageSize targetSize, ImageView imageView)
    {
        final int srcWidth = srcSize.width;
        final int srcHeight = srcSize.height;
        final int targetWidth = targetSize.width;
        final int targetHeight = targetSize.height;

        int scale = 1;

        if (imageView == null)
        {
            scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
        } else
        {
            switch (imageView.getScaleType())
            {
                case FIT_CENTER:
                case FIT_XY:
                case FIT_START:
                case FIT_END:
                case CENTER_INSIDE:
                    scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
                    break;
                case CENTER:
                case CENTER_CROP:
                case MATRIX:
                    scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // min
                    break;
                default:
                    scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
                    break;
            }
        }

        if (scale < 1)
        {
            scale = 1;
        }

        return scale;
    }

    public static ImageSize getImageViewSize(View view)
    {

        ImageSize imageSize = new ImageSize();

        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);

        return imageSize;
    }

    private static int getExpectHeight(View view)
    {

        int height = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            height = view.getWidth(); // 鑾峰緱瀹為檯鐨勫搴�
        }
        if (height <= 0 && params != null)
        {
            height = params.height; // 鑾峰緱甯冨眬鏂囦欢涓殑澹版槑鐨勫搴�
        }

        if (height <= 0)
        {
            height = getImageViewFieldValue(view, "mMaxHeight");// 鑾峰緱璁剧疆鐨勬渶澶х殑瀹藉害
        }

        if (height <= 0)
        {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }

        return height;
    }

    private static int getExpectWidth(View view)
    {
        int width = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            width = view.getWidth(); // 鑾峰緱瀹為檯鐨勫搴�
        }
        if (width <= 0 && params != null)
        {
            width = params.width; // 鑾峰緱甯冨眬鏂囦欢涓殑澹版槑鐨勫搴�
        }

        if (width <= 0)

        {
            width = getImageViewFieldValue(view, "mMaxWidth");// 鑾峰緱璁剧疆鐨勬渶澶х殑瀹藉害
        }
        if (width <= 0)

        {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    private static int getImageViewFieldValue(Object object, String fieldName)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
            {
                value = fieldValue;
            }
        } catch (Exception e)
        {
        }
        return value;

    }
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static String getRealPathByUriOld(Uri data){
        String filename="";
        if (data.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = AppApplication.getSingleContext().getContentResolver().query(data,
                    new String[] {MediaStore.Audio.Media.DATA}, null, null, null);
            if (cursor.moveToFirst()) {
                filename = cursor.getString(0);
            }
        }else if (data.getScheme().toString().compareTo("file") == 0)         //file:///开头的uri
        {
            filename = data.toString();
            filename = data.toString().replace("file://", "");
            //替换file://
            if(!filename.startsWith("/mnt")){
                //加上"/mnt"头
                filename += "/mnt";
            }
        }
         return filename;
    }
}
