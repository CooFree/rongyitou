package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.activity.UserPtIndexActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ShuoMClickableSpan;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtworkComment;

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
public class RongZiXiangQingTab03Fragment extends StickHeaderBaseFragment {
    private static String artWorkId;
    private ListView mListview;
    private CommonAdapter<ArtworkComment> artCommentAdapter;
    private List<ArtworkComment> artCommentDatas;
    private int currentPage=1;
    private View footer;
    private TextView loadFull;
    private TextView noData;
    private TextView more;
    private ProgressBar loading;
    private int lastItem;
    private boolean loadComplete=true;
    static StickHeaderViewPagerManager stickHeaderViewPagerManager;
    public RongZiXiangQingTab03Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab03Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab03Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab03Fragment listFragment = new RongZiXiangQingTab03Fragment(manager, position);
        return listFragment;
    }

    public static RongZiXiangQingTab03Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh,String id) {
        RongZiXiangQingTab03Fragment listFragment = new RongZiXiangQingTab03Fragment(manager, position, isCanPulltoRefresh);
        artWorkId=id;
        stickHeaderViewPagerManager=manager;
        return listFragment;
    }
    public RongZiXiangQingTab03Fragment(){
        super();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artCommentDatas=new ArrayList<>();
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
        artCommentDatas.clear();
        currentPage=1;
        LoadData(true, currentPage);
    }

    private void setAdapter() {
        artCommentAdapter=new CommonAdapter<ArtworkComment>(getActivity(),artCommentDatas,R.layout.pdonclicktab_comment_item) {
            @Override
            public void convert(ViewHolder helper, final ArtworkComment item) {
                LinearLayout linearLayout=helper.getView(R.id.pdctci_ll_all);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AppApplication.getSingleContext(), ProjectCommentReply.class);
                        if (item.getCreator()!=null){
                            intent.putExtra("name", item.getCreator().getName());
                        }else {
                            intent.putExtra("name", "");
                        }
                        intent.putExtra("currentUserId", AppApplication.gUser.getId());
                        intent.putExtra("fatherCommentId", item.getId());
                        intent.putExtra("artworkId", artWorkId);
                        intent.putExtra("flag", 0);
                        intent.putExtra("messageId", "");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (!item.getId().equals( AppApplication.gUser.getId())) {
                            AppApplication.getSingleContext().startActivity(intent);
                        }
                    }
                });
                helper.getView(R.id.pdctci_tv_nickName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCreator().getMaster()!=null){
                            Intent intent=new Intent(getActivity(), ArtistIndexActivity.class);
                            intent.putExtra("userId",item.getCreator().getId());
                            intent.putExtra("name",item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), UserIndexActivity.class);
                            intent.putExtra("userId",item.getCreator().getId());
                            intent.putExtra("name",item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }
                    }
                });
                if (item.getCreator()!=null){
                    helper.setText(R.id.pdctci_tv_nickName, item.getCreator().getName());
                    helper.setImageByUrl(R.id.pdctci_iv_icon, item.getCreator().getPictureUrl());
                }
                helper.setText(R.id.pdctci_tv_date, Utils.timeTransComment1(item.getCreateDatetime()));
                if (item.getFatherComment()!=null){
                    TextView textView=helper.getView(R.id.pdctci_tv_content);
                    String fatherUser = item.getFatherComment().getCreator().getName();
                    SpannableString spanFatherUser = new SpannableString(fatherUser);
                    ClickableSpan click= new ShuoMClickableSpan(fatherUser, AppApplication.getSingleContext()) {
                        @Override
                        public void onClick(View widget) {
                            if (item.getFatherComment().getCreator().getMaster()!=null){
                                Intent intent=new Intent(getActivity(), ArtistIndexActivity.class);
                                intent.putExtra("userId",item.getFatherComment().getCreator().getId());
                                intent.putExtra("name",item.getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }else {
                                Intent intent=new Intent(getActivity(), UserIndexActivity.class);
                                intent.putExtra("userId",item.getFatherComment().getCreator().getId());
                                intent.putExtra("name",item.getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }
                        }
                    };
                    spanFatherUser.setSpan(click, 0, fatherUser.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText("回复");
                    textView.append(spanFatherUser);
                    textView.append(":");
                    textView.append(item.getContent());
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                }else {
                    helper.setText(R.id.pdctci_tv_content,item.getContent());
                }
            }
        };
        mListview.setAdapter(artCommentAdapter);
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
    private void onScroll() {
        stickHeaderViewPagerManager.setOnListViewScrollListener(new StickHeaderViewPagerManager.OnListViewScrollListener() {
            @Override
            public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 2;
                Log.d("XXXXXXXXXXXXXX","YYYYYYYYYYYYYY");
            }

            @Override
            public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == artCommentAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
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
    private void LoadData(final boolean flag,int pageNum) {
        more.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId",artWorkId);
        paramsMap.put("pageSize", Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkComment.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("444444失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    if (flag) {
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<ArtworkComment>>() {
                        }.getType());
                        if (commentList == null || commentList.size()==0) {
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        } else if (commentList.size() < Constants.pageSize){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            artCommentDatas.addAll(commentList);
                            commentList.clear();
                            artCommentAdapter.notifyDataSetChanged();
                        }else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (commentList != null) {
                            artCommentDatas.addAll(commentList);
                            commentList.clear();
                        }
                        artCommentAdapter.notifyDataSetChanged();
                    }else {
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<ArtworkComment>>() {
                        }.getType());
                        if (commentList == null || commentList.size() < Constants.pageSize) {
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                        } else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (commentList != null) {
                            artCommentDatas.addAll(commentList);
                            commentList.clear();
                        }
                        artCommentAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }
    @Override
    protected void lazyLoad() {

    }
}
