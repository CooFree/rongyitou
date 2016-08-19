package com.yxh.ryt.activity;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.yxh.ryt.R;

import cn.jpush.android.api.JPushInterface;

public class ShowMsgActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg);
        TextView text = (TextView) findViewById(R.id.show_text);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("标题:")
                    .append(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE))
                    .append("\n内容:")
                    .append(bundle.getString(JPushInterface.EXTRA_ALERT));
            text.setText(builder.toString());

        }
    }
}
