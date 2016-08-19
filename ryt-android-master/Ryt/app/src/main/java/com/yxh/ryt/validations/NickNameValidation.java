package com.yxh.ryt.validations;

import android.content.Context;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationExecutor;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/3/31.
 */
public class NickNameValidation extends ValidationExecutor {

    @Override
    public boolean doValidate(Context context, String text) {

        String regex = "[\\u4e00-\\u9fa5_a-zA-Z0-9_]{1,8}";
        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            ToastUtil.showShort(AppApplication.getSingleContext(), "昵称格式不正确！");
            return false;
        }
        return true;
    }

}

