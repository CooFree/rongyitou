package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.IndexTabPageIndicatorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-4-4.
 */
@SuppressLint("ValidFragment")
public class TabFragment01 extends  BaseFragment {
    List<BaseFragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    /*private CustomViewPager pager;
    private   int currentIndex=0;
    @Bind({ R.id.tv_financ, R.id.tv_create, R.id.tv_auction })
    List<TextView> tabTvs;
    static final ButterKnife.Setter<View, Integer> SETCOLOR = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
            if(value==index){
                textView.setTextColor(Color.rgb(0,0,0));//可以将选择和未选择的color抽出
                return;
            }
            textView.setTextColor(Color.rgb(161,161,161));
        }
    };
    @Bind({ R.id.tv_bold_line1, R.id.tv_bold_line2, R.id.tv_bold_line3 })
    List<TextView> tabIvs;
    static final ButterKnife.Setter<View, Integer> SETIMAGE = new ButterKnife.Setter<View, Integer>() {

        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
                if(value==index){
                    switch (value){
                        case 0:
                            textView.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            textView.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            textView.setVisibility(View.VISIBLE);
                            break;
                    }
                    return;
                }
                switch (index){
                    case 0:
                        textView.setVisibility(View.GONE);
                        break;
                    case 1:
                        textView.setVisibility(View.GONE);
                        break;
                    case 2:
                        textView.setVisibility(View.GONE);
                        break;
                }
            return;
        }
    };*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        indexChildFragments.add(new FinanceFragment());
        indexChildFragments.add(new CreateFragment());
        indexChildFragments.add(new AuctionFragment());
    }

    public TabFragment01() {
    }

    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_01, container,false);
        indexChildAdapter = new IndexTabPageIndicatorAdapter(getActivity().getSupportFragmentManager(),indexChildFragments);
        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        final TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        /*ButterKnife.bind(this, view);
        indexChildAdapter = new TabPageAdapter(getActivity().getSupportFragmentManager(),indexChildFragments);
        pager = (CustomViewPager)view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setScanScroll(false);
        pager.setAdapter(indexChildAdapter);
        ButterKnife.apply(tabTvs, SETCOLOR, 0);
        ButterKnife.apply(tabIvs, SETIMAGE, 0);
        tabIvs.get(0).setVisibility(View.VISIBLE);
        tabIvs.get(1).setVisibility(View.GONE);
        tabIvs.get(2).setVisibility(View.GONE);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/
        return view;
    }
    /*@OnClick({R.id.rl_financ,R.id.rl_create,R.id.rl_auction})
    public void onClick(View v)
    {
//        fm = getSupportFragmentManager();
//        // 开启Fragment事务
//        transaction = fm.beginTransaction();
        switch (v.getId())
        {
            case R.id.rl_financ:
//                if (tabFragment01 == null)
//                {
//                    tabFragment01 = new TabFragment01();
//                }
//                // 使用当前Fragment的布局替代id_content的控件
//                transaction.replace(R.id.fl_tab, tabFragment01);
                pager.setCurrentItem(0,false);
                ButterKnife.apply(tabTvs, SETCOLOR, 0);
                ButterKnife.apply(tabIvs, SETIMAGE, 0);
                break;
            case R.id.rl_create:
//                if (tabFragment02 == null)
//                {
//                    tabFragment02 = new TabFragment02();
//                }
//                transaction.replace(R.id.fl_tab, tabFragment02);
                pager.setCurrentItem(1,false);
                ButterKnife.apply(tabTvs, SETCOLOR, 1);
                ButterKnife.apply(tabIvs, SETIMAGE, 1);
                break;
            case R.id.rl_auction:
//                if (tabFragment03 == null)
//                {
//                    tabFragment03 = new TabFragment03();
//                }
//                transaction.replace(R.id.fl_tab, tabFragment03);
                pager.setCurrentItem(2,false);
                ButterKnife.apply(tabTvs, SETCOLOR, 2);
                ButterKnife.apply(tabIvs, SETIMAGE, 2);

                break;
        }
        // transaction.addToBackStack();
        // 事务提交
//        transaction.commit();
    }*/

}
