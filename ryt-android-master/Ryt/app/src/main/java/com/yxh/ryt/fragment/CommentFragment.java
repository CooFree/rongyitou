package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtworkComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/13.
 */
@SuppressLint("ValidFragment")
public class CommentFragment extends BaseFragment implements AutoListView.OnLoadListener {
    private AutoListView lstv;
    private CommonAdapter<ArtworkComment> commentCommonAdapter;
    private List<ArtworkComment> commentDatas;
    private int currentPage=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        lstv = (AutoListView) contextView.findViewById(R.id.lstv);
        lstv.setPageSize(Constants.pageSize);
        commentCommonAdapter=new CommonAdapter<ArtworkComment>(AppApplication.getSingleContext(),commentDatas,R.layout.pdonclicktab_comment_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkComment item) {

            }
        };
        lstv.setAdapter(commentCommonAdapter);
        lstv.setOnLoadListener(this);
        return contextView;
    }

    public CommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentDatas=new ArrayList<ArtworkComment>();
    }

    @Override
    protected void lazyLoad() {
        if(commentDatas!=null&&commentDatas.size()>0)return;
        LoadData(AutoListView.LOAD, currentPage);
    }
    private void LoadData(final int state,int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageNum", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorIndex.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                /*if (state == AutoListView.REFRESH) {
                    lstv.onRefreshComplete();
                    commentDatas.clear();
                    List<ArtworkComment> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkComment>>() {
                    }.getType());
                    if (null == objectList || objectList.size() == 0) {
                        lstv.setResultSize(0);
                    }
                    if (null != objectList && objectList.size() > 0) {
                        lstv.setResultSize(objectList.size());
                        commentDatas.addAll(objectList);
                        commentCommonAdapter.notifyDataSetChanged();
                    }
                    return;
                }*/
                if (state == AutoListView.LOAD) {
                    /*lstv.onLoadComplete();
                    List<ArtworkComment> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkComment>>() {
                    }.getType());
                    if (null == objectList || objectList.size() == 0) {
                        lstv.setResultSize(1);
                    }
                    if (null != objectList && objectList.size() > 0) {
                        lstv.setResultSize(objectList.size());
                        commentDatas.addAll(objectList);
                        commentCommonAdapter.notifyDataSetChanged();
                    }
                    return;*/

                }
            }
        });
    }

    @Override
    public void onLoad() {

    }
}
