package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.CommentCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ShuoMClickableSpan;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtworkCommentMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/9.
 */
public class CommentActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {
    private CommonAdapter<ArtworkCommentMsg> cmAdapter;
    private List<ArtworkCommentMsg> artworkCommentDatas;
    private int currentPage=1;
    @Bind(R.id.pl_message_listView)
    AutoListView cmlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_listview);
        ButterKnife.bind(this);/*启用注解绑定*/
        artworkCommentDatas =new ArrayList<ArtworkCommentMsg>();
        initView();
        cmlistview.setPageSize(Constants.pageSize);

    }

    private void initView() {
        cmAdapter=new CommonAdapter<ArtworkCommentMsg>(AppApplication.getSingleContext(), artworkCommentDatas,R.layout.comment_item) {
           // @Override
            public void convert(ViewHolder helper, final ArtworkCommentMsg item) {
                helper.getView(R.id.ci_ll_artwork).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent intent=null;
                            Log.d("XXXXXXXXXXXX",AppApplication.getStep(item.getArtwork().getStep())+"");
                            if (AppApplication.getStep(item.getArtwork().getStep())==1 || AppApplication.getStep(item.getArtwork().getStep())==3 ){
                                intent=new Intent(AppApplication.getSingleContext(), FinanceSummaryActivity.class);
                                intent.putExtra("id", item.getArtwork().getId());
                            }else if(AppApplication.getStep(item.getArtwork().getStep())==2){
                                intent=new Intent(AppApplication.getSingleContext(), CreateSummaryActivity.class);
                                intent.putExtra("id", item.getArtwork().getId());
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppApplication.getSingleContext().startActivity(intent);
                        }

                });
                helper.setImageByUrl(R.id.ci_iv_projectIcon,item.getArtwork().getPicture_url());
                helper.setText(R.id.ci_tv_otherNickname,item.getCreator().getName());
                helper.setText(R.id.ci_tv_date,Utils.timeTrans(item.getCreateDatetime()));
                helper.setImageByUrl(R.id.ci_iv_otherIcon, item.getCreator().getPictureUrl());
                helper.getView(R.id.ll_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCreator().getMaster()!=null){
                            Intent intent=new Intent(CommentActivity.this, ArtistIndexActivity.class);
                            intent.putExtra("userId",item.getCreator().getId());
                            intent.putExtra("name",item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else {
                            Intent intent=new Intent(CommentActivity.this, UserIndexActivity.class);
                            intent.putExtra("userId",item.getCreator().getId());
                            intent.putExtra("name",item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                helper.getView(R.id.ci_tv_reply).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AppApplication.getSingleContext(), ProjectCommentReply.class);
                        intent.putExtra("name", item.getCreator().getName());
                        intent.putExtra("fatherCommentId", item.getId());
                        intent.putExtra("currentUserId",AppApplication.gUser.getId());
                        intent.putExtra("artworkId", item.getArtwork().getId());
                        intent.putExtra("flag", 0);
                        intent.putExtra("messageId", "");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppApplication.getSingleContext().startActivity(intent);
                    }
                });
                if (item.getFatherArtworkCommentBean()!=null){
                    TextView textView=helper.getView(R.id.ci_tv_content);
                    String fatherUser = item.getFatherArtworkCommentBean().getCreator().getName();
                    SpannableString spanFatherUser = new SpannableString(fatherUser);
                    ClickableSpan click= new ShuoMClickableSpan(fatherUser, AppApplication.getSingleContext()) {
                        @Override
                        public void onClick(View widget) {
                            /*Intent intent=new Intent(AppApplication.getSingleContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppApplication.getSingleContext().startActivity(intent);*/
                            if ("".equals(AppApplication.gUser.getId())) {
                                LoginActivity.openActivity(CommentActivity.this);
                                return;
                            }
                            if (AppApplication.gUser != null&&"master".equals(AppApplication.gUser.getMaster1())) {
                                Intent intent=new Intent(AppApplication.getSingleContext(),ArtistIndexActivity.class);
                                intent.putExtra("userId", item.getFatherArtworkCommentBean().getCreator().getId());
                                intent.putExtra("name", item.getFatherArtworkCommentBean().getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else if (AppApplication.gUser != null&&"".equals(AppApplication.gUser.getMaster1())){
                                Intent intent=new Intent(AppApplication.getSingleContext(),UserIndexActivity.class);
                                intent.putExtra("userId", item.getFatherArtworkCommentBean().getCreator().getId());
                                intent.putExtra("name", item.getFatherArtworkCommentBean().getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    };
                    spanFatherUser.setSpan(click, 0, fatherUser.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText("回复");
                    textView.append(spanFatherUser);
                    textView.append(":");
                    textView.append(item.getContent());
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    TextView textView1=helper.getView(R.id.ci_tv_preName);
                    String fatherUser1 = "@"+item.getFatherArtworkCommentBean().getCreator().getName();
                    SpannableString spanFatherUser1 = new SpannableString(fatherUser1);
                    ClickableSpan click1= new ShuoMClickableSpan(fatherUser1, AppApplication.getSingleContext()) {
                        @Override
                        public void onClick(View widget) {
                            /*Intent intent=new Intent(AppApplication.getSingleContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppApplication.getSingleContext().startActivity(intent);*/
                            if ("".equals(AppApplication.gUser.getId())) {
                                LoginActivity.openActivity(CommentActivity.this);
                                return;
                            }
                            if (AppApplication.gUser != null&&"master".equals(AppApplication.gUser.getMaster1())) {
                                Intent intent=new Intent(AppApplication.getSingleContext(),ArtistIndexActivity.class);
                                intent.putExtra("name", item.getFatherArtworkCommentBean().getCreator().getId());
                                intent.putExtra("name", item.getFatherArtworkCommentBean().getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else if (AppApplication.gUser != null&&"".equals(AppApplication.gUser.getMaster1())){
                                Intent intent=new Intent(AppApplication.getSingleContext(),UserIndexActivity.class);
                                intent.putExtra("name", item.getFatherArtworkCommentBean().getCreator().getId());
                                intent.putExtra("name", item.getFatherArtworkCommentBean().getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    };
                    spanFatherUser1.setSpan(click1, 0, fatherUser1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView1.setText(spanFatherUser1);
                    textView1.setMovementMethod(LinkMovementMethod.getInstance());
                    helper.setText(R.id.ci_tv_preContent,":"+item.getFatherArtworkCommentBean().getContent());
                }else {
                    helper.setText(R.id.ci_tv_content, item.getContent());
                    helper.getView(R.id.ci_ll_pre).setVisibility(View.GONE);
                }
                helper.setText(R.id.ci_tv_projectTitle, item.getArtwork().getTitle());
                helper.setText(R.id.ci_tv_projectBrief, item.getArtwork().getBrief());
            }
        };
        cmlistview.setAdapter(cmAdapter);
        cmlistview.setOnLoadListener(this);
        cmlistview.setOnRefreshListener(this);
    }

    private void LoadData(final int state, final int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("type","1");
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum+"");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "information.do", paramsMap, new CommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
                ToastUtil.showLong(CommentActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    if (state == AutoListView.REFRESH) {
                        cmlistview.onRefreshComplete();
                        artworkCommentDatas.clear();
                        List<ArtworkCommentMsg> artworkComment = null;
                        try {
                            artworkComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkCommentMsg>>() {
                            }.getType());
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (null == artworkComment || artworkComment.size() == 0) {
                            cmlistview.setResultSize(0);
                        }
                        if (null != artworkComment && artworkComment.size() > 0) {
                            cmlistview.setResultSize(artworkComment.size());
                            artworkCommentDatas.addAll(artworkComment);
                            cmAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (state == AutoListView.LOAD) {
                        cmlistview.onLoadComplete();
                        List<ArtworkCommentMsg> artworkComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkCommentMsg>>() {
                        }.getType());
                        if (null == artworkComment || artworkComment.size() == 0) {
                            cmlistview.setResultSize(1);
                        }
                        if (null != artworkComment && artworkComment.size() > 0) {
                            cmlistview.setResultSize(artworkComment.size());
                            artworkCommentDatas.addAll(artworkComment);
                            cmAdapter.notifyDataSetChanged();
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
    protected void onResume() {
        super.onResume();
        currentPage=1;
        artworkCommentDatas.clear();
        LoadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage=1;
        LoadData(AutoListView.REFRESH,currentPage);
    }
    @OnClick(R.id.cl_iv_back)
    public void back(){
        finish();
    }
}
