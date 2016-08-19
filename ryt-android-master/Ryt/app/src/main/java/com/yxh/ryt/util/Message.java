package com.yxh.ryt.util;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Message {
    private String mMsg;
    public Message(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = "MainEvent:"+msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
