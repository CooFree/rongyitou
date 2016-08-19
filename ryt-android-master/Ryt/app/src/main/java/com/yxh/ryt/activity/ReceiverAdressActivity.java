package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ConsumerAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;

public class ReceiverAdressActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener, View.OnClickListener {

    private AutoListView adListview;
    private CommonAdapter<ConsumerAddress> cmAdapter;
    private List<ConsumerAddress> addressDatas;
    private int currentPage = 1;
    private String details;
    private String name;
    private String phone;
    private Button addAddress;
    private Button edit;
    private Button del;
    private int unDefult;
    private ImageView back;
    private LinearLayout ll_new_add;
    private LinearLayout ll_add;
    private Button btn_new_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_adress);
        adListview = (AutoListView) findViewById(R.id.pl_message_listView);
        ll_new_add = (LinearLayout) findViewById(R.id.ll_new_add);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        btn_new_add = (Button) findViewById(R.id.btn_new_add);
        addAddress = (Button) findViewById(R.id.btn_add);
        addressDatas = new ArrayList<ConsumerAddress>();

        back = (ImageView) findViewById(R.id.iv_back);
        addAddress.setOnClickListener(this);
        btn_new_add.setOnClickListener(this);
        initView();
        adListview.setPageSize(Constants.pageSize);
        back.setOnClickListener(this);
    }

    private void initView() {

        cmAdapter = new CommonAdapter<ConsumerAddress>(AppApplication.getSingleContext(), addressDatas, R.layout.address_item) {
            @Override
            public void convert(ViewHolder helper, final ConsumerAddress item) {
                if (addressDatas == null) {
                    ll_add.setVisibility(View.INVISIBLE);
                    ll_new_add.setVisibility(View.VISIBLE);
                    btn_new_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ReceiverAdressActivity.this, NewAddressActivity.class));
                        }
                    });
                } else if (addressDatas != null) {
                    ll_add.setVisibility(View.VISIBLE);
                    ll_new_add.setVisibility(View.INVISIBLE);
                    helper.getView(R.id.bt_edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent edIntent = new Intent(ReceiverAdressActivity.this, EditRecAddressActivity.class);
                            edIntent.putExtra("addressId", item.getId());
                            edIntent.putExtra("status", item.getStatus());
                            edIntent.putExtra("consignee", item.getConsignee());
                            edIntent.putExtra("details", item.getDetails());
                            edIntent.putExtra("phone", item.getPhone());
                            edIntent.putExtra("provinceStr", item.getProvinceStr());
                            edIntent.putExtra("districtStr", item.getDistrictStr());
                            startActivity(edIntent);
                        }
                    });
                    helper.getView(R.id.bt_del).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent delIntent = new Intent();
                           delIntent.putExtra("addressId", item.getId());
                            CustomDialog.Builder builder = new CustomDialog.Builder(ReceiverAdressActivity.this);
                            builder.setMessage("确认要删除该收货地址吗？");
                            builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    delAddress(delIntent,addressDatas);
                                }
                            });


                            builder.setNegativeButton("取消",
                                    new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            builder.create().show();
                        }
                    });

                    // delIntent.putExtra("status", item.getStatus());
                    helper.setText(R.id.tv_name, item.getConsignee());
                    helper.setText(R.id.tv_phone, item.getPhone());
                    helper.setText(R.id.tv_adress, item.getDetails());

                    unDefult = Integer.parseInt(item.getStatus());
                    if (unDefult == 2) {
                        helper.setImageResource(R.id.iv_selected, R.mipmap.yixuanze);

                    } else {
                        helper.setImageResource(R.id.iv_selected, R.mipmap.weixuanze);
                    }
                }
            }
        };
        adListview.setAdapter(cmAdapter);
        adListview.setOnLoadListener(this);
        adListview.setOnRefreshListener(this);
    }

    private void LoadData(final int state) {
        Map<String, String> paramsMap = new HashMap<>();
        //paramsMap.put("currentUserId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "listAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showLong(ReceiverAdressActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    if (state == AutoListView.REFRESH) {
                        adListview.onRefreshComplete();
                        addressDatas.clear();
                        List<ConsumerAddress> addressComment = null;
                        try {
                            addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {
                            }.getType());
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (null == addressComment || addressComment.size() == 0) {
                            adListview.setResultSize(0);
                        }
                        if (null != addressComment && addressComment.size() > 0) {
                            adListview.setResultSize(addressComment.size());
                            addressDatas.addAll(addressComment);
                        }
                        cmAdapter.notifyDataSetChanged();
                        return;
                    }
                    if (state == AutoListView.LOAD) {
                        adListview.onLoadComplete();
                        List<ConsumerAddress> addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {
                        }.getType());
                        if (null == addressComment || addressComment.size() == 0) {
                            adListview.setResultSize(1);
                        }
                        if (null != addressComment && addressComment.size() > 0) {
                            adListview.setResultSize(addressComment.size());
                            addressDatas.addAll(addressComment);
                        }
                        cmAdapter.notifyDataSetChanged();
                        return;
                    }else if ("000000".equals(response.get("resultCode"))){
                        SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                            @Override
                            public void getCode(String code) {
                                if ("0".equals(code)){
                                    LoadData(state);
                                }
                            }
                        });
                        sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                    }
                }

            }
        });
    }

    @Override
    public void onLoad() {
        //currentPage++;
        LoadData(AutoListView.LOAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData(AutoListView.REFRESH);
    }

    @Override
    public void onRefresh() {
        // currentPage = 1;
        LoadData(AutoListView.REFRESH);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }


    //点击
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                startActivity(new Intent(ReceiverAdressActivity.this, NewAddressActivity.class));
                break;
            case R.id.btn_new_add:
                startActivity(new Intent(ReceiverAdressActivity.this, NewAddressActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
    private void delAddress(Intent intent, final List list) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("addressId", intent.getStringExtra("addressId"));
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "removeAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showShort(getApplicationContext(), "删除失败");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                LoadData(AutoListView.REFRESH);
                if (list.size() == 1) {
                    ll_add.setVisibility(View.INVISIBLE);
                    ll_new_add.setVisibility(View.VISIBLE);

                } else if (addressDatas != null) {
                    ll_add.setVisibility(View.VISIBLE);
                    ll_new_add.setVisibility(View.INVISIBLE);
                }
                ToastUtil.showShort(getApplicationContext(), "删除成功");
            }
        });
    }
}
