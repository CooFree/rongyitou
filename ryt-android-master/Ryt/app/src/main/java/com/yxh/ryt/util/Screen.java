package com.yxh.ryt.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by YangZhenjie on 2016/7/28.
 */
public class Screen {

    private Screen(){

    }

    public static float getDensity(Activity active) {
        DisplayMetrics dm = active.getResources().getDisplayMetrics();
        return dm.density;
    }
    public static int getDensityDPI(Activity active) {
        DisplayMetrics dm = active.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }
    public static int getScreenWidth(Activity active) {
        DisplayMetrics dm = active.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    public static int getScreenHeight(Activity active) {
        DisplayMetrics dm = active.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


}