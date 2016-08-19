package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ArtworkInvest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class RZInvestFragment extends BaseFragment implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

	private String artWorkId;
	private AutoListView invester;
	private List<ArtworkInvest> investorDatas;
	private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
	private int currentPage = 1;
	private List<ArtworkInvest> investList;

	public RZInvestFragment(String artWorkId) {
		super();
		this.artWorkId = artWorkId;
	}

	public RZInvestFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		investorDatas = new ArrayList<>();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rz_invest, container,false);
		invester = (AutoListView) view.findViewById(R.id.at_invester);
		setInvesterAdapter();
		loadInvesterData(AutoListView.REFRESH, currentPage);
		invester.setOnRefreshListener(this);
		invester.setOnLoadListener(this);
		return view;
	}



	private void loadInvesterData(final int state, int pageNum) {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("artWorkId", artWorkId);
		paramsMap.put("pageSize", Constants.pageSize + "");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkInvest.do", paramsMap, new RZCommentCallBack() {

			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> object = (Map<String, Object>) response.get("object");

					investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
					}.getType());

					if (state == AutoListView.REFRESH) {
						invester.onRefreshComplete();
						investorDatas.clear();
						if (null == investList || investList.size() == 0) {
							invester.setResultSize(0);
						}
						if (null != investList && investList.size() > 0) {
							invester.setResultSize(investList.size());
							investorDatas.addAll(investList);
							investorRecordCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
					if (state == AutoListView.LOAD) {
						invester.onLoadComplete();
						if (null == investList || investList.size() == 0) {
							invester.setResultSize(1);
						}
						if (null != investList && investList.size() > 0) {
							invester.setResultSize(investList.size());
							investorDatas.addAll(investList);
							investorRecordCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
				}
			}
		});
	}
	private void setInvesterAdapter() {
		investorRecordCommonAdapter = new CommonAdapter<ArtworkInvest>(getActivity(), investorDatas, R.layout.investorrecord_item) {
			@Override
			public void convert(ViewHolder helper, ArtworkInvest item) {
				if (item.getCreator() != null) {
					helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
					if (item.getCreator().getName() != null) {
						helper.setText(R.id.iri_tv_nickname, item.getCreator().getName());
					}
				}
				helper.getView(R.id.civ_top).setVisibility(View.GONE);
				helper.getView(R.id.cl_01_civ_pm).setVisibility(View.VISIBLE);
				helper.setText(R.id.cl_01_civ_pm, (helper.getPosition() + 1) + "");
				helper.setText(R.id.iri_tv_content, "￥" + item.getPrice());
				helper.setText(R.id.iri_tv_date, DateUtil.millionToNearly(item.getCreateDatetime()));
			}
		};
		invester.setAdapter(investorRecordCommonAdapter);
		invester.setOnLoadListener(this);
		invester.setOnRefreshListener(this);
	}

	private String long2Timestamp(long time) {
		String sTime = DateUtil.date2String(time,"yyyy-MM-dd  HH:mm:ss");
		Date dt = DateUtil.string2Date(sTime,"yyyy-MM-dd  HH:mm:ss");
		return DateUtil.getTimestampString(dt);
	}
	@Override
	protected void lazyLoad() {
		if(investorDatas!=null&&investorDatas.size()>0)return;
		loadInvesterData(AutoListView.REFRESH, currentPage);
	}

	@Override
	public void onLoad() {
		currentPage++;
		loadInvesterData(AutoListView.LOAD,currentPage);
	}

	@Override
	public void onRefresh() {
		currentPage=1;
		loadInvesterData(AutoListView.REFRESH,currentPage);
	}
}