package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ArtworkCommentMsg implements Serializable{
    private String id;
    private Artwork artwork;
    private String content;
    private User creator;
    private long createDatetime;
    private String status;
    private String isWatch;
    private ArtworkCommentMsg fatherArtworkCommentBean;

    public ArtworkCommentMsg(ArtworkCommentMsg fatherArtworkCommentBean, String isWatch, String status, long createDatetime, User creator, String content, Artwork artwork, String id) {
        this.fatherArtworkCommentBean = fatherArtworkCommentBean;
        this.isWatch = isWatch;
        this.status = status;
        this.createDatetime = createDatetime;
        this.creator = creator;
        this.content = content;
        this.artwork = artwork;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArtworkCommentMsg getFatherArtworkCommentBean() {
        return fatherArtworkCommentBean;
    }

    public void setFatherArtworkCommentBean(ArtworkCommentMsg fatherArtworkCommentBean) {
        this.fatherArtworkCommentBean = fatherArtworkCommentBean;
    }

    public String getIsWatch() {
        return isWatch;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
