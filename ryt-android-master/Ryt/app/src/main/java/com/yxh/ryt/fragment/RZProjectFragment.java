package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.AttentionActivity;
import com.yxh.ryt.activity.CommentListActivity;
import com.yxh.ryt.activity.InvestActivity;
import com.yxh.ryt.activity.InvestorActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.PraiseListActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.ExpandView;
import com.yxh.ryt.custemview.HorizontalListView;
import com.yxh.ryt.custemview.ListViewForScrollView;
import com.yxh.ryt.custemview.RoundProgressBar;
import com.yxh.ryt.util.AnimPraiseCancel;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.LoadingUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ShuoMClickableSpan;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.ContentValidation;
import com.yxh.ryt.vo.ArtWorkPraiseList;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkInvest;
import com.yxh.ryt.vo.ArtworkInvestTop;
import com.yxh.ryt.vo.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class RZProjectFragment extends BaseFragment implements View.OnClickListener {

    private String artWorkId;
    private ListViewForScrollView mListview;
    private ListViewForScrollView iListview;
    private ListViewForScrollView iTopListview;
    private CommonAdapter<ArtworkComment> artCommentAdapter;
    private List<ArtworkComment> artCommentDatas;
    private int currentPage = 1;
    private List<User> users;
    private List<ArtWorkPraiseList> artWorkPraiseList;
    private ImageView imageTitle;
    private ImageView leftPraise;
    private ImageView rightPraise;
    private ImageView redPraise;
    private boolean flag1 = true;
    private boolean isPraise1;
    //private LinearLayout ll_invester;
    private TextView tv_project_name;
    private TextView tv_project_brief;
    private TextView tv_price;
    private TextView tv_name;
    private TextView tv_name2;
    private TextView praiseNum;
    private TextView deadline;
    private TextView tvInvestor;
    private CircleImageView cl_headPortrait;
    private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
    private CommonAdapter<ArtworkInvestTop> investorTopAdapter;
    private List<ArtworkInvest> investorDatas;
    private LinearLayout llpraise;
    private LinearLayout llinvester;
    private ScrollView sv;
    protected static final int PRAISE_SUC = 100;
    protected static final int PRAISE_CANCEL = 101;
    protected static final int COUNT_DOWN = 102;
    private int widthScreen;
    private int widthImage;
    private int widthView;
    private int heightIamge;
    private int right;
    private int count;
    private int time = 0;
    private int padding;
    private ImageButton go;
    private RoundProgressBar mRoundProgressBar;
    private int progress = 0;
    private int max = 0;
    private int screenWidth;
    private int screenHeight;
    private Rect rect;
    private double progressCurrent;
    private ImageView headV;
    private ImageView iv_show;
    private WebView webView;
    private boolean isShow;
    private Button comment;
    private EditText etComment;
    private ImageButton attention;
    private LinearLayout llprogress;
    private ExpandView mExpandView;
    private LinearLayout ll_project;
    private RelativeLayout rl_progress;
    private TextView tvWorksNum;
    private TextView tvFansNum;
    private Artwork artwork;
    private TextView reader;
    private LinearLayout invest;
    private int remainMoney;
    private List<ArtworkInvestTop> investorTopDatas;
    private long deadTime;
    private TextView tvInvestMoney;
    private TextView creatTime;
    private LoadingUtil loadingUtil;
    private ImageView iMaster;
    private Bitmap bitmap;
    private LinearLayout praiseLinearLayout;
    private CircleImageView mImageView;
    private List<ArtWorkPraiseList> praiseHeadDatas;
    private CommonAdapter<ArtWorkPraiseList> praiseHeadCommonAdapter;
    private HorizontalListView praiseHLV;
    private CircleImageView myPraise;

    public RZProjectFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }

    public RZProjectFragment() {
    }

    private final Handler mHandler = new Handler();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRAISE_SUC:
                    isPraise1 = true;
                    int a = Integer.parseInt(praiseNum.getText().toString());
                    a++;
                    praiseNum.setText(a + "");
                    AnimPraiseCancel.animPraise(redPraise);
                    llpraise.setBackgroundResource(R.drawable.praise_after_shape);
                    praiseNum.setTextColor(Color.rgb(255, 255, 255));
                    myPraise.setVisibility(View.VISIBLE);
                    myPraise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PraiseListActivity.class);
                            intent.putExtra("artWorkId", artWorkId);
                            startActivity(intent);
                        }
                    });
                    AppApplication.displayImage(AppApplication.gUser.getPictureUrl(), myPraise);
                    break;
                case PRAISE_CANCEL:
                    isPraise1 = false;
                    AnimPraiseCancel.animCancelPraise(leftPraise,rightPraise);
                    int s = Integer.parseInt(praiseNum.getText().toString());
                    s--;
                    praiseNum.setText(s + "");
                    llpraise.setBackgroundResource(R.drawable.praise_shape);
                    praiseNum.setTextColor(Color.rgb(199, 31, 33));
                    myPraise.setVisibility(View.GONE);
                   /* for (int i = 0;i < praiseHeadDatas.size();i++){
                        if (AppApplication.gUser.getId().equals(praiseHeadDatas.get(i).getUser().getId())) {
                            praiseHeadDatas.remove(praiseHeadDatas.get(i));
                        }
                    }*/
                    for (Iterator<ArtWorkPraiseList> it = praiseHeadDatas.iterator(); it.hasNext(); ) {
                        ArtWorkPraiseList ss = it.next();
                        if (AppApplication.gUser.getId().equals(ss.getUser().getId())) {
                            it.remove();
                        }
                    }
                    praiseHeadCommonAdapter.notifyDataSetChanged();
                    break;
                case COUNT_DOWN:
                    deadTime -= 1000;
                    String time = DateUtil.millisToStringShort(deadTime, false, false).toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(time);

                    ForegroundColorSpan redSpan1 = new ForegroundColorSpan(0xFF999999);
                    ForegroundColorSpan redSpan2 = new ForegroundColorSpan(0xFF999999);
                    ForegroundColorSpan redSpan3 = new ForegroundColorSpan(0xFF999999);
                    ForegroundColorSpan redSpan4 = new ForegroundColorSpan(0xFF999999);
                    if (time.contains("天")) {
                        builder.setSpan(redSpan1, time.indexOf("天"), time.indexOf("天") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (time.contains("时")) {
                        builder.setSpan(redSpan2, time.indexOf("时"), time.indexOf("时") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (time.contains("分")) {
                        builder.setSpan(redSpan3, time.indexOf("分"), time.indexOf("分") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (time.contains("秒")) {
                        builder.setSpan(redSpan4, time.indexOf("秒"), time.indexOf("秒") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    deadline.setText(builder);
                    if (deadTime > 0) {
                        Message message = handler.obtainMessage(COUNT_DOWN);
                        handler.sendMessageDelayed(message, 1000);
                    } else {
                        deadline.setText("已截止");
                    }
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artCommentDatas = new ArrayList<ArtworkComment>();
        investorDatas = new ArrayList<>();
        investorTopDatas = new ArrayList<>();
        praiseHeadDatas = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rz_project, container, false);
        imageTitle = (ImageView) view.findViewById(R.id.cl_01_tv_prc);
        leftPraise = (ImageView) view.findViewById(R.id.iv_xin_hui_left);
        rightPraise = (ImageView) view.findViewById(R.id.iv_xin_hui_right);
        redPraise = (ImageView) view.findViewById(R.id.iv_praise_red);
        creatTime = (TextView) view.findViewById(R.id.tv_creat_time);
        headV = (ImageView) view.findViewById(R.id.iv_master);
        myPraise = (CircleImageView) view.findViewById(R.id.civ_pic);
        // ll_invester = (LinearLayout) view.findViewById(R.id.ll_invester);
        ll_project = (LinearLayout) view.findViewById(R.id.ll_project);
        rl_progress = (RelativeLayout) view.findViewById(R.id.rl_progress);
        tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
        deadline = (TextView) view.findViewById(R.id.tv_deadline);
        tvInvestor = (TextView) view.findViewById(R.id.tv_investor);
        tvInvestMoney = (TextView) view.findViewById(R.id.tv_invest_money);
        reader = (TextView) view.findViewById(R.id.tv_reader);
        tv_project_brief = (TextView) view.findViewById(R.id.tv_project_brief);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name2 = (TextView) view.findViewById(R.id.tv_name2);
        praiseNum = (TextView) view.findViewById(R.id.tv_praise);
        tvWorksNum = (TextView) view.findViewById(R.id.tv_num_works);
        tvFansNum = (TextView) view.findViewById(R.id.tv_num_fans);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        iMaster = (ImageView) view.findViewById(R.id.iv_master);
        go = (ImageButton) view.findViewById(R.id.ib_go);
        comment = (Button) view.findViewById(R.id.bt_comment);
        etComment = (EditText) view.findViewById(R.id.et_comment);
        attention = (ImageButton) view.findViewById(R.id.ib_attention);
        praiseHLV = (HorizontalListView) view.findViewById(R.id.hlv_praise);
        attention.setOnClickListener(this);
        comment.setOnClickListener(this);
        //invest = ((LinearLayout) view.findViewById(R.id.rzp_ll_invest));
        llpraise = (LinearLayout) view.findViewById(R.id.ll_praise);
        //llinvester = (LinearLayout) view.findViewById(R.id.ll_invester);
        mExpandView = (ExpandView) view.findViewById(R.id.expandView);
        sv = (ScrollView) view.findViewById(R.id.sv_sv);
        mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.rpb_progress);
        iv_show = (ImageView) view.findViewById(R.id.iv_is_show);
        rl_progress.setOnClickListener(this);
        mRoundProgressBar.setTextSize(28);
        cl_headPortrait = (CircleImageView) view.findViewById(R.id.cl_headPortrait);
        loadingUtil = new LoadingUtil(getActivity(), getContext());
        mExpandView.setLayoutId(R.layout.layout_expand);
        webView = (WebView) mExpandView.findViewById(R.id.wv_invest_process);
        webView.loadUrl("file:///android_asset/InvestFlowControlller.html");
        mExpandView.collapse();
        for (int i = 0; i < mExpandView.getChildCount(); i++) {
            mExpandView.getChildAt(i).setVisibility(View.GONE);
        }
        //默认顶部
        imageTitle.setFocusable(true);
        imageTitle.setFocusableInTouchMode(true);
        imageTitle.requestFocus();
        llpraise.setOnClickListener(this);
        go.setOnClickListener(this);
        mListview = (ListViewForScrollView) view.findViewById(R.id.lv_comment);
        iListview = (ListViewForScrollView) view.findViewById(R.id.lv_invester);
        iTopListview = (ListViewForScrollView) view.findViewById(R.id.lv_invester_top);
        iTopListview.hideFooterView();
        //invest.setOnClickListener(this);
        praiseHLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PraiseListActivity.class);
                intent.putExtra("artWorkId", artWorkId);
                startActivity(intent);
            }
        });
        showOther();
        showHeadNum();
        loadCommentData(true, currentPage);
        loadInvestorData(true, currentPage);
        setInvestorTopAdapter();
        setInvesterAdapter();
        setCommentAdapter();
        setPraiseHeadAdapter();
        etComment.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        return view;
    }
    private void showHeadNum() {
        //测量箭头的宽高
        int heightArrow = go.getMeasuredHeight();
        int widthArrow = go.getMeasuredWidth();
        //测量prise宽高
        int heightPraise = llpraise.getMeasuredHeight();
        int widthPraise = llpraise.getMeasuredWidth();
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        widthScreen = metric.widthPixels;
        //图片宽高
        widthImage = Utils.dip2px(getActivity(), 24);
        heightIamge = Utils.dip2px(getActivity(), 24);
        //图片padding
        right = Utils.dip2px(getActivity(), 10);
        //一行能容纳的图片个数
        //praise与view间距
        padding = Utils.dip2px(getActivity(), 100);
        widthView = widthScreen - widthPraise - widthArrow - padding;
        count = widthView / (widthImage + right);
    }

    private void setCommentAdapter() {
        artCommentAdapter = new CommonAdapter<ArtworkComment>(getActivity(), artCommentDatas, R.layout.pdonclicktab_comment_item) {
            @Override
            public void convert(final ViewHolder helper, final ArtworkComment item) {
                LinearLayout linearLayout = helper.getView(R.id.pdctci_ll_all);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AppApplication.getSingleContext(), ProjectCommentReply.class);
                        if (item.getCreator() != null) {
                            if ("1".equals(item.getCreator().getType())) {
                                helper.getView(R.id.iv_master).setVisibility(View.VISIBLE);
                            } else {
                                helper.getView(R.id.iv_master).setVisibility(View.INVISIBLE);
                            }
                            intent.putExtra("name", item.getCreator().getName());
                        } else {
                            intent.putExtra("name", "");
                        }
                        intent.putExtra("currentUserId", AppApplication.gUser.getId());
                        intent.putExtra("fatherCommentId", item.getId());
                        intent.putExtra("artworkId", artWorkId);
                        intent.putExtra("flag", 0);
                        intent.putExtra("messageId", "");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (!item.getId().equals(AppApplication.gUser.getId())) {
                            AppApplication.getSingleContext().startActivity(intent);
                        }
                    }
                });

                helper.getView(R.id.pdctci_tv_nickName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCreator().getMaster() != null) {
                            Intent intent = new Intent(getActivity(), ArtistIndexActivity.class);
                            intent.putExtra("userId", item.getCreator().getId());
                            intent.putExtra("name", item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), UserIndexActivity.class);
                            intent.putExtra("userId", item.getCreator().getId());
                            intent.putExtra("name", item.getCreator().getName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }
                    }
                });
                if (item.getCreator() != null) {
                    helper.setText(R.id.pdctci_tv_nickName, item.getCreator().getName());
                    helper.setImageByUrl(R.id.pdctci_iv_icon, item.getCreator().getPictureUrl());
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
                                Intent intent = new Intent(getActivity(), ArtistIndexActivity.class);
                                intent.putExtra("userId", item.getFatherComment().getCreator().getId());
                                intent.putExtra("name", item.getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), UserIndexActivity.class);
                                intent.putExtra("userId", item.getFatherComment().getCreator().getId());
                                intent.putExtra("name", item.getCreator().getName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
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
        mListview.setAdapter(artCommentAdapter);

    }

    private void loadCommentData(final boolean flag, final int pageNum) {
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
                    artCommentDatas.clear();
                    if (flag) {
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<ArtworkComment>>() {
                        }.getType());

                        if (commentList != null) {
                            loadNum5(mListview, artCommentDatas, commentList);
                            commentList.clear();
                            artCommentAdapter.notifyDataSetChanged();
                        } else {
                            mListview.hideFooterView();
                        }
                    }
                } else if ("000000".equals(response.get("resultCode"))) {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                loadCommentData(flag, pageNum);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    private void showOther() {
        loadingUtil.show();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
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
                loadingUtil.dismiss();
                Map<String, Object> object = (Map<String, Object>) response.get("object");
                if (object != null) {
                    artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                            toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraiseList>>() {
                    }.getType());

                    loadPraiseHeadData(artWorkPraiseList);
                    isPraise1 = Boolean.parseBoolean(AppApplication.getSingleGson().toJson(object.get("isPraise")));
                    artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")), Artwork.class);
                    deadTime = Long.parseLong(artwork.getInvestRestTime());
                    creatTime.setText(DateUtil.millionToNearly(artwork.getCreateDatetime()));
                    tv_project_name.setText(artwork.getTitle());
                    tv_project_brief.setText(artwork.getBrief());
                    tvWorksNum.setText(artwork.getAuthor().getMasterWorkNum() + "件作品");
                    tvFansNum.setText(artwork.getAuthor().getFansNum() + "个粉丝");
                    tv_price.setText("￥ " + artwork.getInvestGoalMoney() + ".00");
                    tvInvestor.setText(artwork.getInvestNum() + "人投资 ");
                    tvInvestMoney.setText("￥" + artwork.getInvestsMoney() + "/" + artwork.getInvestGoalMoney());
                    remainMoney = artwork.getInvestGoalMoney().subtract(artwork.getInvestsMoney()).intValue();
                    Message msg = Message.obtain();
                    msg.what = COUNT_DOWN;
                    handler.sendMessageDelayed(msg, 1000);

                    //进度显示
                    progressCurrent = artwork.getInvestsMoney().doubleValue() / artwork.getInvestGoalMoney().doubleValue();
                    max = artwork.getInvestGoalMoney().intValue();
                    isVisibleBar();

                    if (artwork.getAuthor() != null) {
                        tv_name.setText(artwork.getAuthor().getName());
                        tv_name2.setText(artwork.getAuthor().getName());
                        AppApplication.displayImage(artwork.getAuthor().getPictureUrl(), cl_headPortrait);
                        if ("1".equals(artwork.getType())) {
                            headV.setVisibility(View.VISIBLE);
                        } else {
                            headV.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (isPraise1) {
                        llpraise.setBackgroundResource(R.drawable.praise_after_shape);
                        praiseNum.setTextColor(Color.rgb(255, 255, 255));
                    } else {
                        llpraise.setBackgroundResource(R.drawable.praise_shape);
                        praiseNum.setTextColor(Color.rgb(199, 31, 33));
                    }
                    praiseNum.setText(artwork.getPraiseNUm() + "");
                    reader.setText(artwork.getViewNum() + "");
                    AppApplication.displayImage(artwork.getPicture_url(), imageTitle);
                }
            }
        });
    }

    private void isVisibleBar() {
        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        mRoundProgressBar.getLocationInWindow(location);
        System.out.println(Arrays.toString(location));
        sv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    if (mRoundProgressBar.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
                        if (progress == 0 && progressCurrent != 0) {
                            initProgressBar((int) (progressCurrent * 100));
                        }
                    } else {
                        progress = 0;
                    }

                } else if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {

                    if (mRoundProgressBar.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
                        if (progress == 0) {
                            initProgressBar((int) (progressCurrent * 100));
                        }
                    } else {
                        progress = 0;
                    }

                }
                return false;
            }

        });


    }

    private void initProgressBar(final int temp) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (progress <= temp) {
                    progress += 1;
                    System.out.println(progress);
                    mRoundProgressBar.setProgress(progress);
                    try {
                        if (temp!=0){
                            Thread.sleep(250 / temp);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void loadPraiseHeadData(final List<ArtWorkPraiseList> artWorkPraiseList) {
        if (count == 0) {
            go.setVisibility(View.GONE);
        }
        if (count != 0 && artWorkPraiseList.size() > count) {
            for (int i = 0; i < count; i++) {
                praiseHeadDatas.add(artWorkPraiseList.get(i));
            }
        } else {
            praiseHeadDatas.addAll(artWorkPraiseList);
        }
    }

    private void setPraiseHeadAdapter() {
        praiseHeadCommonAdapter = new CommonAdapter<ArtWorkPraiseList>(getActivity(), praiseHeadDatas, R.layout.praise_head_item) {
            @Override
            public void convert(ViewHolder helper, ArtWorkPraiseList item) {
                if (item.getUser().getPictureUrl() != null) {
                    helper.setImageByUrl(R.id.civ_head, item.getUser().getPictureUrl());
                } else {
                    helper.setImageResource(R.id.civ_head, R.mipmap.default_icon);
                }
            }


        };
        praiseHLV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        praiseHLV.setAdapter(praiseHeadCommonAdapter);
    }
    private Bitmap comp(Bitmap response, String pictureUrl) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format = Utils.getImageFormatBig(pictureUrl);
        response.compress(format, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            response.compress(format, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap, pictureUrl);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image, String pictureUrl) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format = Utils.getImageFormatBig(pictureUrl);
        image.compress(format, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void clickHead(int temp) {
        if (artWorkPraiseList.get(temp).getUser().getMaster() != null) {
            Intent intent = new Intent(getActivity(), ArtistIndexActivity.class);
            intent.putExtra("userId", artWorkPraiseList.get(temp).getUser().getId());
            intent.putExtra("name", artWorkPraiseList.get(temp).getUser().getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), UserIndexActivity.class);
            intent.putExtra("userId", artWorkPraiseList.get(temp).getUser().getId());
            intent.putExtra("name", artWorkPraiseList.get(temp).getUser().getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        artCommentDatas.clear();
        currentPage = 1;
        loadCommentData(true, currentPage);
    }

    @Override
    protected void lazyLoad() {
    }

    private void setInvesterAdapter() {
        investorRecordCommonAdapter = new CommonAdapter<ArtworkInvest>(getActivity(), investorDatas, R.layout.investorrecord_item) {
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
        iListview.setAdapter(investorRecordCommonAdapter);
    }

    private void loadInvestorData(final boolean flag, int pageNum) {

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
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    if (flag) {

                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        List<ArtworkInvestTop> investTopList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                toJson(object.get("artworkInvestTopList")), new TypeToken<List<ArtworkInvestTop>>() {
                        }.getType());
                        if (investTopList != null) {
                            investorTopDatas.addAll(investTopList);
                            investorTopAdapter.notifyDataSetChanged();
                            investTopList.clear();
                        }
                        if (investList != null) {
                            // loadNum5(iListview, investorDatas, investList);
                            addInvestorNum(investorDatas, investList);
                            investorRecordCommonAdapter.notifyDataSetChanged();
                            //investList.clear();
                        } else {
                            iListview.hideFooterView();
                        }
                    }
                }
            }

        });
    }

    private void addInvestorNum(final List investorDatas, final List investList) {
        if (investList.size() <= 2) {
            investorDatas.addAll(investList);
            iListview.hideFooterView();
        } else {
            for (int i = 0; i < 2; i++) {
                investorDatas.add(investList.get(i));
            }
            iListview.showFooterView();
            Intent intent = new Intent(getActivity(), InvestorActivity.class);
            clickMore(iListview, intent);
        }
    }

    private void clickMore(ListViewForScrollView myListview, final Intent intent) {
        myListview.footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("artWorkId", artWorkId);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setInvestorTopAdapter() {
        investorTopAdapter = new CommonAdapter<ArtworkInvestTop>(getActivity(), investorTopDatas, R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvestTop item) {
                if (item.getCreator() != null) {
                    helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
                    if (item.getCreator().getName() != null) {
                            helper.setText(R.id.iri_tv_nickname, item.getCreator().getName());
                    }
                }

                if (helper.getPosition() == 0) {
                    helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
                    helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
                    helper.setImageResource(R.id.civ_top, R.mipmap.jinpai);
                } else if (helper.getPosition() == 1) {
                    helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
                    helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
                    helper.setImageResource(R.id.civ_top, R.mipmap.yinpai);
                } else if (helper.getPosition() == 2) {
                    helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
                    helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
                    helper.setImageResource(R.id.civ_top, R.mipmap.tongpai);
                }
                helper.setText(R.id.iri_tv_content, "￥" + item.getPrice());
                helper.setText(R.id.iri_tv_date, DateUtil.millionToNearly(item.getCreateDatetime()));
            }
        };
        iTopListview.setAdapter(investorTopAdapter);
    }


    private void loadNum5(ListViewForScrollView listView, List dataList, List list) {
        if (list.size() <= 4) {
            dataList.addAll(list);
            listView.hideFooterView();
        } else if (list.size() == 5) {
            dataList.addAll(list);
            listView.showFooterView();
        } else {
            for (int i = 0; i < 5; i++) {
                dataList.add(list.get(i));
            }
            listView.showFooterView();

        }
        Intent intent = new Intent(getActivity(), CommentListActivity.class);
        clickMore(listView, intent);
    }
    public int getRemainMoney(){
        return remainMoney;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_praise:
                //点赞
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                } else {
                    if (!isPraise1) {
                        praise(artWorkId);
                    } else {
                        cancelPraise(artWorkId);
                    }
                }
                break;
            case R.id.ib_go:
                Intent intent = new Intent(getActivity(), PraiseListActivity.class);
                intent.putExtra("artWorkId", artWorkId);
                startActivity(intent);
                break;
            /*case R.id.rzp_ll_invest:
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent1 = new Intent(getActivity(), InvestActivity.class);
                    intent1.putExtra("allMoney", remainMoney);
                    intent1.putExtra("artWorkId", artWorkId);
                    startActivity(intent1);
                }
                break;*/
            case R.id.rl_progress:
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                    //  sv.removeView(mExpandView);

                    // mHandler.post(ScrollRunnable2);
                    for (int i = 0; i < mExpandView.getChildCount(); i++) {
                        mExpandView.getChildAt(i).setVisibility(View.GONE);
                    }
                    /*View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.rz_project, null);
                    showHeadNum();
                    loadInvestorData(true, currentPage);
                    loadCommentData(true, currentPage);
                    showOther();
                    getActivity().setContentView(inflate);*/
                   /*EmptyFragment ef = new EmptyFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.expandView, ef);
                    //ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                    ft.commit();*/
                    iv_show.setImageResource(R.mipmap.show_progress);
                    mExpandView.setVisibility(View.VISIBLE);
                } else {
                    mExpandView.expand();

                    for (int i = 0; i < mExpandView.getChildCount(); i++) {
                        mExpandView.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    mHandler.post(ScrollRunnable);
                    iv_show.setImageResource(R.mipmap.no_show_progress);
                }
                break;
            case R.id.bt_comment:
                commit();
                break;
            case R.id.ib_attention:
                if (AppApplication.gUser != null && !"".equals(AppApplication.gUser.getId())) {
                    Intent intent1 = new Intent(getActivity(), AttentionActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("userId", AppApplication.gUser.getId());
                    intent1.putExtra("otherUserId", artwork.getAuthor().getId());
                    if (AppApplication.gUser.getId().equals(artwork.getAuthor().getId())) {
                        intent1.putExtra("flag", "1");
                    } else {
                        intent1.putExtra("flag", "2");
                    }
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                }

                break;
            default:
                break;
        }

    }

    private void commit() {
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(etComment, new ContentValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if (!AppApplication.getSingleEditTextValidator().validate()) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artWorkId + "");
        paramsMap.put("content", etComment.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkComment.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("评论失败");
                ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")) {
                    ToastUtil.show(getActivity(), "评论成功", Toast.LENGTH_SHORT);
                    etComment.setText("");
                    artCommentDatas.clear();
                    loadCommentData(true, currentPage);
                } else if ("000000".equals(response.get("resultCode"))) {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                commit();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollDifference = Utils.dip2px(getActivity(), 160 + 44);
            int off = screenHeight;//判断高度
            /*if (off > 0) {
                sv.scrollBy(0, 30);
                if (sv.getScrollY() == off) {
                    Thread.currentThread().interrupt();
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }*/
            sv.smoothScrollBy(0, screenHeight - scrollDifference);
        }
    };

    private void praise(final String artworkId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artworkId + "");
        paramsMap.put("action", "1");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    if (!isPraise1){
                        Message msg = Message.obtain();
                        msg.what = PRAISE_SUC;
                        handler.sendMessage(msg);
                    }else {
                        cancelPraise(artworkId);
                    }
                } else if ("000000".equals(response.get("resultCode"))) {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                praise(artworkId);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }

            }
        });
    }

    private void cancelPraise(final String artworkId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artworkId + "");
        paramsMap.put("action", "0");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    if (isPraise1){
                        Message msg = Message.obtain();
                        msg.what = PRAISE_CANCEL;
                        handler.sendMessage(msg);
                    }else {
                        praise(artworkId);
                    }
                } else if ("000000".equals(response.get("resultCode"))) {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                cancelPraise(artworkId);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
}