package com.yxh.ryt.custemview;


import com.yxh.ryt.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomDialogView extends View {  
  

        private final int[] ids = new int[] {R.mipmap. shuju1,
                    R.mipmap. shuju2, R.mipmap.shuju3, R.mipmap.shuju4 ,
                    R.mipmap. shuju5, R.mipmap.shuju6, R.mipmap.shuju7 ,
                    R.mipmap. shuju8,R.mipmap.shuju9, };
  
        private Bitmap[] loadingImgs ;  
  
        private Paint loadingImagePaint ;  
  
        private int currentIdsIndex = 0;  
  
        public CustomDialogView(Context context, AttributeSet attrs) {  
              super(context, attrs);  
             init();  
       }  
  
        public CustomDialogView(Context context) {  
              super(context);  
             init();  
       }  
  
        private void init() {  
               
              // ʵ��������  
              loadingImagePaint = new Paint();  
              // ���ÿ����  
              loadingImagePaint.setAntiAlias(true);  
  
              // һ���ԷŽ�����������  
              loadingImgs = new Bitmap[] {  
                           BitmapFactory. decodeResource(getResources(), ids[0]),  
                           BitmapFactory. decodeResource(getResources(), ids[1]),  
                           BitmapFactory. decodeResource(getResources(), ids[2]),  
                           BitmapFactory. decodeResource(getResources(), ids[3]),  
                           BitmapFactory. decodeResource(getResources(), ids[4]),  
                           BitmapFactory. decodeResource(getResources(), ids[5]),  
                           BitmapFactory. decodeResource(getResources(), ids[6]),  
                           BitmapFactory. decodeResource(getResources(), ids[7]),
                           BitmapFactory. decodeResource(getResources(), ids[8]),};
       }  
  
        @Override  
        protected void onDraw(Canvas canvas) {  
  
              // ѭ������ÿһ��ͼƬ�Ļ���˳���ÿ��������ǲ��Ŷ���  
              if (currentIdsIndex >= (ids .length - 1)) {  
                     currentIdsIndex = 0;  
             }  
  
             Bitmap currentLoadingBitmap = loadingImgs[currentIdsIndex ];  
              // ����ͼƬ����ʾ����Ļ����  
             canvas.drawBitmap(currentLoadingBitmap, (getWidth() - currentLoadingBitmap.getWidth())/2,  
                           (getHeight() - currentLoadingBitmap.getHeight())/2, loadingImagePaint );  
  
              currentIdsIndex++;  
  
              super.onDraw(canvas);  
       }  
  
}  