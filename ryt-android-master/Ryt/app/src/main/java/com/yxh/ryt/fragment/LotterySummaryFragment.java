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
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.ExpandView;
import com.yxh.ryt.custemview.ListViewForScrollView;
import com.yxh.ryt.custemview.RoundProgressBar;
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
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class LotterySummaryFragment extends BaseFragment{

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
    private ImageView dianzan;
    private boolean flag1 = true;
    private boolean isPraise1;
    private LinearLayout ll_invester;
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

    public LotterySummaryFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public LotterySummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summarylottery, container, false);
        etComment.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void lazyLoad() {

    }
}