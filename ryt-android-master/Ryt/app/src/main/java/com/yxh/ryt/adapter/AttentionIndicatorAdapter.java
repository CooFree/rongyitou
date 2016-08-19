package com.yxh.ryt.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.yxh.ryt.Constants;
import com.yxh.ryt.fragment.BaseFragment;

import java.util.List;

/**
 * Created by 吴洪杰 on 2016/4/5.
 */
public class AttentionIndicatorAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragments;
    public AttentionIndicatorAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("------------------------");
        //新建一个Fragment来展示ViewPager item的内容，并传递参数
        BaseFragment fragment = this.fragments.get(position);
        Bundle args = new Bundle();
        args.putString("arg", Constants.ATTENTION_TITLE[position]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.ATTENTION_TITLE[position % Constants.ATTENTION_TITLE.length];
    }

    @Override
    public int getCount() {
        return Constants.ATTENTION_TITLE.length;
    }
}
