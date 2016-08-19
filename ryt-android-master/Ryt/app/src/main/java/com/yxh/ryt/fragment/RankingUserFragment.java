package com.yxh.ryt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
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


public class RankingUserFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
	private AutoListView lstv;
	private CommonAdapter<Investor> investorCommonAdapter;
	private List<Investor> investorDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		investorDatas=new ArrayList<Investor>();
	}

	public RankingUserFragment() {
	}

	private void LoadData(final int state, int pageNum) {
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

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.paihang_touzi, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		investorCommonAdapter=new CommonAdapter<Investor>(AppApplication.getSingleContext(),investorDatas,R.layout.fragment_ranking_user) {
			@Override
			public void convert(ViewHolder helper, final Investor item) {
				if (helper.getPosition()==0){
					helper.getView(R.id.fru_iv_medal).setVisibility(View.VISIBLE);
					((ImageView) helper.getView(R.id.fru_iv_medal)).setImageResource(R.mipmap.jinpai);
					helper.getView(R.id.fru_tv_medal).setVisibility(View.GONE);
				}else if (helper.getPosition()==1){
					helper.getView(R.id.fru_iv_medal).setVisibility(View.VISIBLE);
					((ImageView) helper.getView(R.id.fru_iv_medal)).setImageResource(R.mipmap.yinpai);
					helper.getView(R.id.fru_tv_medal).setVisibility(View.GONE);
				}else if (helper.getPosition()==2){
					helper.getView(R.id.fru_iv_medal).setVisibility(View.VISIBLE);
					((ImageView) helper.getView(R.id.fru_iv_medal)).setImageResource(R.mipmap.tongpai);
					helper.getView(R.id.fru_tv_medal).setVisibility(View.GONE);
				}else {
					helper.getView(R.id.fru_iv_medal).setVisibility(View.GONE);
					helper.getView(R.id.fru_tv_medal).setVisibility(View.VISIBLE);
					helper.setText(R.id.fru_tv_content,helper.getPosition()+1+"");
				}
				helper.setImageByUrl(R.id.fru_civ_headerImage,item.getPicture());
				helper.setText(R.id.fru_tv_name,item.getTruename());
				float f =Float.valueOf(String.valueOf(item.getRois()));
				String s = String.format("%.2f", f);
				helper.setText(R.id.fru_tv_content,"￥"+s);
			}
		};
		lstv.setAdapter(investorCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		lstv.setOnItemClickListener(this);
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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position>0 &&position<=investorDatas.size()){
			Intent intent=new Intent(getActivity(),UserIndexActivity.class);
			intent.putExtra("userId",investorDatas.get(position-1).getUser_id());
			intent.putExtra("name",investorDatas.get(position-1).getTruename());
			startActivity(intent);
		}
	}
}