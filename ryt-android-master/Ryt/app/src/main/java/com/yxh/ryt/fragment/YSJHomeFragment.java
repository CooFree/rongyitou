package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.IcsLinearLayout;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.CreateSuccessActivity;
import com.yxh.ryt.activity.EditProject01Activity;
import com.yxh.ryt.activity.PublicDongtaiImageActivity;
import com.yxh.ryt.activity.RecordVedioActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ChatMsgEntity;
import com.yxh.ryt.vo.HomeYSJArtWork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class YSJHomeFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<HomeYSJArtWork> ySJHomeCommonAdapter;
	private List<HomeYSJArtWork> ySJHomeDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private static String userId,currentId;
	private HomeReceiver receiver;

	public YSJHomeFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public YSJHomeFragment(){
		super();
	}
	public YSJHomeFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static YSJHomeFragment newInstance(StickHeaderViewPagerManager manager, int position) {
		YSJHomeFragment listFragment = new YSJHomeFragment(manager, position);
		return listFragment;
	}

	public static YSJHomeFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh, String userID, String currentID) {
		YSJHomeFragment listFragment = new YSJHomeFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		userId=userID;
		currentId=currentID;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ySJHomeDatas=new ArrayList<HomeYSJArtWork>();
		receiver = new HomeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.HOME_BROADCAST");
		AppApplication.getSingleContext().registerReceiver(receiver, filter);
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_ysj_home,container, false);
		lstv = (ListView)view.findViewById(R.id.fiyh_lstv);
		TextView tvNoData = (TextView) view.findViewById(R.id.fiyh_tv_noData);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
		setAdapter();
		onScroll();
		return view;
	}
	public class HomeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("dududududuudududuudud",currentPage+"");
			currentPage=currentPage+1;
			LoadData(false, currentPage);
		}
	}
	private void setAdapter() {
		ySJHomeCommonAdapter=new CommonAdapter<HomeYSJArtWork>(AppApplication.getSingleContext(),ySJHomeDatas,R.layout.mrdm_project_item) {
			@Override
			public void convert(ViewHolder helper, final HomeYSJArtWork item) {
				helper.setImageByUrl(R.id.mpi_iv_icon, item.getPicture_url());
				helper.setText(R.id.mpi_tv_tilte, item.getTitle());
				if(item.getDescription()!=null){
					helper.setText(R.id.mpi_iv_project,"项目描述:"+item.getDescription());
				}else {
					helper.setText(R.id.mpi_iv_project,"目前无项目描述");
				}
				helper.setText(R.id.mpi_tv_money,item.getInvestGoalMoney()+"元");
				helper.setText(R.id.mpi_tv_stage,AppApplication.map.get(item.getStep()));
				helper.getView(R.id.mpi_tv_left).setVisibility(View.GONE);
				helper.getView(R.id.mpi_tv_right).setVisibility(View.GONE);
				if ("100".equals(item.getStep())){
					helper.getView(R.id.mpi_tv_left).setVisibility(View.VISIBLE);
					((TextView) helper.getView(R.id.mpi_tv_left)).setText("提交项目");
					((TextView) helper.getView(R.id.mpi_tv_left)).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							submitArtwork(item.getId(), userId, "10");
						}
					});
					helper.getView(R.id.mpi_tv_right).setVisibility(View.VISIBLE);
					((TextView) helper.getView(R.id.mpi_tv_right)).setText("编辑项目");
					helper.getView(R.id.mpi_tv_right).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(getActivity(), EditProject01Activity.class);
							intent.putExtra("artWorkId",item.getId());
							intent.putExtra("currentUserId",AppApplication.gUser.getId());
							getActivity().startActivity(intent);
						}
					});
				}else if ("21".equals(item.getStep()) || "22".equals(item.getStep())){
					helper.getView(R.id.mpi_tv_left).setVisibility(View.VISIBLE);
					helper.getView(R.id.mpi_tv_left).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(getActivity(), CreateSuccessActivity.class);
							intent.putExtra("artworkId",item.getId());
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});
					((TextView) helper.getView(R.id.mpi_tv_left)).setText("创作完成");
					helper.getView(R.id.mpi_tv_right).setVisibility(View.VISIBLE);
					((TextView) helper.getView(R.id.mpi_tv_right)).setText("发布动态");
					helper.getView(R.id.mpi_tv_right).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dynamic(item.getId());
						}
					});
				}

			}
		};
		lstv.setAdapter(ySJHomeCommonAdapter);
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

	private void dynamic(final String id) {
		new ActionSheetDialog(getActivity())
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("发布图片", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent=new Intent(getActivity(), PublicDongtaiImageActivity.class);
								intent.putExtra("artWorkId",id);
								getActivity().startActivity(intent);
							}
						})
				.addSheetItem("发布视频", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent=new Intent(getActivity(), RecordVedioActivity.class);
								intent.putExtra("artWorkId",id);
								getActivity().startActivity(intent);
							}
						})
				.show();
	}

	private void submitArtwork(String id, String userId, String s) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId", id);
		paramsMap.put("userId", userId);
		paramsMap.put("step", s);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "updateArtWork.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				ySJHomeDatas.clear();
				currentPage = 1;
				LoadData(true, currentPage);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
		ySJHomeDatas.clear();
		currentPage=1;
		LoadData(true, currentPage);
	}

	private void onScroll() {

	}

	@Override
	protected void lazyLoad() {


	}
	public void LoadData(final boolean flag,int pageNum) {
		more.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId",userId);
		paramsMap.put("action","1");
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "userMain.do", paramsMap, new RZCommentCallBack() {
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
						ySJHomeDatas.clear();
						List<HomeYSJArtWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
						}.getType());
						if (commentList == null) {
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.VISIBLE);
						} else if (commentList.size() < Constants.pageSize){
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.VISIBLE);
							noData.setVisibility(View.GONE);
							ySJHomeDatas.addAll(commentList);
							commentList.clear();
							lstv.requestLayout();
							ySJHomeCommonAdapter.notifyDataSetChanged();
						}else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							ySJHomeDatas.addAll(commentList);
							commentList.clear();
						}
						lstv.requestLayout();
						ySJHomeCommonAdapter.notifyDataSetChanged();
					}else {
						List<HomeYSJArtWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
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
							ySJHomeDatas.addAll(commentList);
							commentList.clear();
						}
						lstv.requestLayout();
						ySJHomeCommonAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppApplication.getSingleContext().unregisterReceiver(receiver);
	}
	@Override
	public void onPause() {
		super.onPause();
		ySJHomeDatas.clear();
		if (ySJHomeCommonAdapter!=null){
			ySJHomeCommonAdapter.notifyDataSetChanged();
		}
	}
}