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
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.FollowUserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class FansArtItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener {
	private AutoListView lstv;
	private CommonAdapter<FollowUserUtil> attentionCommonAdapter;
	private List<FollowUserUtil> attentionDatas;
	private int currentPage=1;
	private String flag;
	private boolean bo = false;
	private String otherUserId;
	private String userId;
	public FansArtItemFragment(String userId, String otherUserId, String flag) {
		super();
		this.userId=userId;
		this.flag=flag;
		this.otherUserId=otherUserId;
	}

	public FansArtItemFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		attentionDatas=new ArrayList<FollowUserUtil>();
	}

	private void LoadData(final int state, final int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		//paramsMap.put("userId",userId);
		paramsMap.put("type","1");
		paramsMap.put("pageSize", Constants.pageSize + "");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		paramsMap.put("otherUserId", otherUserId);
		try {
			paramsMap.put("flag",flag);
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "userFans.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))){
					Constants.ATTENTION_TITLE[0]="艺术家("+AppApplication.getSingleGson().toJson(response.get("followsNum"))+")";
					Intent intent = new Intent("android.intent.action.MY_BROADCAST");
					AppApplication.getSingleContext().sendBroadcast(intent);
					if (state == AutoListView.REFRESH) {
						lstv.onRefreshComplete();
						attentionDatas.clear();
						List<FollowUserUtil> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<FollowUserUtil>>() {
						}.getType());
						if (null == objectList || objectList.size() == 0) {
							lstv.setResultSize(0);
							attentionCommonAdapter.notifyDataSetChanged();
						}
						if (null != objectList && objectList.size() > 0) {
							lstv.setResultSize(objectList.size());
							attentionDatas.addAll(objectList);
							attentionCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
					if (state == AutoListView.LOAD) {
						lstv.onLoadComplete();
						List<FollowUserUtil> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<FollowUserUtil>>() {
						}.getType());
						if (null == objectList || objectList.size() == 0) {
							lstv.setResultSize(1);
							attentionCommonAdapter.notifyDataSetChanged();
						}
						if (null != objectList && objectList.size() > 0) {
							lstv.setResultSize(objectList.size());
							attentionDatas.addAll(objectList);
							attentionCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
				}else if ("000000".equals(response.get("resultCode"))){
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								LoadData(state,pageNum);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_item, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		attentionCommonAdapter=new CommonAdapter<FollowUserUtil>(AppApplication.getSingleContext(),attentionDatas,R.layout.fragment_fans_item) {
			@Override
			public void convert(final ViewHolder helper, final FollowUserUtil item) {
				final String followId = item.getArtUserFollowed().getUser().getId();
				helper.setText(R.id.fai_tv_name, item.getArtUserFollowed().getUser().getName());
				if (item.getArtUserFollowed().getUser().getUserBrief()!=null){
					helper.setText(R.id.fai_tv_brief, item.getArtUserFollowed().getUser().getUserBrief().getContent());
				}
				helper.setImageByUrl(R.id.fai_iv_icon, item.getArtUserFollowed().getUser().getPictureUrl());
				helper.getView(R.id.fai_iv_icon).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
							Intent intent=new Intent(AppApplication.getSingleContext(),ArtistIndexActivity.class);
							intent.putExtra("userId", followId);
							intent.putExtra("name", item.getArtUserFollowed().getUser().getName());
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
					}
				});

			}
		};
		lstv.setAdapter(attentionCommonAdapter);
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
		if(attentionDatas!=null&&attentionDatas.size()>0)return;
		LoadData(AutoListView.REFRESH, currentPage);
	}
	@Override
	public void onRefresh() {
		currentPage = 1;
		attentionDatas.clear();
		LoadData(AutoListView.REFRESH,currentPage);
	}
	private void Attention_user(final View v, String followId) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId", AppApplication.gUser.getId());
		paramsMap.put("followId", followId);
		paramsMap.put("identifier", "0");
		paramsMap.put("followType", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "changeFollowStatus.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				//((ImageView) v).setImageResource(R.mipmap.guanzhuhou);
				onRefresh();
			}
		});
	}
	@Override
	public void onLoad() {
		currentPage++;
		LoadData(AutoListView.LOAD, currentPage);
	}

	private void NoAttention_user(final View v, String followId) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId", AppApplication.gUser.getId());
		paramsMap.put("followId", followId);
		paramsMap.put("identifier", "1");
		paramsMap.put("followType", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "changeFollowStatus.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				//((ImageView) v).setImageResource(R.mipmap.guanzhuqian);
				onRefresh();
			}
		});
	}
}