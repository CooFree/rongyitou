package com.yxh.ryt.validations;

import android.content.Context;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationExecutor;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ContentValidation extends ValidationExecutor {
    @Override
    public boolean doValidate(Context context, String text) {
        if(text.isEmpty()){
            ToastUtil.showShort(AppApplication.getSingleContext(), "内容不能为空！");
            return false;
        }
        return true;
    }
}
