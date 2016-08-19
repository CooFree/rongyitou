package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.viewpagerindicator.IcsLinearLayout;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.AttentionIndicatorAdapter;
import com.yxh.ryt.fragment.AttentionArtItemFragment;
import com.yxh.ryt.fragment.AttentionUserItemFragment;
import com.yxh.ryt.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AttentionActivity extends BaseActivity {
    List<BaseFragment> indexChildFragments = new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    @Bind(R.id.attention_pager)
    public ViewPager pager;
    @Bind(R.id.attention_indicator)
    TabPageIndicator indicator;
    private AttentionReceiver receiver;
    private String userId;
    private String otherUserId;
    private String flag;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AttentionActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        ButterKnife.bind(this);/*启用注解绑定*/
        userId = getIntent().getStringExtra("userId");
        otherUserId = getIntent().getStringExtra("otherUserId");
        flag = getIntent().getStringExtra("flag");
        indexChildFragments.add(new AttentionArtItemFragment(userId,otherUserId,flag));
        indexChildFragments.add(new AttentionUserItemFragment(userId,otherUserId,flag));
        indexChildAdapter = new AttentionIndicatorAdapter(getSupportFragmentManager(), indexChildFragments);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(pager);
        receiver = new AttentionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MY_BROADCAST");
        registerReceiver(receiver, filter);

    }



    @OnClick(R.id.attention_ib_top)
    public void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class AttentionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ((TextView) ((IcsLinearLayout) indicator.getChildAt(0)).getChildAt(0)).setText(Constants.ATTENTION_TITLE[0]);
            ((TextView) ((IcsLinearLayout) indicator.getChildAt(0)).getChildAt(1)).setText(Constants.ATTENTION_TITLE[1]);
        }
    }

}