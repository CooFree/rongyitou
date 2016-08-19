package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class RankingArtistFragment extends BaseFragment implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
    private AutoListView lstv;
    private CommonAdapter<Artist> artistCommonAdapter;
    private List<Artist> artistDatas;
    private int currentPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistDatas = new ArrayList<Artist>();

    }

    public RankingArtistFragment() {
    }

    private void LoadData(final int state, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "getArtistTopList.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (state == AutoListView.REFRESH) {
                    lstv.onRefreshComplete();
                    artistDatas.clear();
                    List<Artist> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("ArtistTopList")), new TypeToken<List<Artist>>() {
                    }.getType());
                    if (null == objectList || objectList.size() == 0) {
                        lstv.setResultSize(0);
                    }
                    if (null != objectList && objectList.size() > 0) {
                        lstv.setResultSize(objectList.size());
                        artistDatas.addAll(objectList);
                        artistCommonAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (state == AutoListView.LOAD) {
                    lstv.onLoadComplete();
                    List<Artist> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("ArtistTopList")), new TypeToken<List<Artist>>() {
                    }.getType());
                    if (null == objectList || objectList.size() == 0) {
                        lstv.setResultSize(1);
                    }
                    if (null != objectList && objectList.size() > 0) {
                        lstv.setResultSize(objectList.size());
                        artistDatas.addAll(objectList);
                        artistCommonAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.paihang_yishujia, container, false);
        lstv = (AutoListView) contextView.findViewById(R.id.lstv);
        lstv.setPageSize(Constants.pageSize);
        artistCommonAdapter = new CommonAdapter<Artist>(AppApplication.getSingleContext(), artistDatas, R.layout.fragment_ranking_artist) {
            @Override
            public void convert(ViewHolder helper, final Artist item) {
                if (helper.getPosition()==0){
                    helper.getView(R.id.fra_iv_medal).setVisibility(View.VISIBLE);
                    ((ImageView) helper.getView(R.id.fra_iv_medal)).setImageResource(R.mipmap.jinpai);
                    helper.getView(R.id.fra_tv_medal).setVisibility(View.GONE);
                }else if (helper.getPosition()==1){
                    helper.getView(R.id.fra_iv_medal).setVisibility(View.VISIBLE);
                    ((ImageView) helper.getView(R.id.fra_iv_medal)).setImageResource(R.mipmap.yinpai);
                    helper.getView(R.id.fra_tv_medal).setVisibility(View.GONE);
                }else if (helper.getPosition()==2){
                    helper.getView(R.id.fra_iv_medal).setVisibility(View.VISIBLE);
                    ((ImageView) helper.getView(R.id.fra_iv_medal)).setImageResource(R.mipmap.tongpai);
                    helper.getView(R.id.fra_tv_medal).setVisibility(View.GONE);
                }else {
                    helper.getView(R.id.fra_iv_medal).setVisibility(View.GONE);
                    helper.getView(R.id.fra_tv_medal).setVisibility(View.VISIBLE);
                    helper.setText(R.id.fra_tv_content,helper.getPosition()+1+"");
                }
                helper.setImageByUrl(R.id.fra_civ_headerImage,item.getPicture());
                helper.setText(R.id.fra_tv_name,item.getTruename());
                float f =Float.valueOf(item.getBidding_rate()*100);
                String s = String.format("%.2f", f);
                helper.setText(R.id.fra_tv_content,s+"%");
                if (f>100){
                    ((TextView) helper.getView(R.id.fra_tv_content)).setTextColor(Color.rgb(199, 31, 33));
                    ((ImageView) helper.getView(R.id.fra_iv_rate)).setImageResource(R.mipmap.paihang_jiantou_red);
                }else {
                    ((TextView) helper.getView(R.id.fra_tv_content)).setTextColor(Color.rgb(79, 182, 65));
                    ((ImageView) helper.getView(R.id.fra_iv_rate)).setImageResource(R.mipmap.paihang_jiantou_green);
                }
            }
        };
        lstv.setAdapter(artistCommonAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setOnItemClickListener(this);
        return contextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void lazyLoad() {
        if (artistDatas != null && artistDatas.size() > 0) return;
        LoadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        LoadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD, currentPage);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position>0 &&position<=artistDatas.size()){
            Intent intent=new Intent(getActivity(),ArtistIndexActivity.class);
            intent.putExtra("userId",artistDatas.get(position-1).getAuthor_id());
            intent.putExtra("name",artistDatas.get(position-1).getTruename());
            startActivity(intent);
        }
    }
}