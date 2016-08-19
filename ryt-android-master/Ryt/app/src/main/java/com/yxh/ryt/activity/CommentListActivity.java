package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ShuoMClickableSpan;
import com.yxh.ryt.vo.ArtworkComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CommentListActivity extends Activity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
    private AutoListView cListview;
    private String artWorkId;
    private int currentPage=1;
    private ImageButton back;
    private List<ArtworkComment> artCommentDatas;
    private CommonAdapter<ArtworkComment> artCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        cListview = (AutoListView) findViewById(R.id.cListview);
        back = (ImageButton) findViewById(R.id.ib_back);
        artWorkId = getIntent().getStringExtra("artWorkId");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cListview.setOnLoadListener(this);
        cListview.setOnRefreshListener(this);
        cListview.setOnItemClickListener(this);
        artCommentDatas = new ArrayList<>();
        loadData(AutoListView.REFRESH, currentPage);
        initData();
    }

    private void initData() {
        artCommentAdapter = new CommonAdapter<ArtworkComment>(this, artCommentDatas, R.layout.pdonclicktab_comment_item) {
            @Override
            public void convert(ViewHolder helper, final ArtworkComment item) {
                LinearLayout linearLayout = helper.getView(R.id.pdctci_ll_all);
                helper.getView(R.id.pdctci_tv_nickName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCreator().getMaster() != null) {
                            Intent intent = new Intent(CommentListActivity.this, ArtistIndexActivity.class);
                            intent.putExtra("userId", item.getCreator().getId());
                            intent.putExtra("name", item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(CommentListActivity.this, UserIndexActivity.class);
                            intent.putExtra("userId", item.getCreator().getId());
                            intent.putExtra("name", item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });

                if (item.getCreator() != null) {
                    helper.setText(R.id.pdctci_tv_nickName, item.getCreator().getName());
                    helper.setImageByUrl(R.id.pdctci_iv_icon, item.getCreator().getPictureUrl());
                    if ("1".equals(item.getCreator().getType())) {
                        helper.getView(R.id.iv_master).setVisibility(View.VISIBLE);
                    }else {
                        helper.getView(R.id.iv_master).setVisibility(View.INVISIBLE);
                    }
                }
                helper.setText(R.id.pdctci_tv_date, DateUtil.millionToNearly(item.getCreateDatetime()));
                if (item.getFatherComment() != null) {
                    TextView textView = helper.getView(R.id.pdctci_tv_content);
                    String fatherUser = item.getFatherComment().getCreator().getName();
                    SpannableString spanFatherUser = new SpannableString("@" + fatherUser);
                    ClickableSpan click = new ShuoMClickableSpan(fatherUser, AppApplication.getSingleContext()) {
                        @Override
                        public void onClick(View widget) {
                            if (item.getFatherComment().getCreator().getMaster() != null) {
                                Intent intent = new Intent(CommentListActivity.this, ArtistIndexActivity.class);
                                intent.putExtra("userId", item.getFatherComment().getCreator().getId());
                                intent.putExtra("name", item.getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(CommentListActivity.this, UserIndexActivity.class);
                                intent.putExtra("userId", item.getFatherComment().getCreator().getId());
                                intent.putExtra("name", item.getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    };
                    spanFatherUser.setSpan(click, 0, fatherUser.length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText("回复");
                    textView.append(spanFatherUser);
                    textView.append(":");
                    textView.append(item.getContent());
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    helper.setText(R.id.pdctci_tv_content, item.getContent());
                }
            }
        };
        cListview.setAdapter(artCommentAdapter);

    }

    private void loadData(final int state , final int pageNum) {
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
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<ArtworkComment>>() {
                        }.getType());

                    if (state == AutoListView.REFRESH) {
                        cListview.onRefreshComplete();
                        artCommentDatas.clear();
                        if (null == commentList || commentList.size() == 0) {
                            cListview.setResultSize(0);
                        }
                        if (null != commentList && commentList.size() > 0) {
                            cListview.setResultSize(commentList.size());
                            artCommentDatas.addAll(commentList);
                        }
                        artCommentAdapter.notifyDataSetChanged();

                    }
                    if (state == AutoListView.LOAD) {
                        cListview.onLoadComplete();
                        if (null == commentList || commentList.size() == 0) {
                            cListview.setResultSize(1);
                        }
                        if (null != commentList && commentList.size() > 0) {
                            cListview.setResultSize(commentList.size());
                            artCommentDatas.addAll(commentList);
                        }
                        artCommentAdapter.notifyDataSetChanged();
                    }
                    
                } else if ("000000".equals(response.get("resultCode"))) {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                loadData(state, pageNum);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
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
        //artCommentDatas.clear();
        loadData(AutoListView.REFRESH,currentPage);
    }

    @Override
    public void onLoad() {
        currentPage ++;
        loadData(AutoListView.LOAD,currentPage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AppApplication.getSingleContext(), ProjectCommentReply.class);
            if (artCommentDatas.get(position - 1).getCreator() != null) {
                intent.putExtra("name",artCommentDatas.get(position - 1).getCreator().getName());
            } else {
                intent.putExtra("name", "");
            }
            intent.putExtra("currentUserId", AppApplication.gUser.getId());
            intent.putExtra("fatherCommentId", artCommentDatas.get(position - 1).getId());
            intent.putExtra("artworkId", artWorkId);
            intent.putExtra("flag", 0);
            intent.putExtra("messageId", "");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!artCommentDatas.get(position - 1).getId().equals(AppApplication.gUser.getId())) {
                startActivity(intent);
            }

    }
}
