package com.yxh.ryt.util;

import android.webkit.JavascriptInterface;

public class JsInterface {
    /*interface for javascript to invokes*/
    public interface wvClientClickListener {
        public void wvHasClickEnvent();
    }

    private wvClientClickListener wvEnventPro = null;
    public void setWvClientClickListener(wvClientClickListener listener) {
        wvEnventPro = listener;
    }

    @JavascriptInterface  //这个注解很重要
    public void javaFunction() {
        if(wvEnventPro != null)
            wvEnventPro.wvHasClickEnvent();
    }
}  