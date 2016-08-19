package com.yxh.ryt.util;

import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;

/**
 * Created by YangZhenjie on 2016/8/9.
 */
public class AnimPraiseCancel {

    //点赞红心
    public static void animPraise(View view) {
        AnimationSet animationSet=(AnimationSet) AnimationUtils.loadAnimation(AppApplication.getSingleContext(), R.anim.animset);
        view.startAnimation(animationSet);
    }
    //取消灰心分裂
    public static void animCancelPraise(View left, View right) {
        AnimationSet animationSetLeft=(AnimationSet) AnimationUtils.loadAnimation(AppApplication.getSingleContext(), R.anim.animset_break_left);
        left.startAnimation(animationSetLeft);
        AnimationSet animationSetRight=(AnimationSet) AnimationUtils.loadAnimation(AppApplication.getSingleContext(), R.anim.animset_break_right);
        right.startAnimation(animationSetRight);
    }
}
