package com.efeiyi.ec.art.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/4/7.
 */

public class InvestorTopListVO implements Serializable {
    private String user_id;
    private String truename;
    private BigDecimal price;
    private BigDecimal rois;
    private String username;
    public InvestorTopListVO(){
        super();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRois() {
        return rois;
    }

    public void setRois(BigDecimal rois) {
        this.rois = rois;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
