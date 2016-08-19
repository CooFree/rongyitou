package com.yxh.ryt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.NotifaicationCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.PrivateLetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class PrivateLetterActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener{
    private CommonAdapter<PrivateLetter> plfAdapter;
    private List<PrivateLetter> privateLetterDatas;
    private int currentPage=1;
    @Bind(R.id.pl_message_listView)
    AutoListView plflistview;
    private PrivateLetterReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privateletter_listview);
        ButterKnife.bind(this);/*启用注解绑定*/
        privateLetterDatas=new ArrayList<PrivateLetter>();
        initView();
        plflistview.setPageSize(Constants.pageSize);
        receiver = new PrivateLetterReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.Server_BROADCAST");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        privateLetterDatas.clear();
        currentPage=1;
        LoadData(AutoListView.REFRESH, currentPage);
    }

    private void initView() {
        plfAdapter=new CommonAdapter<PrivateLetter>(AppApplication.getSingleContext(),privateLetterDatas,R.layout.privateletter_item) {
            @Override
            public void convert(ViewHolder helper, final PrivateLetter item) {
                if ("0".equals(item.getIsWatch())){
                    helper.setColor(R.id.pli_rl_all, Color.rgb(250, 250, 250));
                }
                if (item.getFromUser()!=null){
                    helper.setImageByUrl(R.id.pi_iv_icon, item.getFromUser().getPictureUrl());
                    helper.setText(R.id.pi_tv_name, item.getFromUser().getName());
                }
                helper.setText(R.id.pi_tv_content,item.getContent());
                if (item.getIsRead()==0){
                    helper.getView(R.id.pli_ll_count).setVisibility(View.GONE);
                }else if (item.getIsRead()>0){
                    helper.getView(R.id.pli_ll_count).setVisibility(View.VISIBLE);
                    helper.setText(R.id.pli_tv_count,item.getIsRead()+"");
                }else if (item.getIsRead()<0){
                    helper.getView(R.id.pli_ll_count).setVisibility(View.GONE);
                }
                if (item!=null){
                    helper.getView(R.id.pli_rl_all).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            click(item);
                        }
                    });
                }

            }
        };
        plflistview.setAdapter(plfAdapter);
        plflistview.setOnLoadListener(this);
        plflistview.setOnRefreshListener(this);
    }

    private void click(PrivateLetter item) {
        Intent intent=new Intent(this,MsgActivity.class);
        intent.putExtra("userId",AppApplication.gUser.getId());
        intent.putExtra("currentName",item.getTargetUser().getName());
        intent.putExtra("formId", item.getFromUser().getId());
        intent.putExtra("name", item.getFromUser().getName());
        startActivity(intent);
    }

    private void LoadData(final int state, final int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("type","2");
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum+"");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "information.do", paramsMap, new NotifaicationCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
                ToastUtil.showLong(PrivateLetterActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    if (state == AutoListView.REFRESH) {
                        plflistview.onRefreshComplete();
                        privateLetterDatas.clear();
                        List<PrivateLetter> notificationList = null;
                        try {
                            notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<PrivateLetter>>() {
                            }.getType());
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (null == notificationList || notificationList.size() == 0) {
                            plflistview.setResultSize(0);
                        }
                        if (null != notificationList && notificationList.size() > 0) {
                            plflistview.setResultSize(notificationList.size());
                            privateLetterDatas.addAll(notificationList);
                            plfAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (state == AutoListView.LOAD) {
                        plflistview.onLoadComplete();
                        List<PrivateLetter> notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<PrivateLetter>>() {
                        }.getType());
                        if (null == notificationList || notificationList.size() == 0) {
                            plflistview.setResultSize(1);
                        }
                        if (null != notificationList && notificationList.size() > 0) {
                            plflistview.setResultSize(notificationList.size());
                            privateLetterDatas.addAll(notificationList);
                            plfAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                LoadData(state,pageNum);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD,currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage=1;
        LoadData(AutoListView.REFRESH,currentPage);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class PrivateLetterReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            privateLetterDatas.clear();
            currentPage=1;
            LoadData(AutoListView.REFRESH, currentPage);
        }
    }
    @OnClick(R.id.pl_ib_back)
    public void back(){
        finish();
    }
}
