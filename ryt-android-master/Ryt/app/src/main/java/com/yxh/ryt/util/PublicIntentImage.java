package com.yxh.ryt.util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/20.
 */
public class PublicIntentImage {
    public static List<Bitmap> bitmaps=new ArrayList<>();
    public static String[] actions=new String[9];
    public static List<Bitmap> getBitmap(){
        return bitmaps;
    }
    public static String[] getAction(){
        return actions;
    }
}
