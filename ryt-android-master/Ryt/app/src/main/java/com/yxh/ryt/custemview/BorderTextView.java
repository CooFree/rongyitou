package com.yxh.ryt.custemview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BorderTextView extends TextView{
    public BorderTextView(Context context) {
        super(context);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private int sroke_width = 8;
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        //  将边框设为黑色
        paint.setStrokeWidth(sroke_width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(239,91,112));
        //  画TextView的4个边

        canvas.drawLine(0, 0, 15, 0, paint);
        canvas.drawLine(0, 0, 0, 15, paint);
        canvas.drawLine(this.getWidth() , this.getHeight() - 15, this.getWidth(), this.getHeight() , paint);
        canvas.drawLine(this.getWidth() - 15, this.getHeight() , this.getWidth(), this.getHeight(), paint);
        super.onDraw(canvas);
    }
}
