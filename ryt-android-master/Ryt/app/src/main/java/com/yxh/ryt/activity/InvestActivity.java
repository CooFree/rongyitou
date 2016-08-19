package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/3.
 */
public class InvestActivity extends BaseActivity implements TextWatcher {
    private String money;
    @Bind({ R.id.imp_ll_2, R.id.imp_ll_5, R.id.imp_ll_10 ,R.id.imp_ll_28,R.id.imp_ll_88,R.id.imp_ll_all})
    List<LinearLayout> tabTvs;
    ButterKnife.Setter<View, Integer> SETCOLOR = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            LinearLayout ll=(LinearLayout)view;
            if(value == index){
                ll.setBackgroundResource(R.mipmap.heise);
            }else {
                ll.setBackgroundResource(R.drawable.qianbaoweixuanzhong);
            }
        }
    };
    @Bind({ R.id.imp_tv_2, R.id.imp_tv_5, R.id.imp_tv_10 ,R.id.imp_tv_28,R.id.imp_tv_88,R.id.imp_tv_all})
    List<TextView> tabTvs1;
    static final ButterKnife.Setter<View, Integer> SETCOLOR1 = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
            if(value==index ){
                textView.setTextColor(Color.WHITE);//可以将选择和未选择的color抽出
            }else {
                textView.setTextColor(Color.BLACK);
            }
        }
    };
    @Bind(R.id.imp_tv_invest)
    TextView invest;
    @Bind(R.id.imp_et_other)
    EditText other;
    private int allMoney;
    private String artworkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investmentpage);
        ButterKnife.bind(this);
        allMoney = getIntent().getIntExtra("allMoney",0);
        artworkId = getIntent().getStringExtra("artWorkId");
        other.addTextChangedListener(this);
        other.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }
    @OnClick({R.id.imp_ll_2,R.id.imp_ll_5,R.id.imp_ll_10,R.id.imp_ll_28,R.id.imp_ll_88,R.id.imp_ll_all})
    public void click(View view){
        switch (view.getId()){
            case R.id.imp_ll_2:
                ButterKnife.apply(tabTvs, SETCOLOR,0);
                ButterKnife.apply(tabTvs1, SETCOLOR1,0);
                money=2+"";
                //other.setText(money);
                invest.setText("投资"+2+"元");
                break;
            case R.id.imp_ll_5:
                ButterKnife.apply(tabTvs, SETCOLOR,1);
                ButterKnife.apply(tabTvs1, SETCOLOR1,1);
                money=5+"";
                //other.setText(money);
                invest.setText("投资" + 5 + "元");
                break;
            case R.id.imp_ll_10:
                ButterKnife.apply(tabTvs, SETCOLOR,2);
                ButterKnife.apply(tabTvs1, SETCOLOR1,2);
                money=10+"";
                //other.setText(money);
                invest.setText("投资" + 10 + "元");
                break;
            case R.id.imp_ll_28:
                ButterKnife.apply(tabTvs, SETCOLOR,3);
                ButterKnife.apply(tabTvs1, SETCOLOR1,3);
                money=28+"";
                //other.setText(money);
                invest.setText("投资" + 28 + "元");
                break;
            case R.id.imp_ll_88:
                ButterKnife.apply(tabTvs, SETCOLOR,4);
                ButterKnife.apply(tabTvs1, SETCOLOR1,4);
                money=88+"";
                //other.setText(money);
                invest.setText("投资" + 88 + "元");
                break;
            case R.id.imp_ll_all:
                ButterKnife.apply(tabTvs, SETCOLOR, 5);
                ButterKnife.apply(tabTvs1, SETCOLOR1, 5);
                //other.setText(money);
                invest.setText("投资" + allMoney + "元");
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        for (int i = 0;i < 6;i++){
            tabTvs.get(i).setBackgroundResource(R.drawable.qianbaoweixuanzhong);
            tabTvs1.get(i).setTextColor(Color.BLACK);
        }
        if ("2".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,0);
            ButterKnife.apply(tabTvs1, SETCOLOR1,0);
        }
        if ("5".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,1);
            ButterKnife.apply(tabTvs1, SETCOLOR1,1);
        }
        if ("10".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,2);
            ButterKnife.apply(tabTvs1, SETCOLOR1,2);
        }
        if ("28".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,3);
            ButterKnife.apply(tabTvs1, SETCOLOR1,3);
        }
        if ("88".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,4);
            ButterKnife.apply(tabTvs1, SETCOLOR1,4);
        }
        if ((allMoney+"").equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,5);
            ButterKnife.apply(tabTvs1, SETCOLOR1,5);
        }
        money=s.toString();
        invest.setText("投资" + s + "元");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    @OnClick({R.id.imp_tv_invest,R.id.ip_ib_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ip_ib_back:
                finish();
                break;
            case R.id.imp_tv_invest:
                /*if (Integer.parseInt(other.getText().toString()) < 2) {
                    ToastUtil.showShort(getApplicationContext(),"投资最低2元");
                }else {*/
                if ("".equals(other.getText().toString())){
                    CustomDialog.Builder builder = new CustomDialog.Builder(InvestActivity.this);
                    builder.setTitle("确认要投资吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            investMoney();
                        }
                    });
                    builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    CustomDialog customDialog = builder.create();
                    customDialog.setCanceledOnTouchOutside(false);
                    // 设置点击屏幕Dialog不消失
                    customDialog.show();

                }else {
                    if (Integer.parseInt(other.getText().toString()) < 2) {
                        ToastUtil.showShort(getApplicationContext(),"投资最低2元");
                    }else {
                        CustomDialog.Builder builder = new CustomDialog.Builder(InvestActivity.this);
                        builder.setTitle("确认要投资吗");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                investMoney();
                            }
                        });
                        builder.setNegativeButton("取消",
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        CustomDialog customDialog = builder.create();
                        customDialog.setCanceledOnTouchOutside(false);
                        // 设置点击屏幕Dialog不消失
                        customDialog.show();
                    }
                }
                /*}*/
                break;
        }
    }

    private void investMoney() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("money", money);
        paramsMap.put("action", "investAccount");
        paramsMap.put("type", "1");
        paramsMap.put("artWorkId", artworkId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "pay/main.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(InvestActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                investMoney();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }else if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(InvestActivity.this,"投资成功!");
                    finish();
                }else if ("100015".equals(response.get("resultCode"))){
                    CustomDialog.Builder builder = new CustomDialog.Builder(InvestActivity.this);
                    builder.setTitle("余额不足,确认要充值吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Map<String,String> paramsMap=new HashMap<>();
                            //paramsMap.put("userId", AppApplication.gUser.getId());
                            paramsMap.put("money", money);
                            paramsMap.put("action", "invest");
                            paramsMap.put("type", "1");
                            paramsMap.put("artWorkId", artworkId);
                            paramsMap.put("timestamp", System.currentTimeMillis() + "");
                            try {
                                AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
                                paramsMap.put("signmsg", AppApplication.signmsg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            NetRequestUtil.post(Constants.BASE_PATH + "pay/main.do", paramsMap, new AttentionListCallBack() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    e.printStackTrace();
                                    System.out.println("失败了");
                                    ToastUtil.showLong(InvestActivity.this,"网络连接超时,稍后重试!");
                                }

                                @Override
                                public void onResponse(Map<String, Object> response) {
                                    if ("000000".equals(response.get("resultCode"))){
                                        SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                                            @Override
                                            public void getCode(String code) {
                                                if ("0".equals(code)){
                                                    investMoney();
                                                }
                                            }
                                        });
                                        sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                                    }else {
                                        String url = response.get("url").toString();
                                        Intent intent=new Intent(InvestActivity.this,PayPageActivity.class);
                                        intent.putExtra("url",url);
                                        InvestActivity.this.startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    CustomDialog customDialog = builder.create();
                    customDialog.setCanceledOnTouchOutside(false);
                    // 设置点击屏幕Dialog不消失
                    customDialog.show();
                }
            }
        });
    }
}
