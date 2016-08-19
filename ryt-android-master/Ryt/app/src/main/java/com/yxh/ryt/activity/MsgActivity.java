package com.yxh.ryt.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.ChatMsgViewAdapter;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.NotifaicationCallBack;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ChatMsgEntity;
import com.yxh.ryt.vo.PrivateLetter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MsgActivity extends BaseActivity implements OnClickListener {

    private TextView mBtnSend;
    private EditText mEditTextContent;
    private LinearLayout mBottom;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    private String fromId;
    private String name;
    private TextView title;
    private String userId;
    private String currentName;
    private ImageButton back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fromId = getIntent().getStringExtra("formId");
        name = getIntent().getStringExtra("name");
        userId = getIntent().getStringExtra("userId");
        currentName = getIntent().getStringExtra("currentName");
        EventBus.getDefault().register(this);
        initView();
        mEditTextContent.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }

    public void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (TextView) findViewById(R.id.btn_send);
        title = (TextView) findViewById(R.id.tv_top_ct);
        back = (ImageButton) findViewById(R.id.ib_top_rt);
        title.setText(name);
        mBtnSend.setOnClickListener(this);
        back.setOnClickListener(this);
        mBottom = (LinearLayout) findViewById(R.id.btn_bottom);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays, userId, fromId);
        mListView.setAdapter(mAdapter);
    }

    private String[] msgArray = new String[]{};

    private String[] dataArray = new String[]{};

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
            case R.id.ib_top_rt:
                finish();
            default:
                break;
        }
    }

    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(Utils.getCurrentTime());
            entity.setName(currentName);
            entity.setMsgType(false);
            entity.setText(contString);
            entity.setPicUrl(AppApplication.gUser.getPictureUrl());
            pushMessageRequst();
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    private void pushMessageRequst() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("content", mEditTextContent.getText() + "");
        /*paramsMap.put("fromUserId",AppApplication.gUser.getId() );*/
        paramsMap.put("fromUserId", userId);
        paramsMap.put("targetUserId", fromId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "pushMessage.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(MsgActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    System.out.println("成功了");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                pushMessageRequst();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(ChatMsgEntity entity) {
        if (entity.getUserId().equals(fromId)) {
            entity.setName(name);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void LoadData() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fromUserId", fromId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "commentDetail.do", paramsMap, new NotifaicationCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
                ToastUtil.showLong(MsgActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    List<PrivateLetter> notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<PrivateLetter>>() {
                    }.getType());
                    if (notificationList != null) {
                        Iterator<PrivateLetter> iterator = notificationList.iterator();
                        while (iterator.hasNext()) {
                            ChatMsgEntity entity = new ChatMsgEntity();
                            PrivateLetter next = iterator.next();
                            entity.setDate(Utils.timeToFormatTemp("yyyy-MM-dd hh:mm:ss", next.getCreateDatetime()));
                            entity.setName(next.getFromUser().getName());
                            entity.setPicUrl(next.getFromUser().getPictureUrl());
                            entity.setText(next.getContent());

                            entity.setUserId(next.getFromUser().getId());
                            if (userId.equals(next.getFromUser().getId())) {
                                entity.setMsgType(false);
                            }else {
                                entity.setMsgType(true);
                            }
                            mDataArrays.add(entity);
                        }
                        mAdapter.notifyDataSetChanged();
                        mListView.setSelection(mListView.getCount() - 1);
                    }else if ("000000".equals(response.get("resultCode"))){
                        SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                            @Override
                            public void getCode(String code) {
                                if ("0".equals(code)){
                                    LoadData();
                                }
                            }
                        });
                        sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                    }
                }
            }
        });
    }

}
