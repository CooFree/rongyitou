package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Investor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class PaiHangItemFragment01 extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener {
	private AutoListView lstv;
	private CommonAdapter<Investor> investorCommonAdapter;
	private List<Investor> investorDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		investorDatas=new ArrayList<Investor>();
	}
	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize",Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "getInvestorTopList.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if (state==AutoListView.REFRESH){
					lstv.onRefreshComplete();
					investorDatas.clear();
					List<Investor> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("InvestorTopList")), new TypeToken<List<Investor>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(0);
					}
					if (null!=objectList&&objectList.size()>0){
						lstv.setResultSize(objectList.size());
						investorDatas.addAll(objectList);
						investorCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state==AutoListView.LOAD){
					lstv.onLoadComplete();
					List<Investor> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("InvestorTopList")), new TypeToken<List<Investor>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(1);
					}
					if (null!=objectList&&objectList.size()>0) {
						lstv.setResultSize(objectList.size());
						investorDatas.addAll(objectList);
						investorCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
			}
		});
	}

	public PaiHangItemFragment01() {
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
		View contextView = inflater.inflate(R.layout.paihang_touzi, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		investorCommonAdapter=new CommonAdapter<Investor>(AppApplication.getSingleContext(),investorDatas,R.layout.paihang_touzi_lv_item) {
			@Override
			public void convert(ViewHolder helper, final Investor item) {

				if (helper.getPosition() == 0) {
					helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
					helper.setImageResource(R.id.civ_top, R.mipmap.jin);
				} else if (helper.getPosition() == 1) {
					helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
					helper.setImageResource(R.id.civ_top, R.mipmap.yin);
				} else if (helper.getPosition() == 2) {
					helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
					helper.setImageResource(R.id.civ_top, R.mipmap.tong);
				} else {
					helper.getView(R.id.civ_top).setVisibility(View.GONE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.VISIBLE);
					helper.setText(R.id.cl_01_civ_pm, (helper.getPosition() + 1) + "");
				}
				helper.getView(R.id.ptli_rl_head).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if ("".equals(AppApplication.gUser.getId())){
							Intent intent=new Intent(getActivity(), LoginActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}else {
							Intent intent=new Intent(getActivity(), UserIndexActivity.class);
							intent.putExtra("userId",item.getUser_id());
							intent.putExtra("name",item.getTruename());
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					}
				});
				helper.setText(R.id.cl_01_civ_name, item.getTruename());
				helper.setText(R.id.cl_01_civ_rois, df.format(item.getRois().doubleValue()));
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait, item.getPicture());

			}
		};
		lstv.setAdapter(investorCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	protected void lazyLoad() {
		if(investorDatas!=null&&investorDatas.size()>0)return;
		LoadData(AutoListView.REFRESH, currentPage);
	}
	@Override
	public void onRefresh() {
		currentPage=1;
		LoadData(AutoListView.REFRESH,currentPage);
	}

	@Override
	public void onLoad() {
		currentPage++;
		LoadData(AutoListView.LOAD,currentPage);
	}

}