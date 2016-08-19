package com.yxh.ryt.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.yxh.ryt.activity.LoginActivity;

/**
 * Created by Administrator on 2016/5/23.
 */
public abstract class ShuoMClickableSpan extends ClickableSpan {
    String string;
    Context context;
    public ShuoMClickableSpan(String str,Context context){
        super();
        this.string = str;
        this.context = context;
    }


    @Override
    public void updateDrawState(TextPaint ds) {

        ds.setColor(Color.rgb(7, 99, 198));
    }
}
