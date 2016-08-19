package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Bill;
import com.yxh.ryt.vo.Create;
import com.yxh.ryt.vo.UserMoney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/14.
 */
public class WalletDetailsActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    @Bind(R.id.wd_lstv)
    AutoListView listView;
    private CommonAdapter<Bill> walletDetailsCommonAdapter;
    private List<Bill> walletDetailsDatas;
    private int currentPage=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletdetails);
        ButterKnife.bind(this);
        walletDetailsDatas=new ArrayList<>();
        listView.setPageSize(Constants.pageSize);
        walletDetailsCommonAdapter=new CommonAdapter<Bill>(AppApplication.getSingleContext(),walletDetailsDatas,R.layout.item_wallet) {
            @Override
            public void convert(ViewHolder helper, final Bill item) {
                if (!"60".equals(item.getType())){
                    if (!"60".equals(item.getType())){
                        helper.setText(R.id.iw_tv_title,item.getTitle());
                        if ("0".equals(item.getOutOrIn())){
                            helper.setText(R.id.iw_tv_changeMoney,"-"+item.getMoney());
                        }else if ("1".equals(item.getOutOrIn())){
                            helper.setText(R.id.iw_tv_changeMoney,"+"+item.getMoney());
                        }
                        if (item.getCreateDatetime()!=null){
                            helper.setText(R.id.iw_tv_date, Utils.timeTrans(item.getCreateDatetime()));
                        }
                        helper.setText(R.id.iw_tv_allMoney,item.getRestMoney()+"");
                    }
                }
            }
        };
        listView.setAdapter(walletDetailsCommonAdapter);
        if(walletDetailsDatas!=null&&walletDetailsDatas.size()>0){
            return;
        }else {
            LoadData(AutoListView.REFRESH, currentPage);
        }
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
    }
    private void LoadData(final int state, final int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum+"");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "transactionRecord.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(WalletDetailsActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    UserMoney userMoney = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("object")), UserMoney.class);
                    if (state==AutoListView.REFRESH){
                        listView.onRefreshComplete();
                        walletDetailsDatas.clear();
                        List<Bill> objectList = userMoney.getBillList();
                        if(null==objectList||objectList.size()==0){
                            listView.setResultSize(0);
                        }
                        if (null!=objectList&&objectList.size()>0){
                            listView.setResultSize(objectList.size()); //还有数据加载。。。
                            walletDetailsDatas.addAll(objectList);
                            walletDetailsCommonAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (state==AutoListView.LOAD){
                        listView.onLoadComplete();
                        List<Bill> objectList = userMoney.getBillList();
                        if(null==objectList||objectList.size()==0){
                            listView.setResultSize(1);   //已全部加载完毕
                        }
                        if (null!=objectList&&objectList.size()>0) {
                            listView.setResultSize(objectList.size());  //还有数据加载。。。
                            walletDetailsDatas.addAll(objectList);
                            walletDetailsCommonAdapter.notifyDataSetChanged();
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
    public void onRefresh() {
        currentPage=1;
        LoadData(AutoListView.REFRESH,currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD,currentPage);
    }
    @OnClick(R.id.wd_ib_top)
    public void back(){
        finish();
    }
}
