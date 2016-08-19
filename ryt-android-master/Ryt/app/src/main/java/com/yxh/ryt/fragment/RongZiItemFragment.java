package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.yxh.ryt.activity.FinanceSummaryActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.DisplayUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class RongZiItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
	private AutoListView lstv;
	private CommonAdapter<RongZi> rongZiCommonAdapter;
	private List<RongZi> rongZiDatas;
	private int currentPage=1;
	private TextView view1,view2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rongZiDatas=new ArrayList<RongZi>();
	}
	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		/*if ("".equals(AppApplication.gUser.getId())){
			paramsMap.put("userId", "");
		}else {
			paramsMap.put("userId", AppApplication.gUser.getId());
		}*/
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
				if (state == AutoListView.REFRESH) {
					lstv.onRefreshComplete();
					rongZiDatas.clear();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
							toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if (null == objectList || objectList.size() == 0) {
						lstv.setResultSize(0);
					}
					if (null != objectList && objectList.size() > 0) {
						lstv.setResultSize(objectList.size());
						rongZiDatas.addAll(objectList);
						rongZiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state == AutoListView.LOAD) {
					lstv.onLoadComplete();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
							toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if (null == objectList || objectList.size() == 0) {
						lstv.setResultSize(1);
					}
					if (null != objectList && objectList.size() > 0) {
						lstv.setResultSize(objectList.size());
						rongZiDatas.addAll(objectList);
						rongZiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
			}
		});
	}

	public RongZiItemFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_item, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		rongZiCommonAdapter=new CommonAdapter<RongZi>(AppApplication.getSingleContext(),rongZiDatas,R.layout.finance_list_item) {

			@Override
			public void convert(ViewHolder helper, final RongZi item) {
				if (item!=null){
					/*helper.setText(R.id.cl_01_tv_title,item.getTitle());
					helper.setText(R.id.cl_01_tv_brief,item.getBrief());
					helper.setText(R.id.cl_01_tv_name,item.getAuthor().getName());
					helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
					helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
					helper.getView(R.id.cl_01_civ_headPortrait).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), UserYsjIndexActivity.class);
							intent.putExtra("userId", item.getAuthor().getId());
							intent.putExtra("currentId", AppApplication.gUser.getId());
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});*/
					/*LinearLayout linearLayout=helper.getView(R.id.fli_ll_all);
					addText(linearLayout,item);*/
					/*TextView textView=helper.getView(R.id.fli_tv_all);
					String fatherUser = item.getInvestGoalMoney().toString()+"元";
					SpannableString spanFatherUser = new SpannableString(fatherUser);
					ClickableSpan click= new ShowColorSpan(fatherUser, AppApplication.getSingleContext());
					spanFatherUser.setSpan(click, 0, fatherUser.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
					String fatherUser1 = item.getInvestsMoney().toString()+"元";
					SpannableString spanFatherUser1 = new SpannableString(fatherUser1);
					ClickableSpan click1= new ShowColorSpan(fatherUser1, AppApplication.getSingleContext());
					spanFatherUser1.setSpan(click1, 0, fatherUser1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
					String fatherUser2 = item.getInvestRestTime()+"截止";
					//String fatherUser2 = "截止截止截止截止截止截止截止截止截止截止";
					SpannableString spanFatherUser2 = new SpannableString(fatherUser2);
					ClickableSpan click2= new ShowColorSpan(fatherUser2, AppApplication.getSingleContext());
					spanFatherUser2.setSpan(click2, 0, fatherUser2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
					textView.setText("目标:");
					textView.append(spanFatherUser);
					textView.append(" "+"|"+" ");
					textView.append("已投:");
					textView.append(spanFatherUser1);
					textView.append(" "+"|"+" ");
					textView.append(spanFatherUser2);*/
					helper.setText(R.id.cl_01_tv_title,item.getTitle());
					helper.setText(R.id.cl_01_tv_brief,item.getBrief());
					if (item.getAuthor()!=null){
						helper.setText(R.id.cl_01_tv_name,item.getAuthor().getName()+"");
						helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
					}
					helper.setText(R.id.fli_ll_tv_investGoalMoney,item.getInvestGoalMoney()+"元");
					//helper.setText(R.id.fli_ll_tv_remainingTime, Utils.getJudgeDate(item.getInvestRestTime()));
					view1 = ((TextView) helper.getView(R.id.fli_ll_tv_investGoalMoney));
					view2= ((TextView) helper.getView(R.id.fli_ll_tv_remainingTime));
					//helper.setText(R.id.fli_ll_tv_remainingTime, item.getInvestRestTime());
					helper.setText(R.id.fli_ll_tv_investGoalPeople, item.getInvestNum()+"");
					helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
					if (null!=item.getAuthor().getMaster()&&!"".equals(item.getAuthor().getMaster().getTitle())){
						helper.getView(R.id.cl_01_ll_zhicheng).setVisibility(View.VISIBLE);
						helper.setText(R.id.cl_01_tv_zhicheng, item.getAuthor().getMaster().getTitle());
					}else{
						helper.getView(R.id.cl_01_ll_zhicheng).setVisibility(View.GONE);
					}
					double value = item.getInvestsMoney().doubleValue() / item.getInvestGoalMoney().doubleValue();
					helper.setProgress(R.id.progressBar1, (int)(value*100));
					helper.setText(R.id.tv_pb_value, (int) (value * 100) + "%");
					helper.getView(R.id.cl_01_civ_headPortrait).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(getActivity(), ArtistIndexActivity.class);
							intent.putExtra("userId",item.getAuthor().getId());
							intent.putExtra("name", item.getAuthor().getName());
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});
					int i = DisplayUtil.px2dip(44);
					float density = AppApplication.getSingleContext().getResources().getDisplayMetrics().density;
					Log.d("xxxxxxx",i+""+density);
				}
			}
		};
		lstv.setOnItemClickListener(this);
		lstv.setAdapter(rongZiCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		return contextView;
	}

	private void addText(LinearLayout linearLayout, RongZi item) {
		LinearLayout linearLayout1=new LinearLayout(getContext());
		linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		TextView view=new TextView(getContext());
		view.setTextColor(Color.rgb(153,153,153));
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		view.setText("");
		/*String fatherUser = item.getInvestGoalMoney().toString()+"元";
		SpannableString spanFatherUser = new SpannableString(fatherUser);
		ClickableSpan click= new ShowColorSpan(fatherUser, AppApplication.getSingleContext());
		spanFatherUser.setSpan(click, 0, fatherUser.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		String fatherUser1 = item.getInvestsMoney().toString()+"元";
		SpannableString spanFatherUser1 = new SpannableString(fatherUser1);
		ClickableSpan click1= new ShowColorSpan(fatherUser1, AppApplication.getSingleContext());
		spanFatherUser1.setSpan(click1, 0, fatherUser1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		String fatherUser2 = item.getInvestRestTime()+"截止";
		//String fatherUser2 = "截止截止截止截止截止截止截止截止截止截止";
		SpannableString spanFatherUser2 = new SpannableString(fatherUser2);
		ClickableSpan click2= new ShowColorSpan(fatherUser2, AppApplication.getSingleContext());
		spanFatherUser2.setSpan(click2, 0, fatherUser2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*/
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
			/*int[] location = new  int[2] ;
			view1.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
			view1.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
			System.out.println("view--->x坐标:"+location [0]+"view--->y坐标:"+location [1]);
			int[] location1 = new  int[2] ;
			view2.getLocationInWindow(location1); //获取在当前窗口内的绝对坐标
			view2.getLocationOnScreen(location1);//获取在整个屏幕内的绝对坐标
			System.out.println("view--->x坐标:"+location1 [0]+"view--->y坐标:"+location1 [1]);*/
			Intent intent=new Intent(getActivity(), FinanceSummaryActivity.class);
			intent.putExtra("id",rongZiDatas.get(position-1).getId());
			startActivity(intent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}
}