package com.yxh.ryt.custemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/5/20.
 */
public class CustomListview extends ListView {
    public CustomListview(Context context) {
        super(context);
    }

    public CustomListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
