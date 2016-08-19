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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
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
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.PageinfoList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class UserZanGuoFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<Artwork> userZGCommonAdapter;
	private List<Artwork> userZGDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private TextView tv_noData;
	private static String userId;
	private static String currentId;
	private ZanReceiver receiver;

	public UserZanGuoFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public UserZanGuoFragment() {
		super();
	}
	public UserZanGuoFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static UserZanGuoFragment newInstance(StickHeaderViewPagerManager manager, int position ) {
		UserZanGuoFragment listFragment = new UserZanGuoFragment(manager, position);
		return listFragment;
	}

	public static UserZanGuoFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh, String userID, String currentID) {
		UserZanGuoFragment listFragment = new UserZanGuoFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		userId=userID;
		currentId=currentID;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userZGDatas=new ArrayList<Artwork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_zanguo, container,false);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
		tv_noData = (TextView) view.findViewById(R.id.flz_tv_noData);
		tv_noData.setVisibility(View.GONE);
		lstv = (ListView)view.findViewById(R.id.fiz_lstv);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		setAdapter();
		onScroll();
		receiver = new ZanReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PRAISE_BROADCAST");
		AppApplication.getSingleContext().registerReceiver(receiver, filter);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		userZGDatas.clear();
		userZGCommonAdapter.notifyDataSetChanged();
		currentPage=1;
		LoadData(true, currentPage);
	}

	@Override
	public void onPause() {
		super.onPause();
		userZGDatas.clear();
		if (userZGCommonAdapter!=null){
			userZGCommonAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppApplication.getSingleContext().unregisterReceiver(receiver);
	}
	public class ZanReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			currentPage=currentPage+1;
			LoadData(false,currentPage);
		}
	}
	private void setAdapter() {
		userZGCommonAdapter=new CommonAdapter<Artwork>(AppApplication.getSingleContext(),userZGDatas,R.layout.userpt__zanguo_item) {
			@Override
			public void convert(ViewHolder helper, final Artwork item) {
				if (item!=null){
					helper.setImageByUrl(R.id.uzf_iv_icon, item.getPicture_url());
					helper.setText(R.id.uzf_tv_proName, item.getTitle());
					helper.setText(R.id.uzf_tv_proStage, AppApplication.map.get(item.getStep()));
					helper.setText(R.id.uzf_tv_money,"项目金额:"+item.getInvestGoalMoney());
					helper.setText(R.id.uzf_tv_name,item.getAuthor().getName());
					helper.setText(R.id.uzf_tv_zhicheng,item.getAuthor().getMaster().getTitle());
					helper.setText(R.id.uzf_tv_zanTotal,item.getPraiseNUm()+"");
					helper.setImageByUrl(R.id.uzf_iv_headPortrait,item.getAuthor().getPictureUrl());
					if (item.isPraise()){
						((ImageView) helper.getView(R.id.uzf_iv_zan)).setImageResource(R.mipmap.dianzanhou);
						helper.getView(R.id.uzf_iv_zan).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								cancelPraise(v, item.getId(), AppApplication.gUser.getId());
							}
						});
					}else {
						helper.getView(R.id.uzf_iv_zan).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								praise(v, item.getId(), AppApplication.gUser.getId());
							}
						});
					}
				}
			}
		};
		lstv.setAdapter(userZGCommonAdapter);
		lstv.addFooterView(footer);
		loadFull = (TextView) footer.findViewById(R.id.loadFull);
		noData = (TextView) footer.findViewById(R.id.noData);
		more = (TextView) footer.findViewById(R.id.more);
		loading = (ProgressBar) footer.findViewById(R.id.loading);
		more.setVisibility(View.VISIBLE);
		loading.setVisibility(View.GONE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
	}

	private void praise(final View v, String id, String id1) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId",id);
		paramsMap.put("action ", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}
			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					((ImageView) v).setImageResource(R.mipmap.dianzanhou);
					AnimationSet animationSet=new AnimationSet(true);
					ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.5f,1,1.5f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
					scaleAnimation.setDuration(200);
					animationSet.addAnimation(scaleAnimation);
					animationSet.setFillAfter(true);
					animationSet.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							AnimationSet animationSet=new AnimationSet(true);
							ScaleAnimation scaleAnimation=new ScaleAnimation(1.5f,1f,1.5f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
							scaleAnimation.setDuration(200);
							animationSet.addAnimation(scaleAnimation);
							animationSet.setFillAfter(true);
							((ImageView) v).startAnimation(animationSet);
							currentPage=1;
							userZGDatas.clear();
							LoadData(true,currentPage);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					((ImageView) v).startAnimation(animationSet);
				}
			}
		});
	}

	private void cancelPraise(View v, String id, String id1) {

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
		//paramsMap.put("currentId",currentId);
		paramsMap.put("type","1");
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "followed.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}
			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					if (flag) {
						List<Artwork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<Artwork>>() {
						}.getType());
						if (commentList == null || commentList.size()==0) {
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.VISIBLE);
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
						List<Artwork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<Artwork>>() {
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
}