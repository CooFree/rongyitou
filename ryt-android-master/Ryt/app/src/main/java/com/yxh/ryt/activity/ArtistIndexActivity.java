package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.ArtistTabPageIndicatorAdapter;
import com.yxh.ryt.fragment.ArtistHomeFragment;
import com.yxh.ryt.fragment.BriefFragment;
import com.yxh.ryt.fragment.InvestedFragment;
import com.yxh.ryt.fragment.UserPraiseFragment;
import com.yxh.ryt.fragment.WorksFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class ArtistIndexActivity extends BaseActivity {
    List<Fragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    private TextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistindex);
        edit = ((TextView) findViewById(R.id.aai_top_edit));
        final String userId = getIntent().getStringExtra("userId");
        String name=getIntent().getStringExtra("name");
        TextView textName = (TextView) findViewById(R.id.aai_tv_name);
        ((ImageView) findViewById(R.id.aai_top_lf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textName.setText(name);
        if (AppApplication.gUser.getId().equals(userId)){
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ArtistIndexActivity.this,UserYsjIndexActivity.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
            });
        }else {
            edit.setVisibility(View.GONE);
        }
        indexChildFragments.add(new ArtistHomeFragment(userId));
        indexChildFragments.add(new BriefFragment(userId));
        indexChildFragments.add(new WorksFragment(userId));
        indexChildFragments.add(new InvestedFragment(userId));
        indexChildFragments.add(new UserPraiseFragment(userId));
        indexChildAdapter = new ArtistTabPageIndicatorAdapter(getSupportFragmentManager(),indexChildFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.aai_pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.aai_indicator);
        indicator.setViewPager(pager);
    }

}
