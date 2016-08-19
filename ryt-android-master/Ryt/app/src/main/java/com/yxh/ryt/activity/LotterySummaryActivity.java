package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.LotteryTitlePageIndicatorAdapter;
import com.yxh.ryt.adapter.RZTitlePageIndicatorAdapter;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.LotterySummaryFragment;
import com.yxh.ryt.fragment.RZDetailFragment;
import com.yxh.ryt.fragment.RZInvestFragment;
import com.yxh.ryt.fragment.RZProjectFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LotterySummaryActivity extends BaseActivity implements View.OnClickListener {
    List<BaseFragment> rZFragments=new ArrayList<>();
    FragmentPagerAdapter rZAdapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financesummary);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        back.setOnClickListener(this);
        Intent intent = this.getIntent();
        String artWorkId = intent.getStringExtra("id");

        rZFragments.add(new LotterySummaryFragment(artWorkId));
        rZFragments.add(new RZDetailFragment(artWorkId));
        rZFragments.add(new RZInvestFragment(artWorkId));


        rZAdapter = new LotteryTitlePageIndicatorAdapter(this.getSupportFragmentManager(),rZFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(rZAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        final TabPageIndicator mindicator = (TabPageIndicator) findViewById(R.id.indicator);
        mindicator.setViewPager(pager);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            default:
                break;
        }
    }
}
