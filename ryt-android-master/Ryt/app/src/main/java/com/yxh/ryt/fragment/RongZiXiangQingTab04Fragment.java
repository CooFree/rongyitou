package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.CustomListview;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtworkInvest;
import com.yxh.ryt.vo.ArtworkInvestTop;

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
public class RongZiXiangQingTab04Fragment extends StickHeaderBaseFragment {
    private CustomListview mListview;
    private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
    private List<ArtworkInvest> investorDatas;
    private List<ArtworkInvestTop> investorTOpDatas;
    private int currentPage = 1;
    private View footer;
    private TextView loadFull;
    private TextView noData;
    private TextView more;
    private ProgressBar loading;
    private int lastItem;
    private boolean loadComplete = true;
    static StickHeaderViewPagerManager stickHeaderViewPagerManager;
    private CircleImageView icon1;
    private CircleImageView icon2;
    private CircleImageView icon3;
    private TextView title1;
    private TextView title2;
    private TextView title3;
    private TextView money1;
    private TextView money2;
    private TextView money3;
    private static String artworkId;

    public RongZiXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab04Fragment listFragment = new RongZiXiangQingTab04Fragment(manager, position);
        return listFragment;
    }

    public static RongZiXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh, String artworkID) {
        RongZiXiangQingTab04Fragment listFragment = new RongZiXiangQingTab04Fragment(manager, position, isCanPulltoRefresh);
        stickHeaderViewPagerManager = manager;
        artworkId = artworkID;
        return listFragment;
    }

    public RongZiXiangQingTab04Fragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        investorDatas = new ArrayList<>();
        investorTOpDatas = new ArrayList<>();
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_record, container,false);
        mListview = (CustomListview) view.findViewById(R.id.flr_scroll);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer_1, null);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        findView(view);
        setAdapter();
        onScroll();
        investorDatas.clear();
        investorTOpDatas.clear();
        LoadData(true, currentPage);
        return view;
    }

   /* private void topDatas() {
        if (investorTOpDatas.size() == 1) {
            AppApplication.displayImage(investorTOpDatas.get(0).getUser().getPictureUrl(), icon2);
            title2.setText(investorTOpDatas.get(0).getUser().getName());
            money2.setText(investorTOpDatas.get(0).getMoney() + "");
        }
        if (investorTOpDatas.size() == 2) {
            AppApplication.displayImage(investorTOpDatas.get(1).getUser().getPictureUrl(), icon1);
            AppApplication.displayImage(investorTOpDatas.get(0).getUser().getPictureUrl(), icon2);
            title1.setText(investorTOpDatas.get(1).getUser().getName());
            title2.setText(investorTOpDatas.get(0).getUser().getName());
            money1.setText(investorTOpDatas.get(1).getMoney() + "");
            money2.setText(investorTOpDatas.get(0).getMoney() + "");
        }
        if (investorTOpDatas.size() == 3) {
            AppApplication.displayImage(investorTOpDatas.get(1).getUser().getPictureUrl(), icon1);
            AppApplication.displayImage(investorTOpDatas.get(0).getUser().getPictureUrl(), icon2);
            AppApplication.displayImage(investorTOpDatas.get(2).getUser().getPictureUrl(), icon3);
            title1.setText(investorTOpDatas.get(1).getUser().getName());
            title2.setText(investorTOpDatas.get(0).getUser().getName());
            title3.setText(investorTOpDatas.get(2).getUser().getName());
            money1.setText(investorTOpDatas.get(1).getMoney() + "");
            money2.setText(investorTOpDatas.get(0).getMoney() + "");
            money3.setText(investorTOpDatas.get(2).getMoney() + "");
        }
    }*/

    private void findView(View view) {
        icon1 = ((CircleImageView) view.findViewById(R.id.flr_iv_icon1));
        icon2 = ((CircleImageView) view.findViewById(R.id.flr_iv_icon2));
        icon3 = ((CircleImageView) view.findViewById(R.id.flr_iv_icon3));
        title1 = ((TextView) view.findViewById(R.id.flr_tv_title1));
        title2 = ((TextView) view.findViewById(R.id.flr_tv_title2));
        title3 = ((TextView) view.findViewById(R.id.flr_tv_title3));
        money1 = ((TextView) view.findViewById(R.id.flr_tv_money1));
        money2 = ((TextView) view.findViewById(R.id.flr_tv_money2));
        money3 = ((TextView) view.findViewById(R.id.flr_tv_money3));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onScroll() {
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == investorRecordCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
                    more.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loadFull.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                    currentPage = currentPage + 1;
                    LoadData(false, currentPage);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("xxxxxxxxxxxxxxxx", "jksjflsjlfjslfjlsdf");
                lastItem = firstVisibleItem + visibleItemCount - 2;
            }
        });
    }

    private void setAdapter() {
        investorRecordCommonAdapter = new CommonAdapter<ArtworkInvest>(getActivity(), investorDatas, R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvest item) {
                if (item.getCreator() != null) {
                    helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
                    if (item.getCreator().getName() != null) {
                        if (item.getCreator().getName().length() > 3) {
                            helper.setText(R.id.iri_tv_nickname, item.getCreator().getName().substring(0, 3) + "...");
                        } else {
                            helper.setText(R.id.iri_tv_nickname, item.getCreator().getName());
                        }
                    }
                }
                helper.setText(R.id.iri_tv_content, item.getPrice() + "元");
                helper.setText(R.id.iri_tv_date, Utils.timeTransComment1(item.getCreateDatetime()));
            }
        };
        /*Utils.setListViewHeightBasedOnChildren(mListview);*/
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

    private void LoadData(final boolean flag, int pageNum) {
        more.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artworkId);
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
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    if (flag) {
                        List<ArtworkInvestTop> topList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestTopList")), new TypeToken<List<ArtworkInvestTop>>() {
                        }.getType());
                        if (topList == null || topList.size() == 0) {
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        } else {
                            investorTOpDatas.addAll(topList);
                            //topDatas();
                            topList.clear();
                        }
                        List<ArtworkInvest> investList1 = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList1 == null || investList1.size() == 0) {
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                            loadComplete = false;
                        } else if (investList1.size() < Constants.pageSize) {
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            investorDatas.addAll(investList1);
                            investList1.clear();
                            investorRecordCommonAdapter.notifyDataSetChanged();
                        } else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (investList1 != null) {
                            investorDatas.addAll(investList1);
                            investList1.clear();
                        }
                        investorRecordCommonAdapter.notifyDataSetChanged();
                    } else {
                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList == null || investList.size() < Constants.pageSize) {
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            loadComplete = false;
                        } else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (investList != null) {
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
