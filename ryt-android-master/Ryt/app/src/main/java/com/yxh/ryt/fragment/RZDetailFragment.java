package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkAttachment;
import com.yxh.ryt.vo.Artworkdirection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
@SuppressLint("ValidFragment")
public class RZDetailFragment extends BaseFragment {

	private  String artWorkId;
	private LinearLayout llImages;
	private TextView tvContent;
	private TextView tvJiehuo;
	private TextView tvGuocheng;

	public RZDetailFragment(String artWorkId) {
		this.artWorkId = artWorkId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public RZDetailFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rz_detail, container,false);
		//View view = inflater.inflate(R.layout.rz_xq_tab01, null);
		llImages = (LinearLayout) view.findViewById(R.id.ll_images);
		tvContent = (TextView) view.findViewById(R.id.tv_content);
		tvJiehuo = (TextView) view.findViewById(R.id.tv_jiehuo);
		tvGuocheng = (TextView) view.findViewById(R.id.tv_guocheng);
		initView();
		return view;
	}

	public void initView() {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("artWorkId", artWorkId + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(paramsMap.toString() + "====");
		NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				Map<String, Object> object = (Map<String, Object>) response.get("object");
				if (object == null){return;}
				Artworkdirection artworkdirection = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkdirection")), Artworkdirection.class);
				List<ArtworkAttachment> artworkAttachments = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkAttachmentList")), new TypeToken<List<ArtworkAttachment>>() {
				}.getType());
				Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")), Artwork.class);
				if(artworkAttachments!=null&&artworkAttachments.size()>0){
					llImages.removeAllViewsInLayout();
					int count=0;
					for (ArtworkAttachment artworkAttachment:artworkAttachments){
						ImageView imageView=new ImageView(AppApplication.getSingleContext());
						LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						if (count>0)layoutParams.setMargins(0,10,0,0);
						imageView.setLayoutParams(layoutParams);
						llImages.addView(imageView);
						AppApplication.displayImage(artworkAttachment.getFileName(), imageView);
						count++;
					}
				}
				tvContent.setText("\u3000\u3000" + artwork.getDescription());
				if (artworkdirection!=null){
					tvGuocheng.setText(artworkdirection.getMake_instru());
					tvJiehuo.setText(artworkdirection.getFinancing_aq());
				}
			}
		});

	}
	@Override
	protected void lazyLoad() {

	}

}