package com.yxh.ryt.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;


import com.yxh.ryt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

public class CustomMsgActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_msg);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            TextView text = (TextView) findViewById(R.id.custom_text);
            try {
                JSONObject object = new JSONObject(string);
                StringBuilder builder = new StringBuilder();
                builder.append("id为")
                        .append(object.getString("id"))
                        .append("\n名字为")
                        .append(object.getString("name"));
                text.setText(builder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            SpannableString s = new SpannableString(msg);
            Pattern compile = Pattern.compile("笑");
            Matcher matcher = compile.matcher(msg);
            ImageSpan span = new ImageSpan(this, R.mipmap.ic_launcher);
            while (matcher.find()){
                s.setSpan(span, matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            text.setText(s);
        }
    }
}
