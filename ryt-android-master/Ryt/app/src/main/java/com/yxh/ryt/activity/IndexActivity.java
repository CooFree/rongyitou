package com.yxh.ryt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.TabPageAdapter;
import com.yxh.ryt.custemview.CustomViewPager;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.TabFragment01;
import com.yxh.ryt.fragment.TabFragment02;
import com.yxh.ryt.fragment.TabFragment03;
import com.yxh.ryt.fragment.TabFragment04;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndexActivity extends BaseActivity {
    List<BaseFragment> indexFragments=new ArrayList<>();
    FragmentPagerAdapter indexAdapter;
//    FragmentManager fm;
//    FragmentTransaction transaction;
//    TabFragment01 tabFragment01;
//    TabFragment02 tabFragment02;
//    TabFragment03 tabFragment03;
//    TabFragment04 tabFragment04;
    CustomViewPager indexPager;
    @Bind({ R.id.tv_tab_01, R.id.tv_tab_02, R.id.tv_tab_03 ,R.id.tv_tab_04})
    List<TextView> tabTvs;
    static final ButterKnife.Setter<View, Integer> SETCOLOR = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
            if(value==index){
                textView.setTextColor(Color.rgb(245,245,245));//可以将选择和未选择的color抽出
                return;
            }
            textView.setTextColor(Color.rgb(128,128,128));
        }
    };
    @Bind({ R.id.iv_tab_01, R.id.iv_tab_02, R.id.iv_tab_03 ,R.id.iv_tab_04})
    List<ImageView> tabIvs;
    static final ButterKnife.Setter<View, Integer> SETIMAGE = new ButterKnife.Setter<View, Integer>() {

        @Override public void set(View view, Integer value, int index) {
            ImageView imageView=(ImageView)view;
            if(value==index){
                switch (value){
                    case 0:
                        imageView.setImageResource(R.mipmap.shouyexuanzhong);
                        break;
                    case 1:
                        imageView.setImageResource(R.mipmap.paihangxuanzhong);
                        break;
                    case 2:
                        imageView.setImageResource(R.mipmap.xiaozixuanzhong);
                        break;
                    case 3:
                        imageView.setImageResource(R.mipmap.wodexuanzhong);
                        break;
                }
                return;
            }
            switch (index){
                case 0:
                    imageView.setImageResource(R.mipmap.shouyeweixuanzhong);
                    break;
                case 1:
                    imageView.setImageResource(R.mipmap.paihangweixuanzhong);
                    break;
                case 2:
                    imageView.setImageResource(R.mipmap.xiaoxiweixuanzhong);
                    break;
                case 3:
                    imageView.setImageResource(R.mipmap.wodeweixuanzhong);
                    break;
            }
            return;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        ButterKnife.bind(this);
        indexFragments.add(new TabFragment01());
        indexFragments.add(new TabFragment02());
        indexFragments.add(new TabFragment03());
        indexFragments.add(new TabFragment04());
        indexAdapter = new TabPageAdapter(this.getSupportFragmentManager(),indexFragments);
        indexPager= (CustomViewPager) findViewById(R.id.index_pager);
        indexPager.setOffscreenPageLimit(4);
        indexPager.setScanScroll(false);
        indexPager.setAdapter(indexAdapter);
        ButterKnife.apply(tabTvs, SETCOLOR, 0);
        ButterKnife.apply(tabIvs, SETIMAGE, 0);
        indexPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ButterKnife.apply(tabTvs, SETCOLOR, 0);
                        ButterKnife.apply(tabIvs, SETIMAGE, 0);
                        break;
                    case 1:
                        ButterKnife.apply(tabTvs, SETCOLOR, 1);
                        ButterKnife.apply(tabIvs, SETIMAGE, 1);
                        break;
                    case 2:
                        ButterKnife.apply(tabTvs, SETCOLOR, 2);
                        ButterKnife.apply(tabIvs, SETIMAGE, 2);
                        break;
                    case 3:
                        ButterKnife.apply(tabTvs, SETCOLOR, 3);
                        ButterKnife.apply(tabIvs, SETIMAGE, 3);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        // 设置默认的Fragment
//        setDefaultFragment();

    }

//    private void setDefaultFragment()
//    {
//        fm =getSupportFragmentManager();
//        transaction = fm.beginTransaction();
//        tabFragment01 = new TabFragment01();
//        transaction.replace(R.id.fl_tab, tabFragment01);
//        ButterKnife.apply(tabTvs, SETCOLOR, 0);
//        transaction.commit();
//    }

    @OnClick({R.id.rl_tab_01,R.id.rl_tab_02,R.id.rl_tab_03,R.id.rl_tab_04})
    public void onClick(View v)
    {
//        fm = getSupportFragmentManager();
//        // 开启Fragment事务
//        transaction = fm.beginTransaction();

        switch (v.getId())
        {
            case R.id.rl_tab_01:
//                if (tabFragment01 == null)
//                {
//                    tabFragment01 = new TabFragment01();
//                }
//                // 使用当前Fragment的布局替代id_content的控件
//                transaction.replace(R.id.fl_tab, tabFragment01);
                indexPager.setCurrentItem(0,false);
                ButterKnife.apply(tabTvs, SETCOLOR, 0);
                ButterKnife.apply(tabIvs, SETIMAGE, 0);
                break;
            case R.id.rl_tab_02:
//                if (tabFragment02 == null)
//                {
//                    tabFragment02 = new TabFragment02();
//                }
//                transaction.replace(R.id.fl_tab, tabFragment02);
                indexPager.setCurrentItem(1,false);
                ButterKnife.apply(tabTvs, SETCOLOR, 1);
                ButterKnife.apply(tabIvs, SETIMAGE, 1);
                break;
            case R.id.rl_tab_03:
//                if (tabFragment03 == null)
//                {
//                    tabFragment03 = new TabFragment03();
//                }
//                transaction.replace(R.id.fl_tab, tabFragment03);
                if ("".equals(AppApplication.gUser.getId())){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    indexPager.setCurrentItem(2,false);
                    ButterKnife.apply(tabTvs, SETCOLOR, 2);
                    ButterKnife.apply(tabIvs, SETIMAGE, 2);
                }
                break;
            case R.id.rl_tab_04:
//                if (tabFragment04 == null)
//                {
//                    tabFragment04 = new TabFragment04();
//                }
//                transaction.replace(R.id.fl_tab, tabFragment04);
                /*if ("".equals(AppApplication.gUser.getId())){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }else {*/
                    indexPager.setCurrentItem(3,false);
                    ButterKnife.apply(tabTvs, SETCOLOR, 3);
                    ButterKnife.apply(tabIvs, SETIMAGE, 3);
                /*}*/
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
//        transaction.commit();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if("com.yxh.ryt.gouser".equals(intent.getAction())){
            indexPager.setCurrentItem(4,false);
        }
    }
}
