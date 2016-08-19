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
import com.yxh.ryt.R;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkAttachment;
import com.yxh.ryt.vo.Artworkdirection;
import com.yxh.ryt.vo.RongZi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
@SuppressLint("ValidFragment")
public class RongZiXiangQingTab01Fragment extends StickHeaderBaseFragment {


    @Bind(R.id.ll_images)
    LinearLayout llImages;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_guocheng)
    TextView tvGuocheng;
    @Bind(R.id.tv_jiehuo)
    TextView tvJiehuo;

    public RongZiXiangQingTab01Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab01Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab01Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab01Fragment listFragment = new RongZiXiangQingTab01Fragment(manager, position);
        return listFragment;
    }
    public RongZiXiangQingTab01Fragment(){
        super();
    }
    public static RongZiXiangQingTab01Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        RongZiXiangQingTab01Fragment listFragment = new RongZiXiangQingTab01Fragment(manager, position, isCanPulltoRefresh);
        return listFragment;
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rz_xq_tab01, container,false);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        return view;
    }

    @Subscribe
    public void onEventMainThread(  Map<String, Object> objectMap) {
        Artworkdirection artworkdirection = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(objectMap.get("artworkdirection")), Artworkdirection.class);
        List<ArtworkAttachment> artworkAttachments = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(objectMap.get("artworkAttachmentList")), new TypeToken<List<ArtworkAttachment>>() {
        }.getType());
        Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(objectMap.get("artWork")), Artwork.class);
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
        tvContent.setText(artwork.getDescription());
        if (artworkdirection!=null){
            tvGuocheng.setText(artworkdirection.getMake_instru());
            tvJiehuo.setText(artworkdirection.getFinancing_aq());
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
