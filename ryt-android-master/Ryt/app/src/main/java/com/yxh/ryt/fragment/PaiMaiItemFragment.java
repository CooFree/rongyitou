package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.AuctionSummaryActivity;
import com.yxh.ryt.activity.CreateSummaryActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class PaiMaiItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
	private AutoListView lstv;
	private CommonAdapter<RongZi> paiMaiCommonAdapter;
	private List<RongZi> paiMaiDatas;
	private int currentPage=1;
	private TextView tv_detail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		paiMaiDatas=new ArrayList<RongZi>();
	}

	public PaiMaiItemFragment() {
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
		NetRequestUtil.post(Constants.BASE_PATH + "artWorkAuctionList.do", paramsMap, new RongZiListCallBack() {
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
					paiMaiDatas.clear();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(0);
					}
					if (null!=objectList&&objectList.size()>0){
						lstv.setResultSize(objectList.size());
						paiMaiDatas.addAll(objectList);
						paiMaiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state==AutoListView.LOAD){
					lstv.onLoadComplete();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(1);
					}
					if (null!=objectList&&objectList.size()>0) {
						lstv.setResultSize(objectList.size());
						paiMaiDatas.addAll(objectList);
						paiMaiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_item_2, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		paiMaiCommonAdapter=new CommonAdapter<RongZi>(AppApplication.getSingleContext(),paiMaiDatas,R.layout.auction_list_item) {
			@Override
			public void convert(ViewHolder helper, final RongZi item) {
				helper.setText(R.id.cl_01_tv_title,item.getTitle());
				helper.setText(R.id.cl_01_tv_brief,item.getBrief());
				helper.setText(R.id.cl_01_tv_name, item.getAuthor().getName());
				helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait, item.getAuthor().getPictureUrl());
				/*helper.getView(R.id.cl_01_tv_prc).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(), AuctionSummaryActivity.class);
						intent.putExtra("id", item.getId());
						intent.putExtra("title", item.getTitle());
						intent.putExtra("name", item.getAuthor().getName());
						startActivity(intent);
					}
				});*/
				if(Integer.valueOf(item.getStep())==30){
					helper.getView(R.id.cli_tv_time).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_chengjiao_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_size).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_current_price).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_user).setVisibility(View.GONE);
					((ImageView) helper.getView(R.id.cl_01_iv_auction)).setImageResource(R.mipmap.paimaizhuangtai_paimaiyugao);
					helper.setText(R.id.cli_tv_time, "拍卖时间：" + Utils.timeToFormatTemp("MM月DD日 HH:mm", item.getAuctionStartDatetime()) + "--" + Utils.timeToFormatTemp("HH:mm", item.getAuctionEndDatetime()));
					helper.setText(R.id.cli_tv_current_price,"起拍价："+item.getInvestGoalMoney()+"");
				}
				if(Integer.valueOf(item.getStep())==31){
					helper.getView(R.id.cli_tv_time).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_chengjiao_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_size).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_current_price).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_user).setVisibility(View.GONE);
					helper.setText(R.id.cli_tv_size, "出价次数：" + item.getAuctionNum() + "次");
					((ImageView) helper.getView(R.id.cl_01_iv_auction)).setImageResource(R.mipmap.paimaizhuangtai_paimaizhong);
					helper.setText(R.id.cli_tv_current_price,"当前出价："+item.getNewBidingPrice()+"元");
				}
				if(Integer.valueOf(item.getStep())==32){
					helper.getView(R.id.cli_tv_time).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_size).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_current_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_chengjiao_price).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_user).setVisibility(View.VISIBLE);
					helper.setText(R.id.cli_tv_chengjiao_price, "成交价格：" + item.getNewBidingPrice() + "元");
					helper.setText(R.id.cli_tv_user, "拍品得主："+item.getWinner().getName());
					((ImageView) helper.getView(R.id.cl_01_iv_auction)).setImageResource(R.mipmap.paimaizhuangtai_paimaichenggong);
					helper.setText(R.id.cli_tv_size, "出价次数：" + item.getAuctionNum() + "次");
				}
			}
		};
		lstv.setAdapter(paiMaiCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		lstv.setOnItemClickListener(this);
		return contextView;
	}

	@Override
	protected void lazyLoad() {
			if(paiMaiDatas!=null&&paiMaiDatas.size()>0)return;
			LoadData(AutoListView.REFRESH, currentPage);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		if (position<=paiMaiDatas.size()){
			Intent intent=new Intent(getActivity(), AuctionSummaryActivity.class);
			intent.putExtra("id", paiMaiDatas.get(position-1).getId());
			intent.putExtra("title", paiMaiDatas.get(position-1).getTitle());
			intent.putExtra("name", paiMaiDatas.get(position-1).getAuthor().getName());
			startActivity(intent);
		}
	}
}