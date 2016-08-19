package com.yxh.ryt.util;

import android.text.InputFilter;
import android.text.Spanned;

import com.yxh.ryt.AppApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/18.
 */
public class EditTextFilterUtil {
    public static InputFilter getEmojiFilter(){
        InputFilter emojiFilter = new InputFilter() {
            Pattern emoji = Pattern.compile(

                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",


                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart,


                                       int dend) {


                Matcher emojiMatcher = emoji.matcher(source);


                if (emojiMatcher.find()) {
                    ToastUtil.showShort(AppApplication.getSingleContext(),"格式不支持");

                    return "";


                }
                return null;


            }
        };
        return emojiFilter;
    }
}
