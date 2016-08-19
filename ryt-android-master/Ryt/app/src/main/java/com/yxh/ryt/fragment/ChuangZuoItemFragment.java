package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.AuctionSummaryActivity;
import com.yxh.ryt.activity.ChuangZuoXQActivity;
import com.yxh.ryt.activity.CreateSummaryActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class ChuangZuoItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
	private AutoListView lstv;
	private CommonAdapter<Create> chuangZuoCommonAdapter;
	private List<Create> chuangZuoDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chuangZuoDatas=new ArrayList<Create>();
	}

	public ChuangZuoItemFragment() {
	}

	private void LoadData(final int state, final int pageNum) {
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
		NetRequestUtil.post(Constants.BASE_PATH + "artWorkCreationList.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))){
					Map<String,String> object= (Map<String, String>) response.get("object");
					if (state==AutoListView.REFRESH){
						lstv.onRefreshComplete();
						chuangZuoDatas.clear();
						List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<Create>>() {
						}.getType());
						if(null==objectList||objectList.size()==0){
							lstv.setResultSize(0);
						}
						if (null!=objectList&&objectList.size()>0){
							lstv.setResultSize(objectList.size()); //还有数据加载。。。
							chuangZuoDatas.addAll(objectList);
							chuangZuoCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
					if (state==AutoListView.LOAD){
						lstv.onLoadComplete();
						List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<Create>>() {
						}.getType());
						if(null==objectList||objectList.size()==0){
							lstv.setResultSize(1);   //已全部加载完毕
						}
						if (null!=objectList&&objectList.size()>0) {
							lstv.setResultSize(objectList.size());  //还有数据加载。。。
							chuangZuoDatas.addAll(objectList);
							chuangZuoCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
				}/*else if ("000000".equals(response.get("resultCode"))){
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								LoadData(state,pageNum);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}*/

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_item_1, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		chuangZuoCommonAdapter=new CommonAdapter<Create>(AppApplication.getSingleContext(),chuangZuoDatas,R.layout.create_list_item) {
			@Override
			public void convert(ViewHolder helper, final Create item) {
				helper.setText(R.id.cl_01_tv_title,item.getTitle());
				helper.setText(R.id.cl_01_tv_brief,item.getBrief());
				helper.setText(R.id.cl_01_tv_name, item.getAuthor().getName());
				helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait, item.getAuthor().getPictureUrl());
				helper.getView(R.id.cl_01_tv_prc).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(), CreateSummaryActivity.class);
						intent.putExtra("id", item.getId());
						intent.putExtra("name", item.getTitle());
						startActivity(intent);
					}
				});
				if ("暂无更新状态".equals(item.getNewCreationDate())){
					helper.setText(R.id.cli_tv_update,"暂无更新状态");
				}else {
					helper.setText(R.id.cli_tv_update,"最新更新:"+item.getNewCreationDate()+"前");
				}
				if (item.getCreationEndDatetime()>System.currentTimeMillis()){
					helper.setText(R.id.cli_tv_predictComplete, "预计完工:" + Utils.timeTransMonth(item.getCreationEndDatetime()) + "月" + Utils.timeTransDate(item.getCreationEndDatetime())+"日");
				}
			}
		};
		lstv.setAdapter(chuangZuoCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		lstv.setOnItemClickListener(this);
		return contextView;
	}

	@Override
	protected void lazyLoad() {
		if(chuangZuoDatas!=null&&chuangZuoDatas.size()>0)return;
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
		if (position<=chuangZuoDatas.size()){
			Intent intent=new Intent(getActivity(), CreateSummaryActivity.class);
			intent.putExtra("id", chuangZuoDatas.get(position-1).getId());
			intent.putExtra("name", chuangZuoDatas.get(position-1).getTitle());
			startActivity(intent);
		}
	}
}