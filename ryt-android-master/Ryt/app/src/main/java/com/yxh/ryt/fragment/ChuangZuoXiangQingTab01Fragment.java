package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yxh.ryt.R;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkMessage;
import com.yxh.ryt.vo.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
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
public class ChuangZuoXiangQingTab01Fragment extends StickHeaderBaseFragment {


    @Bind(R.id.ll_dongtai)
    LinearLayout llDongtai;

    List<ArtworkMessage> artworkMessages=new ArrayList<>();
    public ChuangZuoXiangQingTab01Fragment(){
        super();
    }
    public ChuangZuoXiangQingTab01Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public ChuangZuoXiangQingTab01Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static ChuangZuoXiangQingTab01Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        ChuangZuoXiangQingTab01Fragment listFragment = new ChuangZuoXiangQingTab01Fragment(manager, position);
        return listFragment;
    }

    public static ChuangZuoXiangQingTab01Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        ChuangZuoXiangQingTab01Fragment listFragment = new ChuangZuoXiangQingTab01Fragment(manager, position, isCanPulltoRefresh);
        return listFragment;
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cz_xq_tab01, container,false);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);

        return view;
    }
    @Subscribe
    public void onEventMainThread(Map<String, Object> objectMap) {
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

    void createData(){
//        Artwork artwork=new Artwork();
//        artwork.setId("in5z7r5f2w2f73so");
//        ArtworkComment artworkComment=new ArtworkComment();
//        User user=new User();
//        for (int i  = 0; i < 10 ;i++){
//            ArtworkMessage artworkMessage=new ArtworkMessage();
//            artworkMessage.setArtwork(artwork);
//            artworkMessage.setArtworkCommentList();
//            artworkMessage.setArtworkMessageAttachments();
//            artworkMessage.setArtWorkPraiseList();
//            artworkMessage.setContent("是大叔大婶大婶大婶大的萨迪");
//            artworkMessage.setCreateDatetime(new Date());
//            artworkMessage.setCreator(user);
//            artworkMessage.setId(i+"");
//            artworkMessage.setStatus("1");
//            artworkMessages.add(artworkMessage);
//        }
    }
}
