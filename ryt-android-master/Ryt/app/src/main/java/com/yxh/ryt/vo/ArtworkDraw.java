package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;

public class ArtworkDraw implements Serializable{
    private String id;
    private Artwork artwork;
    private User luckyUser;
    private User creator;
    private Date createDatetime;

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

    public User getLuckyUser() {
        return luckyUser;
    }

    public void setLuckyUser(User luckyUser) {
        this.luckyUser = luckyUser;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}






