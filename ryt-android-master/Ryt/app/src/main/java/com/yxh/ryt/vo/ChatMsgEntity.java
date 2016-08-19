
package com.yxh.ryt.vo;

import java.io.Serializable;

public class ChatMsgEntity implements Serializable {
    private static final String TAG = ChatMsgEntity.class.getSimpleName();

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private String picUrl;
    private String name;

    private String date;

    private String text;
    
    private String time;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isComMeg() {
        return isComMeg;
    }

    public void setIsComMeg(boolean isComMeg) {
        this.isComMeg = isComMeg;
    }

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private boolean isComMeg = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMeg = isComMsg;
    }

}
