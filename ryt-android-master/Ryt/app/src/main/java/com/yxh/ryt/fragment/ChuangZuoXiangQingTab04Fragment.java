package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtworkInvest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
@SuppressLint("ValidFragment")
public class ChuangZuoXiangQingTab04Fragment extends StickHeaderBaseFragment{
    private ListView mListview;
    private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
    private List<ArtworkInvest> investorDatas;
    private int currentPage=1;
    private View footer;
    private TextView loadFull;
    private TextView noData;
    private TextView more;
    private ProgressBar loading;
    private int lastItem;
    private boolean loadComplete=true;
    static StickHeaderViewPagerManager stickHeaderViewPagerManager;
    public ChuangZuoXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }
    public ChuangZuoXiangQingTab04Fragment(){}
    public ChuangZuoXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static ChuangZuoXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        ChuangZuoXiangQingTab04Fragment listFragment = new ChuangZuoXiangQingTab04Fragment(manager, position);
        return listFragment;
    }

    public static ChuangZuoXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        ChuangZuoXiangQingTab04Fragment listFragment = new ChuangZuoXiangQingTab04Fragment(manager, position, isCanPulltoRefresh);
        stickHeaderViewPagerManager=manager;
        return listFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        investorDatas=new ArrayList<>();
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container,false);
        mListview = (ListView)view.findViewById(R.id.v_scroll);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        setAdapter();
        onScroll();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadData(true, currentPage);
    }

    private void onScroll() {
        stickHeaderViewPagerManager.setOnListViewScrollListener(new StickHeaderViewPagerManager.OnListViewScrollListener() {
            @Override
            public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 2;
            }

            @Override
            public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem==investorRecordCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
                    more.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loadFull.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                    currentPage = currentPage + 1;
                    LoadData(false, currentPage);
                }
            }
        });
    }
    private void setAdapter() {
        investorRecordCommonAdapter=new CommonAdapter<ArtworkInvest>(getActivity(),investorDatas,R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvest item) {
                helper.setText(R.id.iri_tv_nickname,item.getCreator().getName());
                helper.setText(R.id.iri_tv_content,"投资了"+item.getPrice()+"元");
                helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
                helper.setText(R.id.iri_tv_date, Utils.timeTransComment(item.getCreateDatetime()));
            }
        };
        mListview.setAdapter(investorRecordCommonAdapter);
        mListview.addFooterView(footer);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        noData = (TextView) footer.findViewById(R.id.noData);
        more = (TextView) footer.findViewById(R.id.more);
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        more.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);

    }

    private void LoadData(final boolean flag,int pageNum) {
        more.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId","qydeyugqqiugd2");
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkInvest.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    Map<String,Object> object= (Map<String, Object>) response.get("object");
                    if (flag){
                        List<ArtworkInvest> topList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestTopList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (topList==null){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }else {
                            investorDatas.addAll(topList);
                            topList.clear();
                            investorRecordCommonAdapter.notifyDataSetChanged();
                        }
                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList ==null|| investList.size()<Constants.pageSize){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            loadComplete=false;
                        }else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (investList!=null){
                            investorDatas.addAll(investList);
                            investList.clear();
                        }

                        investorRecordCommonAdapter.notifyDataSetChanged();
                    }else {
                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList ==null|| investList.size()<Constants.pageSize){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            loadComplete=false;
                        }else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (investList!=null){
                            investorDatas.addAll(investList);
                            investList.clear();
                        }
                        investorRecordCommonAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    @Override
    protected void lazyLoad() {
    }

}
