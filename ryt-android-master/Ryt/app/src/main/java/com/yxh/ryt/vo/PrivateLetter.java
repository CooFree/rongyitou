package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/5.
 */
public class PrivateLetter implements Serializable {
    private String id;
    private String content;
    private User fromUser;
    private User targetUser;
    private Long createDatetime;
    private String isWatch; //0 未读 1 已读
    private String status;
    private Long isRead;
    private String cid;

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

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public Long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Long createDatetime) {
        this.createDatetime = createDatetime;
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

    public Long getIsRead() {
        return isRead;
    }

    public void setIsRead(Long isRead) {
        this.isRead = isRead;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
