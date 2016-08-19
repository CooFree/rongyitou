package com.yxh.ryt.obsever;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dev on 2015/12/24.
 */
public class Smsobserver extends ContentObserver {
    private Cursor cursor = null;
    private Activity activity;
    private SmsCallBack callBack;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public void getSmsFromPhone() {
        cursor = activity.managedQuery(Uri.parse("content://sms/inbox"),
                new String[] { "_id", "address", "read", "body" }, "address=? and read=?",
                new String[] {"10690736060397", "0" }, "_id desc");
        Log.d("---------",cursor.getCount()+"");
        // 按短信id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
        if (cursor != null && cursor.getCount() > 0) {
            int i=0;
            while (cursor.moveToNext()) {
                i++;
                if (i==cursor.getCount()) {
                    String smsbody = cursor
                            .getString(cursor.getColumnIndex("body"));
                    String regEx = "(?<![0-9])([0-9]{" + 6 + "})(?![0-9])";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(smsbody.toString());
                    while (m.find()) {
                        String smsContent = m.group();
                        System.out.println(smsContent+"================");
                        callBack.response(smsContent);
                    }
                    return;
                }
            }
        }
    }

    public Smsobserver(Handler handler, Activity activity,  SmsCallBack callBack) {
        super(handler);
        this.activity = activity;
        this.callBack = callBack;
    }
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        getSmsFromPhone();

    }
    public interface  SmsCallBack{
        void response(String code);
    }
}
