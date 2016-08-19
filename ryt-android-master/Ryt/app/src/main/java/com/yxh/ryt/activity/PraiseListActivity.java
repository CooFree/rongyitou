package com.yxh.ryt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtWorkPraiseList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/8/1.
 */
public class PraiseListActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {

    private ImageView back;
    private AutoListView lvPraise;
    private String artWorkId;
    private List<ArtWorkPraiseList> artWorkPraiseList;
    private List<ArtWorkPraiseList> praiseDatas;
    private CommonAdapter<ArtWorkPraiseList> praiseAdapter;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.praise_list);
        back = (ImageView) findViewById(R.id.iv_back);
        lvPraise = (AutoListView) findViewById(R.id.lv_praise);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadData(AutoListView.REFRESH, currentPage);
        praiseAdapter = new CommonAdapter<ArtWorkPraiseList>(this, praiseDatas, R.layout.praise_list_item) {
            @Override
            public void convert(ViewHolder helper, ArtWorkPraiseList item) {
                AppApplication.displayImage(item.getUser().getPictureUrl(), (ImageView) helper.getView(R.id.civ_head));
                helper.setText(R.id.tv_name, item.getUser().getName());
            }
        };
        lvPraise.setAdapter(praiseAdapter);
        lvPraise.setOnRefreshListener(this);
        lvPraise.setOnLoadListener(this);
    }

    private void loadData(final int state, int pageNum) {
        praiseDatas = new ArrayList<ArtWorkPraiseList>();
        artWorkId = getIntent().getStringExtra("artWorkId");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
        paramsMap.put("size", Constants.pageSize + "");
        paramsMap.put("index", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(paramsMap.toString() + "====");
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Object> object = (Map<String, Object>) response.get("object");
                if (response != null) {
                    artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                            toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraiseList>>() {
                    }.getType());
                }
                if (state == AutoListView.REFRESH) {
                    lvPraise.onRefreshComplete();
                    praiseDatas.clear();
                    if (null == artWorkPraiseList || artWorkPraiseList.size() == 0) {
                        lvPraise.setResultSize(0);
                    }
                    if (null != artWorkPraiseList && artWorkPraiseList.size() > 0) {
                        lvPraise.setResultSize(artWorkPraiseList.size());
                        praiseDatas.addAll(artWorkPraiseList);
                        praiseAdapter.notifyDataSetChanged();
                    }
                }
                if (state == AutoListView.LOAD) {
                    lvPraise.onLoadComplete();
                    if (null == artWorkPraiseList || artWorkPraiseList.size() == 0) {
                        lvPraise.setResultSize(1);
                    }
                    if (null != artWorkPraiseList && artWorkPraiseList.size() > 0) {
                        lvPraise.setResultSize(artWorkPraiseList.size());
                        praiseDatas.addAll(artWorkPraiseList);
                        praiseAdapter.notifyDataSetChanged();
                    }


                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        loadData(AutoListView.LOAD, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

}