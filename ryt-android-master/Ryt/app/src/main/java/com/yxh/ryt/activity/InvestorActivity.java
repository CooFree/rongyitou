package com.yxh.ryt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtworkInvest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class InvestorActivity extends Activity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    private List<ArtworkInvest> investorDatas;
    private CommonAdapter<ArtworkInvest> investorAdapter;
    private AutoListView iListview;
    private String artWorkId;
    private int currentPage = 1;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor);
        iListview = (AutoListView) findViewById(R.id.iListview);
        back = (ImageButton) findViewById(R.id.ib_back);
        artWorkId = getIntent().getStringExtra("artWorkId");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        investorDatas = new ArrayList<>();
        loadData(AutoListView.REFRESH, currentPage);
        initData();
    }

    private void initData() {
        investorAdapter = new CommonAdapter<ArtworkInvest>(this, investorDatas, R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvest item) {
                if (item.getCreator() != null) {
                    helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
                    if (item.getCreator().getName() != null) {
                        helper.setText(R.id.iri_tv_nickname, item.getCreator().getName());
                    }
                }
                helper.getView(R.id.civ_top).setVisibility(View.GONE);
                helper.getView(R.id.cl_01_civ_pm).setVisibility(View.VISIBLE);
                helper.setText(R.id.cl_01_civ_pm, (helper.getPosition() + 1) + "");
                helper.setText(R.id.iri_tv_content, "￥" + item.getPrice());
                helper.setText(R.id.iri_tv_date, DateUtil.millionToNearly(item.getCreateDatetime()));
            }
        };
        iListview.setAdapter(investorAdapter);
        iListview.setOnLoadListener(this);
        iListview.setOnRefreshListener(this);
    }


    private void loadData(final int state, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkInvest.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {

                Map<String, Object> object = (Map<String, Object>) response.get("object");

                if (state == AutoListView.REFRESH) {
                    iListview.onRefreshComplete();
                    investorDatas.clear();
                    List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                            toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                    }.getType());
                    if (null == investList || investList.size() == 0) {
                        iListview.setResultSize(0);
                    }
                    if (null != investList && investList.size() > 0) {
                        iListview.setResultSize(investList.size());
                        investorDatas.addAll(investList);
                        investorAdapter.notifyDataSetChanged();
                    }
                }
                if (state == AutoListView.LOAD) {
                    iListview.onLoadComplete();
                    List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                            toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                    }.getType());
                    if (null == investList || investList.size() == 0) {
                        iListview.setResultSize(1);
                    }
                    if (null != investList && investList.size() > 0) {
                        iListview.setResultSize(investList.size());
                        investorDatas.addAll(investList);
                        investorAdapter.notifyDataSetChanged();
                    }

                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        loadData(AutoListView.LOAD, currentPage);
    }
}
