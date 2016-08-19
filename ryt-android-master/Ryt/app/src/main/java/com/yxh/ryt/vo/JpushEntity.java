package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/26.
 */
public class JpushEntity implements Serializable{
    private String userId;

    public JpushEntity(String userId) {
        this.userId = userId;
    }

    public JpushEntity() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
