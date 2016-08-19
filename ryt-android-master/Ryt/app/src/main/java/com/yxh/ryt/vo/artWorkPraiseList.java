package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ArtWorkPraiseList implements Serializable {
    private User user;
    private boolean followed;

    public User getUser() {
        return user;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
