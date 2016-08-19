package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements OnClickListener {

    private ViewPager mViewpager;
    private Button mRegist;
    private Button mLogin;
    private List<ImageView> mImageViews;
    private int[] mImageIds = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4};
    private TextView tv_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.vp_guide_viewpager);
        mRegist = (Button) findViewById(R.id.btn_guide_regist);
        mLogin = (Button) findViewById(R.id.btn_guide_login);
        tv_guide = (TextView) findViewById(R.id.tv_guide);
        mImageViews = new ArrayList<ImageView>();
        mImageViews.clear();
        //将图片放到Imageview中，在把imageview放到集合中，通过pageradapter显示出来
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            //将imageveiw添加到集合中
            mImageViews.add(imageView);
        }

        //设置图片给viewpager，类似listview
        mViewpager.setAdapter(new MypagerAdapter());
        //viewpager界面切换监听
        mViewpager.setOnPageChangeListener(onPageChangeListener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        //当界面切换完成调用的方法
        @Override
        public void onPageSelected(int position) {
            //当界面切换完成，判断如果是第三个界面显示button按钮，如果不是，隐藏按钮
            if (position == mImageIds.length - 1) {
                //显示button按钮
                mRegist.setVisibility(View.VISIBLE);
                mLogin.setVisibility(View.VISIBLE);
                tv_guide.setVisibility(View.VISIBLE);
                mRegist.setOnClickListener(GuideActivity.this);
                mLogin.setOnClickListener(GuideActivity.this);
                tv_guide.setOnClickListener(GuideActivity.this);
            } else {
                //隐藏按钮
                mRegist.setVisibility(View.INVISIBLE);
                mLogin.setVisibility(View.INVISIBLE);
                tv_guide.setVisibility(View.INVISIBLE);
                mRegist.setOnClickListener(null);
                mLogin.setOnClickListener(null);
                tv_guide.setOnClickListener(null);
            }
        }

        //当界面切换的时候调用的方法
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        //当界面切换状态改变的时候调用的方法
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class MypagerAdapter extends PagerAdapter {
        //设置viewpager条目的个数
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        //判断viewpager的页面的view对象是否和object一致
        //object : 是instantiateItem返回的object
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //给viewpager添加条目
        //position : 条目的位置
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //根据条目的位置获取出相应的imageview
            ImageView imageView = mImageViews.get(position);
            //将imageveiw添加给viewpager
            container.addView(imageView);
            //你添加什么view，返回的就是什么view
            return imageView;
        }

        //销毁条目
        //object : instantiateItem返回的数据
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guide_regist:

                //进行跳转到主页的操作
                startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
                //更改保存的是否是第一次进入的状态，状态是splashactivity中使用
                SPUtil.put(getApplicationContext(), Constants.ISFIRSTENTER, false);

                //销毁引导界面
                finish();
                break;

            case R.id.btn_guide_login:

                //进行跳转到主页的操作
                Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
                intent.putExtra("guide","guide");
                startActivity(intent);
                //更改保存的是否是第一次进入的状态，状态是splashactivity中使用
                SPUtil.put(getApplicationContext(), Constants.ISFIRSTENTER, false);

                //销毁引导界面
                finish();
                break;
            case R.id.tv_guide:

                //进行跳转到主页的操作
                startActivity(new Intent(GuideActivity.this,IndexActivity.class));
                //更改保存的是否是第一次进入的状态，状态是splashactivity中使用
                SPUtil.put(getApplicationContext(), Constants.ISFIRSTENTER, false);

                //销毁引导界面
                finish();
                break;

            default:
                break;
        }
    }

}