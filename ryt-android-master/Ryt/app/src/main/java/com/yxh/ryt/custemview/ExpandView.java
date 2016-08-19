package com.yxh.ryt.custemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.yxh.ryt.R;

/**
 * Created by Administrator on 2016/7/13.
 */
public class ExpandView extends FrameLayout {


    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand;
    private WebView webView;
    private int layoutId;
    public ExpandView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public ExpandView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        // TODO Auto-generated constructor stub
    }
    public ExpandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    public void setLayoutId(int layoutId){
        this.layoutId=layoutId;
        initExpandView();
    }
    private void initExpandView() {
        LayoutInflater.from(getContext()).inflate(layoutId, this, true);
        /*webView = (WebView) findViewById(R.id.wv_invest_process);
        webView.loadUrl("file:///android_asset/InvestFlowControlller.html");*/
        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.INVISIBLE);
            }
        });

    }
    public void collapse() {
        if (mIsExpand) {
            mIsExpand = false;
            clearAnimation();
            startAnimation(mCollapseAnimation);
        }
    }

    public void expand() {
        if (!mIsExpand) {
            mIsExpand = true;
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setContentView(){
        View view = null;
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_expand, null);

        removeAllViews();
        addView(view);
    }

}