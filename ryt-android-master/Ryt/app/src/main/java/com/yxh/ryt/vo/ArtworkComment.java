package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ArtworkComment implements Serializable{
    private String id;
    private Artwork artwork;
    private String content;
    private User creator;
    private long createDatetime;
    private String status;
    private String isWatch;
    private ArtworkComment fatherComment;
    private List<ArtworkComment> subComment;

    public ArtworkComment(String id, Artwork artwork, String content, User creator, long createDatetime, String isWatch, String status, ArtworkComment fatherComment, List<ArtworkComment> subComment) {
        this.id = id;
        this.artwork = artwork;
        this.content = content;
        this.creator = creator;
        this.createDatetime = createDatetime;
        this.isWatch = isWatch;
        this.status = status;
        this.fatherComment = fatherComment;
        this.subComment = subComment;
    }

    public String getId() {
        return id;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public String getContent() {
        return content;
    }

    public User getCreator() {
        return creator;
    }


    public String getStatus() {
        return status;
    }

    public String getIsWatch() {
        return isWatch;
    }

    public ArtworkComment getFatherComment() {
        return fatherComment;
    }

    public List<ArtworkComment> getSubComment() {
        return subComment;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubComment(List<ArtworkComment> subComment) {
        this.subComment = subComment;
    }

    public void setFatherComment(ArtworkComment fatherComment) {
        this.fatherComment = fatherComment;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
