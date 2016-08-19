package com.yxh.ryt.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Administrator on 2016/5/23.
 */
public  class ShowColorSpan extends ClickableSpan {
    String string;
    Context context;
    public ShowColorSpan(String str, Context context){
        super();
        this.string = str;
        this.context = context;
    }


    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {

        ds.setColor(Color.rgb(35, 35,35));
    }
}
