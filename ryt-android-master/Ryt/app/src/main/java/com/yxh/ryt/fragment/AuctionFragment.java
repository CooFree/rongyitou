package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.AuctionSummaryActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.AnimPraiseCancel;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.LoadingUtil;
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
public class AuctionFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
	private AutoListView lstv;
	private CommonAdapter<Create> rongZiCommonAdapter;
	private List<Create> rongZiDatas;
	private int currentPage=1;
	private Map<Integer,Boolean> selected;
	private int width;
	private int height;
	private LoadingUtil loadingUtil;
	private Map<Integer,Integer> number;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rongZiDatas=new ArrayList<Create>();
		selected=new HashMap<>();
		number=new HashMap<>();
		loadingUtil = new LoadingUtil(getActivity(),getContext());
	}

	public AuctionFragment() {
	}

	private void LoadData(final int state, final int pageNum) {
		final Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize", Constants.pageSize+"");
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
			public void onResponse(final Map<String, Object> response) {
				if ("".equals(AppApplication.gUser.getId())){
					if (state == AutoListView.REFRESH) {
						lstv.onRefreshComplete();
						rongZiDatas.clear();
						List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<Create>>() {
						}.getType());
						if (null == objectList || objectList.size() == 0) {
							lstv.setResultSize(0);
						}
						if (null != objectList && objectList.size() > 0) {
							lstv.setResultSize(objectList.size());
							rongZiDatas.addAll(objectList);
							rongZiCommonAdapter.notifyDataSetChanged();
						}
					}
					if (state == AutoListView.LOAD) {
						lstv.onLoadComplete();
						List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<Create>>() {
						}.getType());
						if (null == objectList || objectList.size() == 0) {
							lstv.setResultSize(1);
						}
						if (null != objectList && objectList.size() > 0) {
							lstv.setResultSize(objectList.size());
							rongZiDatas.addAll(objectList);
							rongZiCommonAdapter.notifyDataSetChanged();
						}
					}
					if (pageNum==1 && selected.size()>0){
						selected.clear();
						number.clear();
					}
					if (selected.size()<=rongZiDatas.size()){
						for (int i=selected.size();i<rongZiDatas.size();i++){
							selected.put(i, false);
							number.put(i,rongZiDatas.get(i).getPraiseNUm());

						}
					}
				}else {
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								NetRequestUtil.post(Constants.BASE_PATH + "artWorkAuctionList.do", paramsMap, new RongZiListCallBack() {
									@Override
									public void onError(Call call, Exception e) {
										ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
									}

									@Override
									public void onResponse(Map<String, Object> response) {
										if (state == AutoListView.REFRESH) {
											lstv.onRefreshComplete();
											rongZiDatas.clear();
											List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<Create>>() {
											}.getType());
											if (null == objectList || objectList.size() == 0) {
												lstv.setResultSize(0);
											}
											if (null != objectList && objectList.size() > 0) {
												lstv.setResultSize(objectList.size());
												rongZiDatas.addAll(objectList);
												rongZiCommonAdapter.notifyDataSetChanged();
											}
										}
										if (state == AutoListView.LOAD) {
											lstv.onLoadComplete();
											List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<Create>>() {
											}.getType());
											if (null == objectList || objectList.size() == 0) {
												lstv.setResultSize(1);
											}
											if (null != objectList && objectList.size() > 0) {
												lstv.setResultSize(objectList.size());
												rongZiDatas.addAll(objectList);
												rongZiCommonAdapter.notifyDataSetChanged();
											}
										}
										if (pageNum==1 && selected.size()>0){
											selected.clear();
											number.clear();
										}
										if (selected.size()<=rongZiDatas.size()){
											for (int i=selected.size();i<rongZiDatas.size();i++){
												selected.put(i,rongZiDatas.get(i).isPraise());
												number.put(i,rongZiDatas.get(i).getPraiseNUm());
											}
										}
									}
								});
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
		rongZiCommonAdapter=new CommonAdapter<Create>(AppApplication.getSingleContext(),rongZiDatas,R.layout.auction_list_item1) {

			@Override
			public void convert(final ViewHolder helper, final Create item) {
				if (item!=null){
					helper.setText(R.id.clh1_tv_title,item.getTitle());
					helper.setText(R.id.clh1_tv_brief,item.getBrief());
					if (item.getAuthor()!=null){
						helper.setText(R.id.clh1_tv_artistName,item.getAuthor().getName()+"");
						helper.setImageByUrl(R.id.clh1_cv_headerImage,item.getAuthor().getPictureUrl());
						helper.setText(R.id.clh1_tv_totalWork,item.getAuthor().getMasterWorkNum()+"件作品");
						helper.setText(R.id.clh1_tv_totalFans,item.getAuthor().getFansNum()+"个粉丝");
						helper.getView(R.id.clh1_cv_headerImage).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(getActivity(), ArtistIndexActivity.class);
								intent.putExtra("userId",item.getAuthor().getId());
								intent.putExtra("name",item.getAuthor().getName()+"");
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								getActivity().startActivity(intent);
							}
						});
					}
					if ("30".equals(item.getStep())){
						helper.setText(R.id.ali1_tv_content,"拍卖时间 "+Utils.timeAuction(item.getAuctionStartDatetime()));
						helper.setText(R.id.clh1_tv_state,"拍卖预告");
						helper.getView(R.id.ali1_iv_date).setVisibility(View.VISIBLE);
						helper.getView(R.id.ali1_iv_finalPerson).setVisibility(View.GONE);
					}else if ("31".equals(item.getStep())){
						helper.setText(R.id.ali1_tv_content, DateUtil.millionToDead(item.getAuctionEndDatetime())+"后截止");
						helper.setText(R.id.clh1_tv_state,"拍卖中");
						helper.getView(R.id.ali1_iv_date).setVisibility(View.VISIBLE);
						helper.getView(R.id.ali1_iv_finalPerson).setVisibility(View.GONE);
					}else {
						helper.setText(R.id.ali1_tv_content,"拍卖得主 "+item.getWinner().getName());
						helper.setText(R.id.clh1_tv_state,"拍卖结束");
						helper.getView(R.id.ali1_iv_date).setVisibility(View.GONE);
						helper.getView(R.id.ali1_iv_finalPerson).setVisibility(View.VISIBLE);
					}
					helper.setImageByUrl(R.id.clh1_tv_prc, item.getPicture_url());
					if (null!=item.getAuthor().getMaster()&&!"".equals(item.getAuthor().getMaster().getTitle())){
						helper.getView(R.id.clh1_tv_artistTitle).setVisibility(View.VISIBLE);
						helper.setText(R.id.clh1_tv_artistTitle, item.getAuthor().getMaster().getTitle());
					}else{
						helper.getView(R.id.clh1_tv_artistTitle).setVisibility(View.GONE);
					}
					helper.getView(R.id.clh1_ll_praise).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (selected.get(helper.getPosition())) {
								cancelPraise(item.getId(), ((LinearLayout) helper.getView(R.id.clh1_ll_praise)), ((TextView) helper.getView(R.id.clh1_tv_praiseNum)), number.get(helper.getPosition()), helper);
								helper.getView(R.id.clh1_ll_praise).setEnabled(false);
							}else {
								if ("".equals(AppApplication.gUser.getId())) {
									Intent intent = new Intent(getActivity(), LoginActivity.class);
									intent.putExtra("userId", item.getAuthor().getId());
									intent.putExtra("currentId", AppApplication.gUser.getId());
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									getActivity().startActivity(intent);
								} else {
									praise(item.getId(), ((LinearLayout) helper.getView(R.id.clh1_ll_praise)), ((TextView) helper.getView(R.id.clh1_tv_praiseNum)), number.get(helper.getPosition()), helper);
									helper.getView(R.id.clh1_ll_praise).setEnabled(false);
								}
							}
						}
					});
					if (selected.get(helper.getPosition())) {
						helper.getView(R.id.clh1_ll_praise).setBackgroundResource(R.drawable.praise_after_shape);
						((TextView) helper.getView(R.id.clh1_tv_praiseNum)).setTextColor(Color.rgb(255, 255, 255));
						helper.setText(R.id.clh1_tv_praiseNum, number.get(helper.getPosition()) + "");
					} else {
						helper.getView(R.id.clh1_ll_praise).setBackgroundResource(R.drawable.praise_shape);
						((TextView) helper.getView(R.id.clh1_tv_praiseNum)).setTextColor(Color.rgb(199, 31, 33));
						helper.setText(R.id.clh1_tv_praiseNum, number.get(helper.getPosition()) + "");

					}
				}
			}
		};
		lstv.setOnItemClickListener(this);
		lstv.setAdapter(rongZiCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		return contextView;
	}
	private void cancelPraise(final String id, final LinearLayout view, final TextView textView, final int praiseNum, final ViewHolder helper) {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("artworkId", id + "");
		paramsMap.put("action", "0");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					if (selected.get(helper.getPosition())) {
						helper.getView(R.id.clh1_ll_praise).setEnabled(true);
						view.setBackgroundResource(R.drawable.praise_shape);
						AnimPraiseCancel.animCancelPraise(helper.getView(R.id.iv_xin_hui_left),helper.getView(R.id.iv_xin_hui_right));
						textView.setTextColor(Color.rgb(199, 31, 33));
						String raw = textView.getText().toString();
						number.put(helper.getPosition(), praiseNum - 1);
						textView.setText(Integer.valueOf(raw) - 1 + "");
						selected.put(helper.getPosition(), false);
					} else {
						praise(id, view, textView, praiseNum, helper);
					}
				} else if ("000000".equals(response.get("resultCode"))) {
					SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)) {
								cancelPraise(id, view, textView, praiseNum, helper);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}
			}
		});

	}



	private void praise(final String artworkId, final LinearLayout view, final TextView textView, final int praiseNum, final ViewHolder helper) {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("artworkId", artworkId + "");
		paramsMap.put("action", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					if (!selected.get(helper.getPosition())) {
						helper.getView(R.id.clh1_ll_praise).setEnabled(true);
						view.setBackgroundResource(R.drawable.praise_after_shape);
						AnimPraiseCancel.animPraise(helper.getView(R.id.iv_praise_red));
						textView.setTextColor(Color.rgb(255, 255, 255));
						textView.setText(praiseNum + 1 + "");
						number.put(helper.getPosition(), praiseNum + 1);
						selected.put(helper.getPosition(), true);
						//向上平移
                        /*Animator anim1 = ObjectAnimator.ofFloat(helper.getView(R.id.iv_praise_red),"translationY",0f,-150f);
                        anim1.setDuration(2000);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playSequentially(anim1);
                        animatorSet.start();*/
                       /* TranslateAnimation anim1 = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0f,
                                TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0f);
                        anim1.setDuration(3000);
                        anim1.setFillAfter(true);
                        helper.getView(R.id.iv_praise).startAnimation(anim1);*/
                        /*AnimationSet animatorSet = new AnimationSet(true);
                        Animation translateAnimation= AnimationUtils.loadAnimation(getActivity(), R.anim.translate);//加载Xml文件中的动画
                        Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha);
                        animatorSet.addAnimation(translateAnimation);*/
						//animatorSet.setInterpolator(getActivity(), android.R.anim.anticipate_interpolator);
						//animatorSet.addAnimation(animatorSet);
					} else {
						cancelPraise(artworkId, view, textView, praiseNum, helper);
					}
				} else if ("000000".equals(response.get("resultCode"))) {
					SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)) {
								praise(artworkId, view, textView, praiseNum, helper);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}
			}
		});

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	protected void lazyLoad() {
		if(rongZiDatas!=null&&rongZiDatas.size()>0)return;
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
		LoadData(AutoListView.LOAD, currentPage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position<=rongZiDatas.size()){
			Intent intent=new Intent(getActivity(), AuctionSummaryActivity.class);
			intent.putExtra("id", rongZiDatas.get(position-1).getId());
			intent.putExtra("title", rongZiDatas.get(position-1).getTitle());
			intent.putExtra("name", rongZiDatas.get(position-1).getAuthor().getName());
			intent.putExtra("userId", rongZiDatas.get(position-1).getAuthor().getId());
			startActivity(intent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}
}