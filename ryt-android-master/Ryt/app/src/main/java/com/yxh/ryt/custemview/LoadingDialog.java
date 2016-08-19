package com.yxh.ryt.custemview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.yxh.ryt.R;


/**
 * 加载中Dialog
 *
 * @author lexyhp
 */
public class LoadingDialog extends AlertDialog {

    /**
     * 构造方法
     *
     * @param context
     *            上下文
     * @param
     */
    public LoadingDialog(Context context, int theme) {
        super(context,theme);
        //this.layoutResId = layoutResId;
        //message = context.getResources().getString(R.string.loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_tips_loading2);
		/*tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
		tips_loading_msg.setText(this.message);*/
    }


}

