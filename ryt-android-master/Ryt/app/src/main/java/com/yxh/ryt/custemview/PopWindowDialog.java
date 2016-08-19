package com.yxh.ryt.custemview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yxh.ryt.R;


/**
 * 加载中Dialog
 *
 * @author lexyhp
 */
public class PopWindowDialog extends PopupWindow {

    /**
     * 构造方法
     *
     * @param context
     *            上下文
     * @param
     */
    private View mainView;
    private LinearLayout layoutDetail, layoutWithdraw;

    public PopWindowDialog(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwindowlayout, null);
        //分享布局
        layoutDetail = ((LinearLayout)mainView.findViewById(R.id.pwl_ll_detail));
        //复制布局
        layoutWithdraw = (LinearLayout)mainView.findViewById(R.id.pwl_ll_withdraw);
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            layoutDetail.setOnClickListener(paramOnClickListener);
            layoutWithdraw.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

}

