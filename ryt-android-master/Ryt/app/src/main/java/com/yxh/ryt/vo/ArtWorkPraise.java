package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ArtWorkPraise implements Serializable{

    private String id;

    private Artwork artwork;//点赞项目

    private User user;//点赞用户

    private Date createDateTime;//点赞时间

    private String status;// 0 删除  1使用

    private String watch;//0 未读  1 已读

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }
}
