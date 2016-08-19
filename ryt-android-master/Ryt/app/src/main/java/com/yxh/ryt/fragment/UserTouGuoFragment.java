package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ConvertWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class UserTouGuoFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<ConvertWork> userZGCommonAdapter;
	private List<ConvertWork> userZGDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private TextView tvTotal,tvNoData;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private static String userId;
	private static String currentId;
	private View header;
	private InvestReceiver receiver;

	public UserTouGuoFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}

	public UserTouGuoFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static UserTouGuoFragment newInstance(StickHeaderViewPagerManager manager, int position) {
		UserTouGuoFragment listFragment = new UserTouGuoFragment(manager, position);
		return listFragment;
	}
	public UserTouGuoFragment(){
		super();
	}
	public static UserTouGuoFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh, String userID, String currentID) {
		UserTouGuoFragment listFragment = new UserTouGuoFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		userId=userID;
		currentId=currentID;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userZGDatas=new ArrayList<ConvertWork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_touguo, container,false);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
		lstv = (ListView)view.findViewById(R.id.fit_lstv);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		header = LayoutInflater.from(getActivity()).inflate(R.layout.touguo_header, null);
		tvNoData = (TextView) view.findViewById(R.id.fit_tv_noData);
		setAdapter();
		onScroll();
		userZGDatas.clear();
		LoadData(true, currentPage);
		receiver = new InvestReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.CAST_BROADCAST");
		AppApplication.getSingleContext().registerReceiver(receiver, filter);
		return view;
	}
	public class InvestReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			currentPage=currentPage+1;
			LoadData(false,currentPage);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		AppApplication.getSingleContext().unregisterReceiver(receiver);
	}
	private void setAdapter() {
		userZGCommonAdapter=new CommonAdapter<ConvertWork>(AppApplication.getSingleContext(),userZGDatas,R.layout.userpt__touguo_item) {
			@Override
			public void convert(ViewHolder helper, ConvertWork item) {
				if (userZGDatas!=null){
					helper.setImageByUrl(R.id.utf_iv_icon, item.getPicture_url());
					helper.setText(R.id.utf_tv_proName, item.getTitle());
					helper.setText(R.id.utf_tv_proStage,AppApplication.ptMap.get(item.getStep()));
					helper.setText(R.id.utf_tv_money,"项目金额:"+item.getGoalMoney());
					helper.setText(R.id.utf_tv_name,item.getUser().getName());
					helper.setText(R.id.utf_tv_zhicheng,item.getUser().getMaster().getTitle());
					helper.setImageByUrl(R.id.utf_iv_headPortrait,item.getUser().getPictureUrl());
				}
			}
		};
		lstv.setAdapter(userZGCommonAdapter);
		lstv.addFooterView(footer);
		lstv.addHeaderView(header);
		tvTotal=((TextView) header.findViewById(R.id.fit_tv));
		loadFull = (TextView) footer.findViewById(R.id.loadFull);
		noData = (TextView) footer.findViewById(R.id.noData);
		more = (TextView) footer.findViewById(R.id.more);
		loading = (ProgressBar) footer.findViewById(R.id.loading);
		more.setVisibility(View.VISIBLE);
		loading.setVisibility(View.GONE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	private void onScroll() {

	}
	@Override
	protected void lazyLoad() {

	}
	private void LoadData(final boolean flag,int pageNum) {
		more.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId",userId);
		//paramsMap.put("currentId", currentId);
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}
			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> object = (Map<String, Object>) response.get("pageInfo");
					tvTotal.setText("投资项目:"+AppApplication.getSingleGson().toJson(((Map<String, Object>) response.get("pageInfo")).get("num"))+"个");
					if (flag) {
						List<ConvertWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworks")), new TypeToken<List<ConvertWork>>() {
						}.getType());

						if (commentList == null) {
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.VISIBLE);
							header.setVisibility(View.GONE);
						} else if (commentList.size()==0){
							header.setVisibility(View.GONE);
							footer.setVisibility(View.GONE);
							tvNoData.setVisibility(View.VISIBLE);
							return;
						} else if (commentList.size() < Constants.pageSize){
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.VISIBLE);
							noData.setVisibility(View.GONE);
							userZGDatas.addAll(commentList);
							commentList.clear();
							lstv.requestLayout();
							userZGCommonAdapter.notifyDataSetChanged();
						}else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							userZGDatas.addAll(commentList);
							commentList.clear();
						}
						lstv.requestLayout();
						userZGCommonAdapter.notifyDataSetChanged();
					}else {
						List<ConvertWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworks")), new TypeToken<List<ConvertWork>>() {
						}.getType());
						if (commentList == null || commentList.size() < Constants.pageSize) {
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.VISIBLE);
							noData.setVisibility(View.GONE);
						} else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							userZGDatas.addAll(commentList);
							commentList.clear();
						}
						lstv.requestLayout();
						userZGCommonAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	@Override
	public void onPause() {
		super.onPause();
		userZGDatas.clear();
		if (userZGCommonAdapter!=null){
			userZGCommonAdapter.notifyDataSetChanged();
		}
	}
}