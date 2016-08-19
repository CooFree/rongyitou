package com.yxh.ryt.custemview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/21.
 */
public class ScaleHeightImageView extends ImageView {

    public ScaleHeightImageView(Context context) {
        super(context);
    }

    public ScaleHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (getWidth() > 0) {
            float es = (float) getHeight() / (float)bm.getHeight();
            int height = (int) (bm.getHeight() * es);
            int width= (int) (bm.getWidth()*es);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = height;
            params.width = width;
            setLayoutParams(params);
        }
    }

}
