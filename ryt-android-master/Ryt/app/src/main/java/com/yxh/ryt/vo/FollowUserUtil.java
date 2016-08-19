package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/26.
 */
public class FollowUserUtil implements Serializable {
    private ArtUserFollowed artUserFollowed;
    private UserBrief userBrief;
    private Master master;
    private String flag;// 1 关注 2 未关注

    public ArtUserFollowed getArtUserFollowed() {
        return artUserFollowed;
    }

    public void setArtUserFollowed(ArtUserFollowed artUserFollowed) {
        this.artUserFollowed = artUserFollowed;
    }

    public UserBrief getUserBrief() {
        return userBrief;
    }

    public void setUserBrief(UserBrief userBrief) {
        this.userBrief = userBrief;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
