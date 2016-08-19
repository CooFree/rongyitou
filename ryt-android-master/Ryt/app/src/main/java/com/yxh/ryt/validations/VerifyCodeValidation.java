package com.yxh.ryt.validations;

import android.content.Context;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.util.avalidations.ValidationExecutor;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/3/31.
 */
public class VerifyCodeValidation extends ValidationExecutor {
    @Override
    public boolean doValidate(Context context, String text) {
        String regex = "^[0-9]{6}$";

        boolean result = Pattern.compile(regex).matcher(text).find();
        if (!result) {
            Toast.makeText(AppApplication.getSingleContext(), "验证码格式不正确！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
