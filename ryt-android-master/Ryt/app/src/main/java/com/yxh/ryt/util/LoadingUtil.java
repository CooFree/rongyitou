package com.yxh.ryt.util;


import android.app.Activity;
import android.content.Context;

import com.yxh.ryt.R;
import com.yxh.ryt.custemview.LoadingDialog;

/**
 * Created by Administrator on 2016/7/21.
 */
public class LoadingUtil {
    private Context context;
    private Activity active;
    private int screenWidth;
    private int screenHeight;
    private  LoadingDialog dialog;


    public LoadingUtil(Activity active, Context context) {
        this.active = active;
        this.context = context;

        // 屏幕宽度（像素）
        screenWidth = Screen.getScreenWidth(active);
        screenHeight = Screen.getScreenHeight(active);
        dialog = new LoadingDialog(context, R.style.dialog);
        //dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (screenHeight * 0.08);   //高度设置为屏幕的0.3
        p.width = (int) (screenWidth * 0.20);    //宽度设置为屏幕的0.load5
        dialog.getWindow().setAttributes(p);     //设置生效
    }
    public void show(){
        dialog.show();
    }public void dismiss(){
        dialog.dismiss();
    }


}
