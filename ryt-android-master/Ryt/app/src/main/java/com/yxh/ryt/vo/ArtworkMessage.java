package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ArtworkMessage implements Serializable{//项目动态
    private String id;
    private String content;
    private User creator;
    private Date createDatetime;
    private Artwork artwork;
    private String status;
    private List<ArtworkMessageAttachment> artworkMessageAttachments;
    private List<ArtWorkPraise> artWorkPraiseList; //点赞
    private List<ArtworkComment> artworkCommentList;//评论

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ArtworkMessageAttachment> getArtworkMessageAttachments() {
        return artworkMessageAttachments;
    }

    public void setArtworkMessageAttachments(List<ArtworkMessageAttachment> artworkMessageAttachments) {
        this.artworkMessageAttachments = artworkMessageAttachments;
    }

    public List<ArtWorkPraise> getArtWorkPraiseList() {
        return artWorkPraiseList;
    }

    public void setArtWorkPraiseList(List<ArtWorkPraise> artWorkPraiseList) {
        this.artWorkPraiseList = artWorkPraiseList;
    }

    public List<ArtworkComment> getArtworkCommentList() {
        return artworkCommentList;
    }

    public void setArtworkCommentList(List<ArtworkComment> artworkCommentList) {
        this.artworkCommentList = artworkCommentList;
    }
}

