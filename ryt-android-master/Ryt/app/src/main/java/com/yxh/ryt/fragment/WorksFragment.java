package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.MasterWork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/18.
 */
@SuppressLint("ValidFragment")
public class WorksFragment extends BaseFragment {
    private String userId;
    private List<MasterWork> workDatas;
    private int currentPage = 1;
    private CommonAdapter<MasterWork> imageAdapter;
    private GridView gridView;

    public WorksFragment(String userId) {
        this.userId = userId;
    }

    public WorksFragment() {
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workDatas = new ArrayList<MasterWork>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_works, container,false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        loadData(true,currentPage);
        setAdapter();
        return view;
    }


    private void loadData(final boolean flag, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "userWork.do", paramsMap, new RZCommentCallBack() {
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
                        List<MasterWork> imageList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
                        }.getType());
                        if (imageList != null) {
                            workDatas.addAll(imageList);
                            imageList.clear();
                        }
                        imageAdapter.notifyDataSetChanged();

                    } else {
                        List<MasterWork> imageList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
                        }.getType());

                        if (imageList != null) {
                            workDatas.addAll(imageList);
                            imageList.clear();
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setAdapter() {
        imageAdapter = new CommonAdapter<MasterWork>(getActivity(),workDatas,R.layout.item_image_works) {
            @Override
            public void convert(ViewHolder helper, MasterWork item) {
                helper.setText(R.id.tv_title,item.getName());
                helper.setImageByUrl(R.id.iv_works,item.getPictureUrl());
            }
        };
        gridView.setAdapter(imageAdapter);

    }
}


