package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Notification implements Serializable {
    private String content;
    private long createDatetime;
    private int isWatch;
    private User fromUser;

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Notification(long createDatetime, String content, int isWatch, User fromUser) {
        this.createDatetime = createDatetime;
        this.content = content;
        this.isWatch = isWatch;
        this.fromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public int getIsWatch() {
        return isWatch;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setIsWatch(int isWatch) {
        this.isWatch = isWatch;
    }

}
