package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ConsumerAddress;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/8/17.
 */
public class CommitDepositPriceActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llPay;
    private TextView tvPay;
    private ImageView ivPay;
    private ImageView add;
    private ImageView selected;
    private TextView depositPrice;
    private RelativeLayout address;
    private LinearLayout llAdd;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;
    private TextView auctionProtocol;
    private String artWorkId;
    private String userId;
    private String depositNum;
    private boolean agree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.auction_pay_pop);

        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        ivPay = (ImageView) findViewById(R.id.iv_pay);
        add = (ImageView) findViewById(R.id.iv_add);
        depositPrice = (TextView) findViewById(R.id.tv_price);
        auctionProtocol = (TextView) findViewById(R.id.tv_auction_protocol);
        selected = (ImageView) findViewById(R.id.iv_selected);
        address = (RelativeLayout) findViewById(R.id.rl_address);
        llAdd = (LinearLayout) findViewById(R.id.ll_add_address);
        userName = (TextView) findViewById(R.id.tv_name);
        userPhone = (TextView) findViewById(R.id.tv_phone);
        userAddress = (TextView) findViewById(R.id.tv_address);

        artWorkId = getIntent().getStringExtra("artWorkId");
        userId = getIntent().getStringExtra("userId");
        llPay.setOnClickListener(this);

        selected.setOnClickListener(this);
        auctionProtocol.setOnClickListener(this);

        initData(artWorkId);
        initAddress();
    }

    //获取并加载保证金金额
    private void initData(final String artWorkId) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.get(Constants.BASE_PATH + "artWorkAuctionView.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(CommitDepositPriceActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println(response);
                Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                        toJson(response.get("artwork")), Artwork.class);
                String isSubmitDepositPrice = (String) response.get("isSubmitDepositPrice");
                depositNum = artwork.getInvestGoalMoney().divide(new BigDecimal(10),0, BigDecimal.ROUND_UP).toString();
                depositPrice.setText("￥" + depositNum);

            }
        });
    }
    //加载收货地址
    private void initAddress() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "listAddress.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(CommitDepositPriceActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println(response);
                List<ConsumerAddress> addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(
                        response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {}.getType());
                if ( addressComment != null && addressComment.size() == 0) {
                    address.setVisibility(View.INVISIBLE);
                    llAdd.setVisibility(View.VISIBLE);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(),ReceiverAdressActivity.class));
                        }
                    });
                }else {
                    llAdd.setVisibility(View.INVISIBLE);
                    address.setVisibility(View.VISIBLE);
                    userName.setText(addressComment.get(0).getConsignee());
                    userPhone.setText(addressComment.get(0).getPhone());
                    userAddress.setText(addressComment.get(0).getDetails());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pay:
                investMoney();
                break;
            case R.id.tv_auction_protocol:
                startActivity(new Intent(this,AuctionProtocolActivity.class));
                break;
            case R.id.iv_selected:
                if (!agree){
                selected.setImageResource(R.mipmap.commit_money);
                    agree = true;
                    llPay.setEnabled(true);
                } else {
                    selected.setImageResource(R.mipmap.before);
                    agree = false;
                    llPay.setEnabled(false);
                }
                break;

        }
    }

    private void investMoney() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("money", depositNum);
        paramsMap.put("action", "investAccount");
        paramsMap.put("type", "1");
        paramsMap.put("artWorkId", artWorkId);
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
                ToastUtil.showLong(CommitDepositPriceActivity.this,"网络连接超时,稍后重试!");
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
                    ToastUtil.showLong(CommitDepositPriceActivity.this,"投资成功!");
                    finish();
                }else if ("100015".equals(response.get("resultCode"))){
                    CustomDialog.Builder builder = new CustomDialog.Builder(CommitDepositPriceActivity.this);
                    builder.setTitle("余额不足,确认要充值吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Map<String,String> paramsMap=new HashMap<>();
                            //paramsMap.put("userId", AppApplication.gUser.getId());
                            paramsMap.put("money", depositNum);
                            paramsMap.put("action", "invest");
                            paramsMap.put("type", "1");
                            paramsMap.put("artWorkId", artWorkId);
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
                                    ToastUtil.showLong(CommitDepositPriceActivity.this,"网络连接超时,稍后重试!");
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
                                        Intent intent=new Intent(CommitDepositPriceActivity.this,PayPageActivity.class);
                                        intent.putExtra("url",url);
                                        CommitDepositPriceActivity.this.startActivity(intent);
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
